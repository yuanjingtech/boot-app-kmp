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
- https://developer.android.com/blog/posts/modernizing-the-room
- https://stackoverflow.com/questions/78858784/roomdatabaseconstructor-on-kotlinmultiplatform-has-no-corresponding-expected-dec

## Version Requirements

- Room KMP: `2.7.0`+（完全 KMP 支持，含 JS/WASM）
- Room SQLite Wrapper: `2.8.0`+
- SQLite bundled: `2.6.2`+
- Kotlin `2.0+`（无需 `kotlin.native.disableCompilerDaemon`）

## 平台支持矩阵

| 平台 | 数据库驱动 | KSP 配置 | 说明 |
|------|-----------|----------|------|
| Android | BundledSQLiteDriver | kspAndroid | |
| JVM | BundledSQLiteDriver | 自动 | |
| iOS | NativeSQLiteDriver | kspIosArm64 等 | 需要 -lsqlite3 linker |
| JS | WebWorkerSQLiteDriver + OPFS | kspJs | 用 sqlite-web |
| WASM | WebWorkerSQLiteDriver + OPFS | kspWasmJs | 用 sqlite-web |

## 架构：`expect object` + 各平台 actual

Room KSP 自动生成 `actual` 实现。只需在 commonMain 编写 `expect` 声明。

## 文件结构

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

// Room 编译器自动生成 actual 实现。
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

// 通用入口 — 接收平台 builder，应用共享配置。
fun getRoomDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

// Koin 模块示例
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

### jsMain / wasmJsMain：使用 `sqlite-web`（Room 3.0 新增）

JS/WASM 通过 `WebWorkerSQLiteDriver` + OPFS（Origin Private File System）支持持久化存储。

#### 1. 创建 `sqlite-wasm-worker` 模块

```
sqlite-wasm-worker/
├── build.gradle.kts
└── worker/
    ├── package.json        # npm dependencies
    └── worker.js           # WebWorker 实现
```

**`sqlite-wasm-worker/build.gradle.kts`**

```kotlin
@file:OptIn(ExperimentalWasmDsl::class)

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    js { browser { useEsModules() } }
    wasmJs { browser { useEsModules() } }

    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.sqlite.web)
            implementation(npm("sqlite-wasm-worker", layout.projectDirectory.dir("worker")))
        }
        wasmJsMain.dependencies {
            implementation(libs.kotlinx.browser)
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

**`sqlite-wasm-worker/worker/worker.js`** — 实现 `WebWorkerSQLiteDriver` 协议，参考 AndroidX 源码：
https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:sqlite/sqlite-web-worker-test/web-worker/worker.js

#### 2. JS/WASM actual 实现

```kotlin
// jsMain / wasmJsMain
fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    return Room.databaseBuilder<AppDatabase>("my_room.db")
        .setDriver(WebWorkerSQLiteDriver(createWorker()))
        .setCoroutineContext(Dispatchers.IO)
}

fun createWorker(): Worker = Worker(
    js("""new URL("sqlite-wasm-worker/worker.js", import.meta.url)""")
)
```

> Worker 由 `sqlite-wasm-worker` 模块的 NPM 依赖提供。参考 demo: https://github.com/danysantiago/room-web-demo

## build.gradle.kts 配置

### libs.versions.toml

```toml
[versions]
room   = "2.8.4"
sqlite = "2.6.2"

[libraries]
androidx-sqlite-bundled = { module = "androidx.sqlite:sqlite-bundled", version.ref = "sqlite" }
androidx-sqlite-web     = { module = "androidx.sqlite:sqlite-web", version.ref = "sqlite" }
androidx-room-runtime  = { module = "androidx.room3:room3-runtime", version.ref = "room" }
androidx-room-compiler  = { module = "androidx.room3:room3-compiler", version.ref = "room" }
```

### shared/build.gradle.kts

```kotlin
plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.room3)
}

room3 {
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
    // 必须为所有使用 Room 的平台配置 KSP
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspJs", libs.androidx.room.compiler)
    add("kspWasmJs", libs.androidx.room.compiler)
}
```

### composeApp/build.gradle.kts（含 JS/WASM 目标）

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room3)
}

kotlin {
    js { browser(); binaries.executable() }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser(); binaries.executable() }
}

commonMain.dependencies {
    implementation(libs.androidx.room.runtime)
}
webMain.dependencies {
    implementation(libs.androidx.sqlite.web)
    implementation(project(":sqlite-wasm-worker"))
}

dependencies {
    add("kspJs", libs.androidx.room.compiler)
    add("kspWasmJs", libs.androidx.room.compiler)
}
```

## SO 78858784：Redeclaration 错误

**现象**：`@ConstructedBy` + `expect object` 编译报错：

```
Error 1: 'actual object AppDatabaseConstructor' has no corresponding expected declaration
Error 2: Redeclaration: actual object AppDatabaseConstructor
```

**根因**：Room KSP 在 commonMain metadata 中同时生成 `expect` 和 `actual`。若源码中同时存在手写 `expect` 和平台 `actual`，会产生两个 `actual` 冲突。

**修复**：`2.7.0+` 配合正确的 iOS/JS/WASM KSP 配置后，Room 自动生成 `actual`。不要手写 Android/JVM/iOS 的 `actual object`，只需写 `expect`。JS/WASM 仍需手写 actual（Room 的 WebWorkerSQLiteDriver 需要平台特定初始化）。

## alpha03 差异

`3.0.0-alpha03` 的 KSP 可能不会自动生成 iOS/JS actual，需手动提供：

```kotlin
// iosMain / jsMain / wasmJsMain
actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    actual override fun initialize(): AppDatabase {
        throw UnsupportedOperationException(
            "Room3 BundledSQLiteDriver 在此平台不受支持。" +
                "请使用 SQLDelight 作为数据库方案。"
        )
    }
}
```

## ProGuard / R8

```proguard
-keep class * extends androidx.room.RoomDatabase { <init>(); }
```
