---
title: "Boot 组件集成 Shimmer Skeleton 骨架屏"
type: feat
status: completed
date: 2026-04-30
origin: docs/plans/2026-04-30-001-feat-shimmer-skeleton-plan.md
---

# Boot 组件集成 Shimmer Skeleton 骨架屏

## Summary

为 `ui` 组件库中的 Boot* 组件（BootCard、BootText、BootImage、BootSurface）添加 `isLoading` 参数，使用 `compose-shimmer-skeleton` 库的 `Modifier.shimmerEffect()` 实现骨架屏加载状态。当 `isLoading = true` 时，组件显示 shimmer 动画占位符，替代正常内容。骨架屏颜色方案与组件风格一致（LiquidGlass 用暗色系，Material3 用浅色系）。

---

## Problem Frame

当前 UI 组件库只有 `BootProgressIndicator`（进度指示器），缺少骨架屏（Skeleton Loading）组件。骨架屏在内容加载场景下比进度指示器提供更好的视觉体验，尤其是在列表、卡片等场景。

**关键变化**：用户要求"骨架默认集成到组件中"，意味着骨架屏不是独立组件，而是作为现有 Boot* 组件的内置特性，通过 `isLoading` 参数控制。

---

## Requirements

- R1. 为 BootCard、BootText、BootImage、BootSurface 添加 `isLoading: Boolean = false` 参数
- R2. 骨架屏颜色方案与 UI 风格一致（LiquidGlass 用暗色系，Material3 用浅色系）
- R3. `isLoading = false` 时组件行为不变，不影响现有使用
- R4. 与主题系统集成，支持亮色/暗色模式
- R5. 提供 `@Preview` 预览，文档说明使用方式

---

## Scope Boundaries

- 仅封装 `compose-shimmer-skeleton` 的 `Modifier.shimmerEffect()`，不自研动画实现
- 不修改现有组件的非 loading 行为
- 不添加独立的 `BootShimmerSkeleton` 组件（独立组件作为后续迭代）
- iOS/Web 平台支持由 `compose-shimmer-skeleton` 库保证

---

## Context & Research

### Relevant Code and Patterns

- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootCard.kt` — BootCard 公共 API（3 层架构示例）
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootText.kt` — BootText 公共 API
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootImage.kt` — BootImage 公共 API
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootSurface.kt` — BootSurface 公共 API
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/LiquidGlassCard.kt` — LiquidGlass 卡片实现
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/LiquidGlassText.kt` — LiquidGlass 文本实现
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/LiquidGlassSurface.kt` — LiquidGlass 表面实现
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/LiquidGlassModifiers.kt` — LiquidGlass 特效 modifier（`.liquidGlassSurface()` 等）
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/effects/LgEffects.kt` — LiquidGlass 特效配置
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/material3/Material3Cards.kt` — Material3 卡片实现
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/material3/Material3Text.kt` — Material3 文本实现
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/material3/Material3Surfaces.kt` — Material3 表面实现
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/AsyncImageView.kt` — Coil3 异步图片（含 placeholder）

### Library Integration

- **Maven artifact:** `dev.seyfarth:compose-shimmer-skeleton:1.0.1`
- **API:** `Modifier.shimmerEffect(visible, shape, baseColor, highlightColor, animationSpec, shimmerAngle, content)` — 用于包裹内容显示骨架屏
- **Alternative API:** `shimmerBackground(visible, shape, baseColor, highlightColor, ...)` — 用于将现有背景变为骨架样式
- **支持的平台:** Android, iOS, JVM, Web (Wasm & JS)
- **License:** MIT

### External Reference

- klibs.io: https://klibs.io/project/timoseyfarth/compose-shimmer-skeleton

---

## Key Technical Decisions

- **方案: 自研 Shimmer Modifier（替代外部库）**：`compose-shimmer-skeleton` 库（Maven: `dev.seyfarth:compose-shimmer-skeleton:1.0.1`）经 Maven Central 验证，**仅有 `javadoc.jar`，无任何平台二进制文件**（仅有 `kotlin-metadata` 变体，无 `android.jar`、无 `iosArm64`/`wasmJs`/`js` 等平台工件）。改为使用 Compose 内置 API（`drawWithContent` + `Animatable`/`infiniteTransition`）实现 shimmer 效果，代码量约 100 行，完全可控。
- **Modifier 封装策略**：在 `BootShimmer.kt` 中创建 `Modifier.shimmerEffect()` 扩展函数，提供 style-aware 颜色配置。LiquidGlass 风格使用透明背景上的浅色骨架，Material3 风格使用浅灰色骨架。
- **组件级别集成**：每个 Boot* 组件接收 `isLoading` 参数，当 `isLoading = true` 时，组件内部替换内容为骨架占位符，保留外层样式（圆角、边框、阴影等）。
- **颜色方案与风格一致**：
  - LiquidGlass：骨架在毛玻璃表面上方，显示浅灰色 shimmer（`0xFFE0E0E0` base, `0xFFF5F5F5` highlight）
  - Material3：骨架使用浅灰色（`0xFFE0E0E0` base, `0xFFF5F5F5` highlight），与 Material3 Design 规范一致
- **向后兼容**：`isLoading` 默认为 `false`，现有代码不受影响

---

## Open Questions

### Resolved During Planning

- Q: `compose-shimmer-skeleton` 库是否可用？
- A: **不可用**。Maven Central 验证显示该库仅发布 `javadoc.jar` 和 `kotlin-metadata`，无平台二进制文件。改用自研 `Modifier.shimmerEffect()` 实现。

- Q: 独立 Shimmer 组件还做吗？
- A: 暂不做。先完成组件内置骨架屏，后续迭代再考虑独立 `BootShimmerSkeleton` 组件

- Q: LiquidGlass 和 Material3 骨架屏颜色如何区分？
- A: 通过 `LocalUiStyle.current` 在 Boot 层分发到不同实现，每个实现在自己的颜色空间中配置 shimmer 颜色

- Q: BootCard 和 BootCards（多个卡片）都要添加 `isLoading` 吗？
- A: 先为主流组件添加（BootCard、BootText、BootImage、BootSurface），其他组件（BootElevatedCard、BootOutlinedCard 等）通过组合方式复用 BootCard 的 shimmer 实现

### Deferred to Implementation

- Q: BootImage 的 shimmer 是显示图片占位符还是直接覆盖整个图片区域？
- A: 显示图片尺寸的骨架占位符，不显示占位图/错误图。后续可扩展 placeholder 参数与 shimmer 的组合

- Q: BootText 在 loading 时显示几行占位符？
- A: 单行占位符（lineHeight 匹配原文本高度），与 Material3 Shimmer 设计规范一致

---

## Implementation Units

- U1. **创建自研 Shimmer Modifier 实现**

**Goal:** 使用 Compose 内置 API 实现 shimmer 效果，不依赖外部库

**Dependencies:** None

**Files:**
- Create: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootShimmer.kt`

**Approach:**
- 使用 `GraphicsLayerScope.drawWithContent()` + `Animatable` / `infiniteTransition` 实现 shimmer 动画
- 使用 `ShaderBrush` 配合 linear gradient 实现光扫效果
- 创建 `Modifier.shimmerEffect(visible, shape, baseColor, highlightColor)` 扩展函数
- 骨架颜色与 UI 风格相关（LiquidGlass/Material3 各有颜色方案）
- 动画使用 `tween` + `repeatForever`，shimmer angle = 18°

**Patterns to follow:**
- `LiquidGlassModifiers.kt` 的 modifier extension 模式
- Compose Material3 Shimmer 实现思路（但自研，不引入依赖）

**Test scenarios:**
- Happy path: `Modifier.shimmerEffect(visible = true, cornerRadius = 12.dp)` 在 Box 中正确显示骨架屏
- Edge case: `visible = false` 时不显示任何效果（保持原始内容）
- Edge case: 动画正确停止/重启

**Verification:**
- 预览显示 shimmer 动画
- 风格切换时颜色方案正确变化

---

- U2. **创建 BootShimmer.kt 共享 shimmer 工具（风格配置层）**

**Goal:** 在 `BootShimmer.kt` 中创建风格感知的颜色配置函数，供各组件复用

**Requirements:** R2, R4

**Dependencies:** U1

**Files:**
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootShimmer.kt`

**Approach:**
- 创建 `shimmerColors(style)` 函数，根据当前 UI 风格返回颜色对（baseColor, highlightColor）
- LiquidGlass: base=`0xFFE8E8E8`, highlight=`0xFFF8F8F8`（浅色骨架在暗色背景上）
- Material3: base=`0xFFE0E0E0`, highlight=`0xFFF5F5F5`
- 创建 `Modifier.shimmerLoading(visible, cornerRadius)` extension，提供一键 shimmer 效果
- 封装 `shimmerEffect()` 的默认参数配置（animationSpec, shimmerAngle 等）

**Patterns to follow:**
- `LiquidGlassModifiers.kt` 的 modifier extension 模式
- 颜色根据 `LocalUiStyle` 动态选择

**Test scenarios:**
- Happy path: `Modifier.shimmerLoading(visible = true, cornerRadius = 12.dp)` 在 Box 中正确显示骨架屏
- Edge case: `visible = false` 时不显示任何效果（保持原始内容）

**Verification:**
- 预览显示 shimmer 动画
- 风格切换时颜色方案正确变化

---

- U3. **为 BootCard 添加 isLoading 支持**

**Goal:** 在 `BootCard` 中添加 `isLoading` 参数，当 loading 时显示骨架占位符

**Requirements:** R1, R2, R3

**Dependencies:** U2

**Files:**
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootCard.kt`
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/LiquidGlassCard.kt`
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/material3/Material3Cards.kt`

**Approach:**
- 在 `BootCard` 中添加 `isLoading: Boolean = false` 参数
- 在 `when (LocalUiStyle.current)` 分发时传递 `isLoading` 参数
- LiquidGlassCard：当 `isLoading = true` 时，在 content 内显示占位符（模拟内容区域），保留 `.liquidGlassSurface()` 外层样式
- Material3Card：当 `isLoading = true` 时，在 content 内显示占位符，保留 Material3 卡片样式
- 使用 shimmer modifier 包裹占位符内容

**Patterns to follow:**
- `BootProgress.kt` 的 `when (LocalUiStyle.current)` 分发模式
- `LiquidGlassCard` 的 `.liquidGlassSurface()` 使用方式

**Test scenarios:**
- Happy path: `BootCard(isLoading = true)` 显示骨架卡片
- Happy path: `BootCard(isLoading = false)` 显示正常卡片内容
- Happy path: `BootCard(isLoading = true) { Text("content") }` 加载时显示骨架而非内容
- Edge case: 嵌套 BootCard 的 isLoading 行为正确

**Verification:**
- 预览在 LiquidGlass 风格下显示暗色系骨架卡片
- 预览在 Material3 风格下显示浅色系骨架卡片
- isLoading 切换时状态正确变化

---

- U4. **为 BootText 添加 isLoading 支持**

**Goal:** 在 `BootText` 中添加 `isLoading` 参数，当 loading 时显示文本行骨架占位符

**Requirements:** R1, R3

**Dependencies:** U2

**Files:**
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootText.kt`
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/LiquidGlassText.kt`
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/material3/Material3Text.kt`

**Approach:**
- 在 `BootText` 中添加 `isLoading: Boolean = false` 参数
- 当 `isLoading = true` 时，显示与原文本等高（fontSize、lineHeight）的单行骨架占位符
- 当 `isLoading = false` 时，行为与原来完全一致
- 占位符宽度默认 60%，通过可选参数可调整

**Patterns to follow:**
- `BootCard` 的 isLoading 分发模式
- Material3 Design 的文本骨架屏规范

**Test scenarios:**
- Happy path: `BootText("Hello", isLoading = true)` 显示文本高度的骨架占位符
- Happy path: `BootText("Hello", isLoading = false)` 显示正常文本
- Edge case: 不同 fontSize 和 lineHeight 的骨架高度正确匹配

**Verification:**
- 预览显示与原文本高度一致的骨架占位符
- 切换 isLoading 时文本/骨架正确交替

---

- U5. **为 BootImage 添加 isLoading 支持**

**Goal:** 在 `BootImage` 中添加 `isLoading` 参数，当 loading 时显示图片尺寸骨架占位符

**Requirements:** R1, R3

**Dependencies:** U2

**Files:**
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootImage.kt`
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/AsyncImageView.kt`

**Approach:**
- 在 `BootImage` 中添加 `isLoading: Boolean = false` 参数
- 当 `isLoading = true` 时，显示与 Image 尺寸一致的骨架占位符（忽略 model）
- 骨架占位符覆盖整个 Image 区域，不显示 placeholder/error painter
- 保持现有的 cornerRadius、contentDescription 等参数

**Patterns to follow:**
- `BootImage` 现有的 Coil3 集成模式

**Test scenarios:**
- Happy path: `BootImage(model = url, isLoading = true)` 显示图片尺寸的骨架占位符
- Happy path: `BootImage(model = url, isLoading = false)` 显示实际图片
- Edge case: 不同尺寸图片的骨架高度正确

**Verification:**
- 预览显示正确尺寸的骨架图片占位符
- 图片加载完成后骨架消失，显示实际图片

---

- U6. **为 BootSurface 添加 isLoading 支持**

**Goal:** 在 `BootSurface` 中添加 `isLoading` 参数，当 loading 时显示表面骨架

**Requirements:** R1, R3

**Dependencies:** U2

**Files:**
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootSurface.kt`
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/LiquidGlassSurface.kt`
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/material3/Material3Surfaces.kt`

**Approach:**
- 在 `BootSurface` 中添加 `isLoading: Boolean = false` 参数
- 当 `isLoading = true` 时，在 content 外层包裹 shimmer 效果
- 保留现有的 cornerRadius 等样式参数

**Patterns to follow:**
- `BootSurface` 的分发模式

**Test scenarios:**
- Happy path: `BootSurface(isLoading = true)` 显示骨架表面
- Edge case: cornerRadius 在 shimmer 效果中正确应用

**Verification:**
- 预览显示圆角正确的骨架表面

---

- U7. **为 BootElevatedCard / BootOutlinedCard 添加 isLoading 支持**

**Goal:** 在 `BootCards.kt` 的变体卡片中添加 `isLoading` 参数，委托给 BootCard 实现

**Requirements:** R1

**Dependencies:** U3, U6

**Files:**
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootCards.kt`

**Approach:**
- 在 `BootElevatedCard`、`BootOutlinedCard` 中添加 `isLoading: Boolean = false` 参数
- 通过组合使用底层 BootCard 的 isLoading 功能（不重复实现 shimmer 逻辑）

**Patterns to follow:**
- `BootCards.kt` 现有实现

**Test scenarios:**
- Happy path: `BootElevatedCard(isLoading = true)` 显示骨架卡片
- Happy path: `BootOutlinedCard(isLoading = true)` 显示骨架卡片

**Verification:**
- 预览显示变体卡片的骨架效果

---

- U8. **添加预览和文档**

**Goal:** 为所有组件的 isLoading 功能添加 IDE 预览，并更新 SKILL.md 集成指南

**Requirements:** R5

**Dependencies:** U3, U4, U5, U6, U7

**Files:**
- Modify: U3, U4, U5, U6, U7 创建/修改的文件（添加 @Preview）
- Modify: `skills/boot-app-kmp/SKILL.md`

**Approach:**
- 使用 `@PreviewWrapper` 为每个组件添加亮色/暗色预览
- 预览包含 `isLoading = true` 和 `isLoading = false` 两种状态
- 更新 SKILL.md，添加骨架屏使用说明

**Patterns to follow:**
- `BootProgress.kt` 的预览和文档模式

**Test scenarios:**
- Test expectation: none — 纯文档变更

**Verification:**
- Android Studio 预览正常显示各组件的 loading 状态
- 文档可读且完整

---

## System-Wide Impact

- **Interaction graph:** 新增参数不影响现有组件行为，仅在 isLoading=true 时替换内容
- **Error propagation:** shimmer 动画失败时优雅降级为静态占位符
- **State lifecycle risks:** 无持久化状态，仅 UI 效果
- **API surface parity:** 所有组件的 isLoading 参数行为一致
- **Integration coverage:** 支持组合到任何现有布局中

---

## Risks & Dependencies

| Risk | Mitigation |
|------|------------|
| 自研 shimmer 动画在低端 Android/iOS 设备上掉帧 | 使用 `.graphicsLayer { clip = true }` 限制绘制区域，动画使用 `frameRate` 友好的 `tween` 曲线 |
| 不同平台 shader 行为差异 | Web/WASM 使用 CSS animation fallback（通过 `Modifier.shimmerEffect()` 的平台特定实现） |
| BootImage 在无固定尺寸时骨架高度不确定 | 要求调用方提供明确尺寸（`Modifier.fillMaxWidth().height(200.dp)` 等），或使用 `BoxWithConstraints` 限制最小尺寸 |
| 骨架屏颜色在不同 app 主题下可读性差 | 颜色方案基于 `LocalContentColor` / `LocalMaterialTheme.colorScheme` 动态计算，支持亮色/暗色切换 |

---

## Documentation / Operational Notes

- 更新 `skills/boot-app-kmp/SKILL.md`，添加骨架屏使用说明
- 示例代码：
  ```kotlin
  // 卡片骨架屏
  BootCard(isLoading = isLoading) {
      // 内容
  }

  // 文本骨架屏
  BootText("Hello", isLoading = isLoading)

  // 图片骨架屏
  BootImage(model = url, isLoading = isLoading)

  // 表面骨架屏
  BootSurface(isLoading = isLoading) {
      // 内容
  }
  ```

---

## Sources & References

- **Origin plan (superseded):** [docs/plans/2026-04-30-001-feat-shimmer-skeleton-plan.md](docs/plans/2026-04-30-001-feat-shimmer-skeleton-plan.md) — 独立组件方案（已废弃，发现外部库无平台二进制）
- External library (验证不可用): `dev.seyfarth:compose-shimmer-skeleton:1.0.1` — Maven Central 仅有 javadoc 和 kotlin-metadata，无平台二进制
- Compose 内置 shimmer 实现参考: `androidx.compose.material3` Shimmer 组件
- 现有组件模式: `BootCard`, `BootText`, `BootImage`, `BootSurface`