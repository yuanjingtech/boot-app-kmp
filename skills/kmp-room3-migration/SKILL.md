---
name: kmp-room3-migration
description: "Migrates a KMP project to Room 3.x with @ConstructedBy and RoomDatabaseConstructor expect/actual pattern."
license: MIT
metadata:
  author: "lotosbin"
  version: "1.0.0"
user-invocable: true
disable-model-invocation: false
---

# KMP Room 3 Migration Skill

## Reference

- https://developer.android.com/kotlin/multiplatform/room
- https://stackoverflow.com/questions/78858784/roomdatabaseconstructor-on-kotlinmultiplatform-has-no-corresponding-expected-dec

## Version Requirements

- Room KMP: `2.7.0`+
- Room SQLite Wrapper: `2.8.0`+
- SQLite bundled library: `2.6.2`+
- Kotlin `2.0+` (no `kotlin.native.disableCompilerDaemon` needed)

## Architecture: expect object + platform actuals

Room KSP generates `actual` implementations for `RoomDatabaseConstructor` automatically at compile time. You only write the `expect` declaration — Room handles the rest.

## Files to Create

### commonMain: `AppDatabase.kt`

```kotlin
package com.example.data

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import org.koin.dsl.module

@Database(entities = [TodoEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}

// Room compiler generates the actual implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

// Common entry point — takes the platform builder and applies shared config.
fun getRoomDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

// Koin module wiring example
internal val appDataModule = module {
    single<AppDatabase> { getRoomDatabase(getDatabaseBuilder()) }
    single { get<AppDatabase>().todoDao() }
}
```

### androidMain: `AppDatabase.android.kt`

```kotlin
package com.example.data

import android.content.Context

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("my_room.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}
```

### jvmMain: `AppDatabase.jvm.kt`

```kotlin
package com.example.data

import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room.db")
    return Room.databaseBuilder<AppDatabase>(name = dbFile.absolutePath)
}
```

### iosMain: `AppDatabase.ios.kt`

```kotlin
package com.example.data

import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + "/my_room.db"
    return Room.databaseBuilder<AppDatabase>(name = dbFilePath)
}

private fun documentDirectory(): String {
    val url = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(url?.path)
}
```

### JS/WASM

SQLite is not bundled for JS/WASM. Use SQLDelight or throw if unsupported:

```kotlin
// jsMain / wasmJsMain
fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    throw UnsupportedOperationException(
        "Room3 BundledSQLiteDriver is not supported on JS/WASM. " +
            "Use SQLDelight for web database operations."
    )
}
```

## build.gradle.kts Configuration

### libs.versions.toml

```toml
[versions]
room  = "2.8.4"   # or latest 2.x stable
sqlite = "2.6.2"

[libraries]
androidx-sqlite-bundled = { module = "androidx.sqlite:sqlite-bundled", version.ref = "sqlite" }
androidx-room-runtime   = { module = "androidx.room:room-runtime", version.ref = "room" }
androidx-room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }
# Optional SQLite Wrapper (2.8.0+)
androidx-room-sqlite-wrapper = { module = "androidx.room:room-sqlite-wrapper", version.ref = "room" }

[plugins]
ksp          = { id = "com.google.devtools.ksp", version.ref = "ksp" }
androidx-room = { id = "androidx.room", version.ref = "room" }
```

### shared/build.gradle.kts

```kotlin
plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

// Room schema export
room {
    schemaDirectory("$projectDir/schemas")
}

commonMain.dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.sqlite.bundled)
}

// iOS linker option for NativeSQLiteDriver
kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            linkerOpts.add("-lsqlite3")
        }
    }
}
```

### KSP configuration — critical for iOS

```kotlin
dependencies {
    // Required: KSP must run for all platform targets that use Room
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}
```

> Without explicit iOS KSP configs, the `actual` for `RoomDatabaseConstructor` is not generated for iOS targets.

## SO 78858784: Redeclaration Error

**Symptom**: After adding `@ConstructedBy` and `expect object`, compilation fails with:

```
Error 1: 'actual object AppDatabaseConstructor' has no corresponding expected declaration
Error 2: Redeclaration: actual object AppDatabaseConstructor
```

**Root cause**: Room KSP generates BOTH `expect` and `actual` in commonMain metadata. If your source also contains `expect object` (for KSP to resolve the annotation) AND platform `actual object` implementations, two `actual` declarations exist — the manual one and the KSP-generated one.

**Fix**: With Room `2.7.0+` and correct KSP configs for all iOS targets, Room generates the `actual` automatically. Do NOT write manual `actual object` for iOS/Android/JVM — only write the `expect` in commonMain. JS/WASM still need a manual throw-based actual since Room has no SQLite driver there.

## Differences from alpha03

Room `3.0.0-alpha03` may not auto-generate iOS actuals. In that case, provide manual actuals:

```kotlin
// iosMain
actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    actual override fun initialize(): AppDatabase {
        throw UnsupportedOperationException(
            "Room3 BundledSQLiteDriver is not supported on iOS. " +
                "Use SQLDelight for iOS database operations."
        )
    }
}
```

## ProGuard / R8 (release builds)

```proguard
-keep class * extends androidx.room.RoomDatabase { <init>(); }
```
