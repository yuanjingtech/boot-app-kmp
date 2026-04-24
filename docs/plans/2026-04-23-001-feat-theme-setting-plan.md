---
title: 完成主题设置功能
type: feat
status: active
date: 2026-04-23
---

# 完成主题设置功能

## Overview

实现主题设置功能的完整链路：用户在设置界面选择主题偏好 → 持久化到 Room3 数据库 → 应用启动时从数据库读取偏好 → 动态应用主题。当前 `BootApplication` 中的 `BootAppTheme` 硬编码使用 `ThemeMode.FOLLOW_SYSTEM`，不会读取用户保存的主题偏好。

---

## Problem Frame

用户通过 `BootThemeSettingScreen` 选择主题后，数据会保存到数据库，但 `BootAppTheme` 在应用根节点使用硬编码默认值 `ThemeMode.FOLLOW_SYSTEM`，导致：
1. 每次打开 App 都重置为跟随系统
2. 用户选择的主题无法生效

---

## Requirements Trace

- R1. 应用启动时从 `BootThemeStore` 读取已保存的主题模式
- R2. 将读取到的主题模式传递给 `BootAppTheme`
- R3. 设置界面正常保存主题偏好到数据库
- R4. 跟随系统模式能正确响应系统深色/浅色切换

---

## Scope Boundaries

- 不涉及颜色主题定制（仅深浅主题）
- 不涉及其他设置项（仅主题设置）

---

## Context & Research

### Relevant Code and Patterns

- `BootApplication.kt` — 应用入口，`BootAppTheme` 默认使用 `BootThemeMode.FOLLOW_SYSTEM`
- `BootThemeStore.kt` — 通过 `themeModeFlow` 提供主题模式，`setThemeMode` 持久化
- `BootThemeSettingScreen.kt` — 已有完整的设置 UI，使用 `BootThemeStore`，包含 `BootThemeSettingScreenWithStore` 函数

### Architecture Patterns

- Clean Architecture + Koin DI
- 数据层：`BootThemeStore` → `ThemeDao` → `ThemeSettings`
- UI 层：`BootThemeSettingScreenWithStore`（位于 `BootThemeSettingScreen.kt`）已正确使用 `BootThemeStore`

---

## Key Technical Decisions

- **注入 `BootThemeStore` 到 `BootApplication`**：使用 Koin 注入 `BootThemeStore`，通过 `collectAsState` 收集 `themeModeFlow`，传递给 `BootAppTheme`
- **延迟初始化 `themeMode`**：首次 collect 之前使用默认值 `BootThemeMode.FOLLOW_SYSTEM`，确保 Compose 编译时类型安全

---

## Implementation Units

- [ ] U1. **[修改 BootApplication 以支持动态主题]**

**Goal:** 将 `BootThemeStore` 注入到 `BootApplication`，从 Flow 中收集主题模式并传递给 `BootAppTheme`

**Requirements:** R1, R2, R4

**Dependencies:** `bootModule` 必须已在 `BootApplication` 的 Koin 配置中注册（`bootModule` 通过 `bootDataModule` 提供 `BootThemeStore` 绑定）；这是实现的前置条件

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/BootApplication.kt`

**Approach:**
- 将 `BootApplication.kt` 中的 `pluginModule`（不存在）替换为 `bootModule`（实际注册了 `BootThemeStore` 的模块）
- 在 `BootApplication` 中使用 Koin 注入 `BootThemeStore`（通过 `koinInject<BootThemeStore>()`）
- 通过 `collectAsState` 收集 `themeModeFlow` 的值
- 将收集到的主题模式传递给 `BootAppTheme`
- 首次 collect 前使用 `BootThemeMode.FOLLOW_SYSTEM` 作为初始值

**Patterns to follow:**
- `BootThemeSettingScreen.kt` 中的 `BootThemeSettingScreenWithStore` 函数已有的 Koin 注入和 `collectAsState` 模式

**Test scenarios:**
- Happy path: 应用启动后读取到已保存的主题模式并正确应用
- Edge case: 数据库无记录时默认使用 `FOLLOW_SYSTEM`
- Integration: 用户在设置界面切换主题后，应用立即响应（需重启或重建 Activity 时验证）

---

## System-Wide Impact

- **Interaction graph:** `BootApplication` 作为顶层 Composable，其下所有子组件都会受到主题变化的影响
- **Error propagation:** 若 `BootThemeStore` 初始化失败，应回退到默认主题模式

---

## Risks & Dependencies

| Risk | Mitigation |
|------|------------|
| `BootApplication.kt` 中引用了不存在的 `pluginModule` | 修改为 `bootModule`（现有代码中的实际模块），或使用目标应用指定的模块 |
| Koin 依赖在 Composition 早期不可用 | 使用 `@OptIn(KoinExperimentalAPI::class)` 并确保 Koin 在 `BootApplication` 调用前已初始化 |
| Flow collect 导致重组性能问题 | `collectAsState` 已是 Compose 推荐模式，主题变更频率低，性能影响可忽略 |

---

## Sources & References

- BootApplication.kt — 应用根入口
- BootThemeStore.kt — 主题数据管理
- BootThemeSettingScreen.kt — 已有的设置界面（含 `BootThemeSettingScreenWithStore` 函数）