---
title: "BootThemeStore crashes on first launch due to nullable Flow"
date: "2026-04-24"
category: docs/solutions/runtime-errors/
module: boot-app-kmp/shared
problem_type: runtime_error
component: database
symptoms:
  - "IllegalStateException: The query result was empty, but expected a single row to return a NON-NULL object of type 'com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeSettings'"
  - "App crashes on first launch when theme settings have not yet been persisted"
  - "BootThemeStore assumes Flow<ThemeSettings> but ThemeDao returns Flow<ThemeSettings?>"
root_cause: logic_error
resolution_type: code_fix
severity: high
related_components:
  - shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/data/theme/room3/ThemeDao.kt
  - shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/data/theme/BootThemeStore.kt
  - shared/src/commonTest/kotlin/com/yuanjingtech/boot/app/kmp/data/theme/BootThemeStoreTest.kt
tags:
  - room
  - kotlin-flow
  - null-safety
  - theme-settings
---

# BootThemeStore crashes on first launch due to nullable Flow

## Problem

`BootThemeStore` throws an `IllegalStateException` at runtime on iOS/WASM/JS platforms during first launch when the `theme_settings` Room database table is empty, because `settings!!.themeMode` failed when `settings` was null.

## Symptoms

- App crashes on first launch with `IllegalStateException`
- No crash on subsequent launches (after a row is inserted into `theme_settings`)
- Platform-specific: reproducible on iOS, WASM, and JS targets; not on Android JVM where Room may synthesize a non-null default row
- Stack trace points to the `map` block in `BootThemeStore.kt` consuming the null-emitted Flow

## What Didn't Work

- Initially the code used `settings!!.themeMode` (Kotlin non-null assertion), expecting Room KSP to generate `Flow<ThemeSettings>` (non-nullable). This assumption held at compile time and on JVM, but on iOS/WASM/JS the underlying SQLite driver returns `null` at runtime, causing the assertion to throw `IllegalStateException` instead of a normal NPE.
- Changing the Flow type signature in `ThemeDao` was not necessary — the DAO was always correct with `Flow<ThemeSettings?>`. The bug was in the store's consumption of the flow.

## Solution

Handle the nullable `Flow` at the consumer. The DAO declares a nullable return type, which must be handled defensively at the call site.

`shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/data/theme/BootThemeStore.kt`:

```kotlin
// Before — crashes when settings is null
val themeModeFlow: Flow<BootThemeMode> = themeDao.getThemeSettings().map { settings ->
    try {
        BootThemeMode.valueOf(settings!!.themeMode)
    } catch (e: IllegalArgumentException) {
        BootThemeMode.FOLLOW_SYSTEM
    }
}

// After — safe null handling
val themeModeFlow: Flow<BootThemeMode> = themeDao.getThemeSettings().map { settings ->
    try {
        settings?.let { BootThemeMode.valueOf(it.themeMode) } ?: BootThemeMode.FOLLOW_SYSTEM
    } catch (e: IllegalArgumentException) {
        BootThemeMode.FOLLOW_SYSTEM
    }
}
```

Tests were updated to match the actual DAO contract (`Flow<ThemeSettings?>`) and a new test was added to cover the null case:

`shared/src/commonTest/kotlin/com/yuanjingtech/boot/app/kmp/data/theme/BootThemeStoreTest.kt`:

```kotlin
class FakeThemeDao : ThemeDao {
    private val settingsFlow = MutableStateFlow<ThemeSettings?>(ThemeSettings())

    override fun getThemeSettings(): Flow<ThemeSettings?> = settingsFlow

    override suspend fun insertOrUpdate(newSettings: ThemeSettings) {
        settingsFlow.value = newSettings
    }
}

@Test
fun themeStore_nullSettings_defaultsToFollowSystem() = runTest {
    val emptyDao = object : ThemeDao {
        private val flow = MutableStateFlow<ThemeSettings?>(null)
        override fun getThemeSettings(): Flow<ThemeSettings?> = flow
        override suspend fun insertOrUpdate(settings: ThemeSettings) {}
    }
    val store = BootThemeStore(emptyDao)
    val mode = store.themeModeFlow.first()
    assertEquals(BootThemeMode.FOLLOW_SYSTEM, mode)
}
```

## Why This Works

Room KSP generates `Flow<ThemeSettings?>` for queries that can return null (e.g., `SELECT * ...` with no guaranteed row). On Android/JVM, the driver may synthesize a non-null default row, masking the nullability. On iOS/WASM/JS, the driver exposes the nullable reality of the underlying SQLite result. Using safe-call (`settings?.let { ... } ?: FOLLOW_SYSTEM`) correctly handles both the null case (no row yet) and the invalid enum string case, falling back to `FOLLOW_SYSTEM` as the intended default in either scenario.

## Prevention

- Treat all Room DAO `Flow` return types as potentially nullable when the query has no guaranteed row, even if KSP generates a non-null annotation on some platforms. Default to `Flow<T?>` and handle null at the consumer.
- When designing a single-row settings table, insert a default row at first launch (e.g., via a `RoomDatabase.Callback`) so the query never yields null. Alternatively, use a singleton pattern where `getThemeSettings()` returns `Flow<ThemeSettings>` by construction.
- Add a unit test with a `MutableStateFlow<ThemeSettings?>(null)` DAO for every store class that consumes a nullable Flow, asserting that the store's public Flow emits the correct default value.

## Related Issues

- Same branch covers the unrelated `sqlite-bundled` KMP resolution fix (`fix/sqlite-bundled-kmp-resolution`, PR [#1](https://github.com/yuanjingtech/boot-app-kmp/pull/1))
- Session history: Prior session `00b3789c` (April 22) built the theme settings infrastructure but was interrupted before the crash manifested; current session `f23df7e0` (April 24) diagnosed and fixed the `!!` assertion crash
