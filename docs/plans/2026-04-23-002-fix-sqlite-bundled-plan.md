---
title: fix: KMP sqlite-bundled 依赖跨平台解析失败
type: fix
status: active
date: 2026-04-23
---

# fix: KMP sqlite-bundled 依赖跨平台解析失败

## Overview

将 `androidx.sqlite:sqlite-bundled` 依赖从 `commonMain` 移至平台特定 source set，并用 `expect`/`actual` 模式重构 `createBootDatabase()` 工厂函数，使 JS/WASM 平台可以编译通过，同时保持 JVM/Android 的 SQLite 支持。

---

## Problem Frame

`shared/build.gradle.kts` 的 `commonMain.dependencies` 中直接声明了 `implementation(libs.sqlite.bundled)`。`androidx.sqlite:sqlite-bundled` 仅支持 JVM 和 Android 平台，Gradle 在解析 JS/WASM 目标的 `commonMain` 依赖时失败：

```
Couldn't resolve dependency 'androidx.sqlite:sqlite-bundled' in 'commonMain' for all target platforms.
Unresolved platforms: [js, wasmJs]
```

---

## Requirements Trace

- R1. `shared` 模块在所有平台（Android、iOS、JVM、JS、WASM）上均可编译通过
- R2. JVM 和 Android 平台保留 SQLite Bundled 驱动支持
- R3. JS/WASM 平台使用 Web SQLite 驱动（`sqlite-web`）或不依赖本地驱动

---

## Scope Boundaries

- 不改变 Room3 数据库 schema 或数据层逻辑
- 不涉及 iOS 平台的 SQLite 驱动配置（iOS 使用 native driver，已有 sqldelight 覆盖）

---

## Context & Research

### Relevant Code and Patterns

- `shared/build.gradle.kts` — `commonMain.dependencies` 第 69 行直接引用 `libs.sqlite.bundled`
- `gradle/libs.versions.toml` — 定义了 `sqlite-bundled` 和 `sqlite-web` 两个库版本
- `BootDatabaseFactory.kt` — 在 `commonMain` 中使用 `BundledSQLiteDriver`
- `Platform.kt` — 现有 `expect`/`actual` 模式（`expect fun getPlatform()` + 各平台 `actual` 实现）

### Pattern to Follow

`Platform.kt` 的 `expect`/`actual` 模式（iOS 使用 `nativeMain` 而非 `iosMain`）：
- `commonMain`: `expect fun createBootDatabase(): BootDatabase`
- `jvmMain`: `actual fun createBootDatabase()` — 使用 `BundledSQLiteDriver`
- `androidMain`: `actual fun createBootDatabase()` — 使用 `BundledSQLiteDriver`
- `jsMain`: `actual fun createBootDatabase()` — 使用 `WebSQLiteDriver` 或 `UnsupportedOperationException`
- `wasmJsMain`: `actual fun createBootDatabase()` — 同上
- `nativeMain`（覆盖 iOS）：`actual fun createBootDatabase()` — 抛出 `UnsupportedOperationException`（iOS 数据库功能由 SQLDelight 覆盖）

---

## Key Technical Decisions

- **使用 `expect`/`actual` 模式**：`createBootDatabase()` 从 `commonMain` 迁移为 expect 函数，各平台提供 actual 实现
- **`sqlite-bundled` 移至平台 source set**：`commonMain` 不再依赖 `sqlite-bundled`，改为仅在 `jvmMain`/`androidMain` 中声明
- **JS/WASM 使用 `sqlite-web`**：`androidx.sqlite:sqlite-web` 支持 Web 平台，替换 `sqlite-bundled` 在 `jsMain`/`wasmJsMain` 中的使用

---

## Implementation Units

- [ ] U1. **[重构 createBootDatabase 为 expect/actual]**

**Goal:** 将 `createBootDatabase()` 从 `commonMain` 具体实现改为 expect 函数，各平台提供 actual 实现

**Requirements:** R1, R2, R3

**Dependencies:** None

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/data/room3/BootDatabaseFactory.kt`
- Create: `shared/src/jvmMain/kotlin/com/yuanjingtech/boot/app/kmp/data/room3/BootDatabaseFactory.jvm.kt`
- Create: `shared/src/androidMain/kotlin/com/yuanjingtech/boot/app/kmp/data/room3/BootDatabaseFactory.android.kt`
- Create: `shared/src/jsMain/kotlin/com/yuanjingtech/boot/app/kmp/data/room3/BootDatabaseFactory.js.kt`
- Create: `shared/src/wasmJsMain/kotlin/com/yuanjingtech/boot/app/kmp/data/room3/BootDatabaseFactory.wasmJs.kt`
- Create: `shared/src/nativeMain/kotlin/com/yuanjingtech/boot/app/kmp/data/room3/BootDatabaseFactory.native.kt`（覆盖 iOS）

**Approach:**
- `commonMain`：`createBootDatabase()` 改为 `expect fun createBootDatabase(): BootDatabase`（删除 `BundledSQLiteDriver` import）
- `jvmMain`/`androidMain`：保留原有 `BundledSQLiteDriver` 实现
- `jsMain`/`wasmJsMain`：使用 `WebSQLiteDriver`（来自 `sqlite-web`，已确认支持 JS/WASM）

**Patterns to follow:**
- `Platform.kt` 中的 `expect`/`actual` 模式

**Test scenarios:**
- Happy path: `shared` 模块在 JVM/Android/JS/WASM 平台均可解析依赖并编译
- Error path: 若 JS/WASM actual 实现缺失，编译报错（预期行为）

---

- [ ] U2. **[将 sqlite-bundled 移至平台 source set]**

**Goal:** 从 `commonMain.dependencies` 移除 `sqlite-bundled`，改为在 `jvmMain`/`androidMain` 中声明

**Requirements:** R1, R2

**Dependencies:** U1（完成后移除 commonMain 中的驱动引用）

**Files:**
- Modify: `shared/build.gradle.kts`

**Approach:**
- 从 `commonMain.dependencies` 删除 `implementation(libs.sqlite.bundled)`
- 在 `jvmMain.dependencies` 和 `androidMain.dependencies` 中添加 `implementation(libs.sqlite.bundled)`
- 在 `jsMain.dependencies` 和 `wasmJsMain.dependencies` 中添加 `implementation(libs.sqlite.web)` 以支持 Web 驱动

**Patterns to follow:**
- `sqldelight/build.gradle.kts` 中按平台 source set 配置依赖的模式

**Test scenarios:**
- Happy path: `./gradlew :shared:compileKotlinJvm` 通过
- Happy path: `./gradlew :shared:compileKotlinJs` 通过
- Happy path: `./gradlew :shared:compileKotlinWasmJs` 通过

---

## System-Wide Impact

- **Interaction graph:** `createBootDatabase()` 被 `BootDataModule.kt` 中的 `createBootDatabase()` 调用，迁移后 DI 绑定无需修改
- **API surface parity:** 函数签名不变，下游无需感知平台差异

---

## Risks & Dependencies

| Risk | Mitigation |
|------|------------|
| `sqlite-web` 在 JS/WASM 实际运行行为未经测试 | Web 驱动在编译时可用即可；运行时行为超出本次修复范围 |
| 平台 source set 目录结构不存在 | 确认 `jvmMain`、`androidMain`、`jsMain`、`wasmJsMain`、`nativeMain` 目录均已存在 |

---

## Sources & References

- `shared/build.gradle.kts` — 依赖配置
- `gradle/libs.versions.toml` — SQLite 库版本定义
- `BootDatabaseFactory.kt` — 现有工厂实现
- `Platform.kt` — expect/actual 模式参考