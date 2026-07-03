---
title: "backdrop 0.0.1-alpha02 triggers IrLinkageError on iOS KMP — org.jetbrains.skia.ColorMatrix.<init> not found"
date: "2026-07-03"
category: docs/solutions/runtime-errors/
module: boot-app-kmp/ui
problem_type: link_error
component: liquidglass
symptoms:
  - "kotlin.internal.IrLinkageError: Constructor 'ColorMatrix.<init>' can not be called: No constructor found for symbol 'org.jetbrains.skia/ColorMatrix.<init>'"
  - "iOS KMP link failure (compileKotlinIosArm64 / compileKotlinIosSimulatorArm64 / iosArm64MainBinaries)"
  - "Does NOT reproduce on Android, JVM, WASM, JS"
  - "Triggers when LiquidGlass backdrop on iOS invokes vibrancy()/opacity()/colorControls()/exposureAdjustment()"
root_cause: upstream_library_bug
resolution_type: temporary_workaround
severity: high
status: work_in_progress
workaround_in_place_since: "2026-07-03"
related_components:
  - ui/src/iosArm64Main/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/LiquidGlassBackdrop.ios.kt
  - ui/build.gradle.kts
  - gradle/libs.versions.toml
tags:
  - kmp
  - ios
  - link-error
  - skia
  - skiko
  - colormatrix
  - irlinkageerror
  - backdrop
  - liquidglass
---

# backdrop 0.0.1-alpha02 triggers IrLinkageError on iOS KMP — `org.jetbrains.skia.ColorMatrix.<init>` not found

## Problem

`io.github.kashif-mehmood-km:backdrop:0.0.1-alpha02` triggers a Kotlin/Native link error when its effects are invoked from iOS KMP sources:

```
kotlin.internal.IrLinkageError: Constructor 'ColorMatrix.<init>' can not be called:
  No constructor found for symbol 'org.jetbrains.skia/ColorMatrix.<init>'
```

The error originates in `backdrop`'s `skiaMain` color-filter implementation. It is **not** reproducible on Android, JVM, JS, or WASM because those platforms have their own (different) `ColorMatrix` actuals.

## Symptoms

- K/N link phase fails (`./gradlew :ui:iosArm64MainBinaries` or `compileKotlinIosArm64`).
- Same klib compiles fine for non-iOS targets.
- Stack trace points to `kashif_e.backdrop.effects.ColorFilter` (vibrancy/opacity/colorControls/exposureAdjustment).
- Triggered as soon as iOS code references any backdrop effect that internally builds a `ColorMatrix`.

## Root Cause

`backdrop 0.0.1-alpha02` / `skiaMain/com/kashif_e/backdrop/effects/ColorFilter.kt` calls:

```kotlin
val cm = ColorMatrix(*matrix)        // vararg → FloatArray underlying type
colorFilter(ColorFilter.makeMatrix(cm))
```

`org.jetbrains.skia.ColorMatrix` in skiko 0.144.x is a `@JvmInline value class` whose only constructor is `<init>(mat: FloatArray)`. The Kotlin signature **should** match the `*matrix` spread call, but at the iOS K/N cinterop layer skiko does **not** expose a `_nMakeColorMatrix` (or equivalent) bridge for this value class. The K/N linker therefore cannot find a concrete symbol for `ColorMatrix.<init>` when backdrop's klib is linked into an iOS target → `IrLinkageError`.

This is a **bug in the upstream `backdrop` library** (and/or its misuse of the skia ColorMatrix API on iOS K/N). The error has nothing to do with `boot-app-kmp` calling `ColorMatrix` directly — the project never references `org.jetbrains.skia.ColorMatrix` in its own code.

## Effects that hit this path

- `vibrancy()`
- `opacity(alpha)`
- `colorControls(brightness, contrast, saturation)`
- `exposureAdjustment(ev)`

## Effects that are safe (do NOT go through ColorMatrix)

- `blur(radius)` → uses `org.jetbrains.skia.ImageFilter.makeBlur` (real cinterop bridge exists)
- `lens(...)` → uses `org.jetbrains.skia.RuntimeEffect.makeForShader` + SkSL (SkSL bridge exists)

## Workaround Applied (2026-07-03)

In `ui/src/iosArm64Main/.../LiquidGlassBackdrop.ios.kt`:

1. Removed the `import com.kashif_e.backdrop.effects.vibrancy` line.
2. Removed the `if (config.vibrancy) vibrancy()` call from both `liquidGlassBackdrop` and `liquidGlassBackdropCanvas` effects blocks.
3. Added a multi-line comment explaining why vibrancy is intentionally skipped on iOS.

`blur()` and `lens()` continue to be called. Visual difference on iOS: the backdrop will not have the iOS-style saturation boost from vibrancy, but the surface overlay (`surfaceColor`/`surfaceAlpha`) drawn on top of the backdrop still provides glass readability. The effect is small and acceptable.

### Verification

```
./gradlew :ui:compileKotlinIosSimulatorArm64  # BUILD SUCCESSFUL
./gradlew :ui:compileKotlinIosArm64            # BUILD SUCCESSFUL
./gradlew :ui:iosArm64MainBinaries             # BUILD SUCCESSFUL (K/N link passes)
```

The `IrLinkageError` no longer appears.

## What Did NOT Work

- Bumping skiko version (skiko is transitively pinned by Compose Multiplatform 1.11.0-beta03 → 0.144.x). Forcing a different skiko risks breaking Compose Multiplatform itself.
- Adding a custom `actual fun` for `vibrancy()` — the `expect` is declared in backdrop's `commonMain` and resolution goes through the same broken klib; we cannot override it from our code.

## Follow-up — Long-term Solution

Tracked in upstream `kashif-mehmood-km/backdrop` (the `backdrop` library's repo). When the library ships a fix, the workaround in `LiquidGlassBackdrop.ios.kt` should be reverted. Possible upstream fixes:

1. Replace `ColorMatrix(*matrix)` with `ColorMatrix(matrix)` (explicit FloatArray, no vararg spread) — verifies the cinterop layer's init bridge is correctly resolved by the K/N compiler.
2. Provide a no-op or `RuntimeShader`-based implementation of `vibrancy` for iOS KMP that avoids `ColorMatrix` entirely.
3. Drop `ColorMatrix` from the iOS path entirely and use the existing `colorControls` SkSL shader approach.

When the upgrade becomes available:

- Revert the iOS workaround (re-add `vibrancy()` import and call).
- Re-run `./gradlew :ui:iosArm64MainBinaries` to confirm no regression.
- Re-run UI tests on iOS to confirm vibrancy looks correct.

## Notes

- The Android path is unaffected; `vibrancy()` continues to work on Android/JVM/WASM/JS.
- The dependency declaration in `ui/build.gradle.kts` for `iosArm64Main` and `iosSimulatorArm64Main` (the `implementation(libs.backdrop)` lines) **must stay** — the library is still needed for `blur()` and `lens()`. Only the `vibrancy()` call site is disabled.
