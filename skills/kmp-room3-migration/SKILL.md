---
name: kmp-room3-migration
description: "Migrates a KMP project to Room 3.x with @ConstructedBy and RoomDatabaseConstructor expect/actual pattern."
license: MIT
metadata:
  author: "lotosbin"
  version: "1.1.0"
user-invocable: true
disable-model-invocation: false
---

# KMP Room 3 Migration Skill

## 参考文档

- https://developer.android.com/kotlin/multiplatform/room
- https://developer.android.com/blog/posts/modernizing-the-room
- https://medium.com/@hgarcia.alberto/implementing-room-database-in-kotlin-multiplatform-ksp2-koin-aac564da2d4f

## 版本要求

| 依赖 | 版本 |
|------|------|
| Room KMP | `2.7.0+` (完全 KMP 支持，含 JS/WASM) |
| Room SQLite Wrapper | `2.8.0+` |
| SQLite bundled | `2.6.2+` |
| KSP | `2.0.0+` (推荐使用 KSP2) |
| Kotlin | `2.0+` |

## KSP2 配置 (推荐)

在 `gradle.properties` 中启用 KSP2：

```properties
ksp.useKSP2=true
```

**KSP2 优势**: 单一 `ksp(libs.room.compiler)` 声明即可处理所有平台，无需逐个配置 `kspJvm`、`kspJs` 等。

```kotlin
// build.gradle.kts
plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

dependencies {
    ksp(libs.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
```

## 平台支持矩阵

| 平台 | 数据库驱动 | KSP 配置 (KSP1) | KSP2 |
|------|-----------|-----------------|------|
| Android | BundledSQLiteDriver | kspAndroid | 统一 ksp |
| JVM | BundledSQLiteDriver | kspJvm | 统一 ksp |
| iOS | NativeSQLiteDriver | kspIosArm64 等 | 统一 ksp |
| JS | WebWorkerSQLiteDriver + OPFS | kspJs | 统一 ksp |
| WASM | WebWorkerSQLiteDriver + OPFS | kspWasmJs | 统一 ksp |

## 架构：`expect object` + 各平台 actual

Room KSP 自动生成 `actual` 实现。只需在 commonMain 编写 `expect` 声明。

### commonMain: `AppDatabase.kt`

```kotlin
package com.example.data

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = true,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
```

### commonMain: `AppDatabaseConstructor.kt`

```kotlin
package com.example.data

import androidx.room3.RoomDatabaseConstructor
import kotlin.Suppress

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
```

**重要**: `@Suppress("KotlinNoActualForExpect")` 在 Kotlin 2.3+ 可能必需，且有助于 IDE 不报警告。

### commonMain: `GetDatabaseBuilder.kt`

```kotlin
package com.example.data

import androidx.room3.RoomDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

// Android 需要 Context 的重载
expect fun getDatabaseBuilder(context: Any): RoomDatabase.Builder<AppDatabase>
```

### commonMain: `GetRoomDatabase.kt`

```kotlin
package com.example.data

import androidx.room3.RoomDatabase

/**
 * 跨平台统一入口 — 接收平台 builder，应用共享配置。
 */
fun getRoomDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
```

## 平台实现

### androidMain: `DatabaseBuilder.android.kt`

```kotlin
package com.example.data

import android.content.Context
import androidx.room3.Room

actual fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("app_database.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    error("Android 需要 Context，请使用 getDatabaseBuilder(context)")
}
```

### jvmMain: `DatabaseBuilder.jvm.kt`

```kotlin
package com.example.data

import androidx.room3.Room
import androidx.room3.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "app_database.db")
    return Room.databaseBuilder<AppDatabase>(name = dbFile.absolutePath)
}
```

### iosMain: `DatabaseBuilder.ios.kt`

```kotlin
package com.example.data

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.native.NativeSQLiteDriver
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + "/app_database.db"
    return Room.databaseBuilder<AppDatabase>(name = dbFilePath)
        .setDriver(NativeSQLiteDriver())
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

### jsMain / wasmJsMain: `DatabaseBuilder.js.kt` / `.wasmJs.kt`

JS/WASM 通过 `WebWorkerSQLiteDriver` + OPFS（Origin Private File System）支持持久化存储。

#### 1. 创建 `sqlite-wasm-worker` 模块

```
sqlite-wasm-worker/
├── build.gradle.kts
└── worker/
    ├── package.json
    └── worker.js
```

**`sqlite-wasm-worker/build.gradle.kts`**

```kotlin
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    js { browser { useEsModules() } }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser { useEsModules() } }

    sourceSets {
        commonMain.dependencies {
            api(libs.sqlite.web)
            implementation(npm("sqlite-wasm-worker", file("worker")))
        }
    }
}
```

**`sqlite-wasm-worker/worker/package.json`**

```json
{
  "name": "sqlite-wasm-worker",
  "version": "1.0.0",
  "type": "module",
  "dependencies": {
    "@sqlite.org/sqlite-wasm": "^3.47.0"
  }
}
```

**`sqlite-wasm-worker/worker/worker.js`**

实现 `WebWorkerSQLiteDriver` 协议，参考 AndroidX 源码：
https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:sqlite/sqlite-web-worker-test/web-worker/worker.js

#### 2. JS/WASM actual 实现

```kotlin
// jsMain / wasmJsMain
package com.example.data

import androidx.sqlite.driver.worker.WebWorkerSQLiteDriver

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    return Room.databaseBuilder<AppDatabase>("app_database.db")
        .setDriver(WebWorkerSQLiteDriver(createWorker()))
}

@Suppress("ACTUAL_FUNCTION_WITH_DEFAULT_ARGUMENTS")
external fun createWorker(): Worker
```

## build.gradle.kts 配置

### KSP1 配置 (传统方式)

需要为每个平台配置 KSP：

```kotlin
plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

room {
    schemaDirectory("$projectDir/schemas")
}

// iOS NativeSQLiteDriver 需要链接 sqlite3
kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            linkerOpts.add("-lsqlite3")
        }
    }
}

dependencies {
    // KSP1 需要逐个配置
    add("kspAndroid", libs.room.compiler)
    add("kspJvm", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspJs", libs.room.compiler)
    add("kspWasmJs", libs.room.compiler)
}
```

### KSP2 配置 (推荐方式)

```kotlin
plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

room {
    schemaDirectory("$projectDir/schemas")
}

// iOS linker 配置
kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            linkerOpts.add("-lsqlite3")
        }
    }
}

dependencies {
    ksp(libs.room.compiler)  // KSP2 统一处理
}
```

### shared 模块依赖

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.room.runtime)
        }
        androidMain.dependencies {
            implementation(libs.sqlite.bundled)
        }
        jvmMain.dependencies {
            implementation(libs.sqlite.bundled)
        }
        jsMain.dependencies {
            implementation(libs.sqlite.web)
            implementation(npm("sqlite-wasm-worker", file("../sqlite-wasm-worker/worker")))
        }
        wasmJsMain.dependencies {
            implementation(libs.sqlite.web)
            implementation(npm("sqlite-wasm-worker", file("../sqlite-wasm-worker/worker")))
        }
    }
}
```

## Koin 集成

### commonMain: `AppModule.kt`

```kotlin
package com.example.data

import org.koin.dsl.module

internal val appModule = module {
    single { getRoomDatabase(getDatabaseBuilder()) }
    single { get<Database>().todoDao() }
}
```

### Android 需要 Context 注入

Android 平台需要通过 Koin 注入 Application Context：

```kotlin
// 在 Android 应用层配置
internal val androidModule = module {
    single<Context>(createdAtStart = true) { get<Application>() }
}
```

## SO 78858784：错误 "no corresponding expected declaration"

**现象**: `@ConstructedBy` + `expect object` 编译报错：

```
Error 1: 'actual object AppDatabaseConstructor' has no corresponding expected declaration
Error 2: Redeclaration: actual object AppDatabaseConstructor
```

**根因**: Room KSP 在 commonMain metadata 中同时生成 `expect` 和 `actual`。若源码中同时存在手写 `expect` 和平台 `actual`，会产生两个 `actual` 冲突。

**修复**: 使用 KSP2（`ksp.useKSP2=true`）或确保正确的 KSP 配置：
- KSP2: 单一 `ksp(libs.room.compiler)` 自动处理所有平台
- KSP1: 只需 `kspCommonMainMetadata` + 平台 KSP (`kspJs`/`kspWasmJs`/`kspIos*`)，不需要 JVM/Android 的 KSP

**重要**: 不要同时使用 `kspCommonMainMetadata` 和平台 KSP (`kspJvm`/`kspAndroid`)，这会导致重复生成。

## alpha03 差异

`3.0.0-alpha03` 的 KSP 可能不会自动生成 iOS/JS/WASM actual，需手动提供：

```kotlin
// iosMain / jsMain / wasmJsMain
actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    actual override fun initialize(): AppDatabase {
        throw UnsupportedOperationException(
            "Room3 BundledSQLiteDriver 在此平台不受支持。" +
                "请使用 WebWorkerSQLiteDriver 或 NativeSQLiteDriver。"
        )
    }
}
```

## ProGuard / R8

```proguard
-keep class * extends androidx.room.RoomDatabase { <init>(); }
```