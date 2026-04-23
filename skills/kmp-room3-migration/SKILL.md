---
name: kmp-room3-migration
description: "将 KMP 项目迁移到 Room 3.x，使用 @ConstructedBy 和 RoomDatabaseConstructor expect/actual 模式。"
license: MIT
metadata:
  author: "lotosbin"
  version: "1.0.0"
user-invocable: true
disable-model-invocation: false
---

# KMP Room 3 迁移指南

## 参考

- https://developer.android.com/kotlin/multiplatform/room
- https://stackoverflow.com/questions/78858784/roomdatabaseconstructor-on-kotlinmultiplatform-has-no-corresponding-expected-dec

## 版本要求

- Room KMP：`2.7.0`+
- Room SQLite Wrapper：`2.8.0`+
- SQLite bundled：`2.6.2`+
- Kotlin `2.0+`（无需 `kotlin.native.disableCompilerDaemon`）

## 架构：`expect object` + 各平台 actual

Room KSP 会自动生成 `actual` 实现。只需编写 `expect` 声明，其余由 Room 处理。

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

### jsMain / wasmJsMain

JS/WASM 不支持 BundledSQLiteDriver，使用 SQLDelight 或抛出异常：

```kotlin
// jsMain / wasmJsMain
fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    throw UnsupportedOperationException(
        "Room3 BundledSQLiteDriver 在 JS/WASM 上不受支持。" +
            "请使用 SQLDelight 作为 Web 数据库方案。"
    )
}
```

## build.gradle.kts 配置

### libs.versions.toml

```toml
[versions]
room   = "2.8.4"   # 或最新 2.x 稳定版
sqlite = "2.6.2"

[libraries]
androidx-sqlite-bundled       = { module = "androidx.sqlite:sqlite-bundled", version.ref = "sqlite" }
androidx-room-runtime         = { module = "androidx.room:room-runtime", version.ref = "room" }
androidx-room-compiler        = { module = "androidx.room:room-compiler", version.ref = "room" }
# 可选：SQLite Wrapper (2.8.0+)
androidx-room-sqlite-wrapper  = { module = "androidx.room:room-sqlite-wrapper", version.ref = "room" }

[plugins]
ksp           = { id = "com.google.devtools.ksp", version.ref = "ksp" }
androidx-room = { id = "androidx.room", version.ref = "room" }
```

### shared/build.gradle.kts

```kotlin
plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

// Room schema 导出目录
room {
    schemaDirectory("$projectDir/schemas")
}

commonMain.dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.sqlite.bundled)
}

// iOS NativeSQLiteDriver 需要链接 sqlite3
kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            linkerOpts.add("-lsqlite3")
        }
    }
}
```

### KSP 配置 — iOS 必须

```kotlin
dependencies {
    // 必须为所有使用 Room 的平台配置 KSP
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}
```

> 缺少 iOS KSP 配置将导致 `actual` 无法为 iOS 目标生成。

## SO 78858784：Redeclaration 错误

**现象**：添加 `@ConstructedBy` 和 `expect object` 后，编译报错：

```
Error 1: 'actual object AppDatabaseConstructor' has no corresponding expected declaration
Error 2: Redeclaration: actual object AppDatabaseConstructor
```

**根因**：Room KSP 在 commonMain metadata 中同时生成了 `expect` 和 `actual`。如果源码中同时存在手写的 `expect object`（用于 KSP 解析注解）和手写的平台 `actual object` 实现，就会产生两个 `actual` 声明——手写的和 KSP 生成的——冲突。

**修复**：Room `2.7.0+` 配合正确的 iOS KSP 配置后，Room 会自动生成 `actual`。**不要**手写 iOS/Android/JVM 的 `actual object`，只需在 commonMain 写 `expect`。JS/WASM 仍需手写 throw 异常形式的 actual（Room 无 SQLite 驱动支持）。

## alpha03 版本差异

Room `3.0.0-alpha03` 的 KSP 可能不会自动生成 iOS actual，此时需要手动提供：

```kotlin
// iosMain
actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    actual override fun initialize(): AppDatabase {
        throw UnsupportedOperationException(
            "Room3 BundledSQLiteDriver 在 iOS 上不受支持。" +
                "请使用 SQLDelight 作为 iOS 数据库方案。"
        )
    }
}
```

## ProGuard / R8（发布构建）

```proguard
-keep class * extends androidx.room.RoomDatabase { <init>(); }
```
