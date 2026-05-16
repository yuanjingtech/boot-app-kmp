# Code Review Findings — feat/shimmer-skeleton

**Branch:** feat/shimmer-skeleton
**Base:** caf9f37f5b07c6c627fbeb2818c79c9cccb7dd23
**Review Date:** 2026-04-30
**Reviewers:** correctness, testing, maintainability, project-standards, agent-native, learnings

---

## P0 — Critical

| # | File | Line | Title | Severity | Confidence | autofix_class | Reviewer |
|---|------|------|-------|----------|------------|---------------|----------|
| 1 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootShimmer.kt` | 113-119 | 渐变 stop 计算 `sortedBy + map { it.second }` 可能产生非单调 stop 序列，导致 shimmer 扫光在错误位置显示错误颜色，引发视觉闪烁 | P0 | 75 | `safe_auto` | correctness, learnings |
| 2 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/` | — | ui module 无 commonTest 目录；新增 shimmerEffect() 和 6 个 isLoading 分发路径完全无单元测试覆盖 | P0 | 100 | `manual` | testing |

## P1 — High

| # | File | Line | Title | Severity | Confidence | autofix_class | Reviewer |
|---|------|------|-------|----------|------------|---------------|----------|
| 3 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/LiquidGlassSurface.kt` | 1279 | `LiquidGlassSurfacePreview()` 预览函数被删除，仅保留 isLoading 预览，非 loading 状态无 IDE 预览 | P1 | 75 | `gated_auto` | correctness, project-standards |
| 4 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/LiquidGlassText.kt` | 1372-1385 | 骨架高度仅使用 lineHeight（fallback 24dp），忽略 fontSize；`BootText(fontSize=48.sp, isLoading=true)` 渲染 24dp 栏而非 48sp 高度 | P1 | 75 | `gated_auto` | correctness |
| 5 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/material3/Material3Text.kt` | 1690 | Material3Text fallback 为 20dp，LiquidGlassText fallback 为 24dp；相同 BootText 输入产生不同高度骨架 | P1 | 65 | `gated_auto` | correctness |
| 6 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootShimmer.kt` | 836-859 | 暗色主题下 LIQUID_GLASS 和 MATERIAL3 返回相同颜色（`0xFF2C2C2C/0xFF3A3A3A`）；风格区分在暗色下失效 | P1 | 40 | `advisory` | correctness |
| 7 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/LiquidGlassCard.kt` | 1193-1223 | `isLoading=true` 时 content lambda 被静默丢弃，渲染固定 3 行骨架；调用方无法自定义骨架以匹配实际内容布局 | P1 | 75 | `gated_auto` | correctness, maintainability |
| 8 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/` + `material3/` 目录 | — | 内部实现层独立渲染 isLoading 骨架内容，违反 3 层架构（components/ 应为 public API，内容生成不应在 internal 层） | P1 | 85 | `manual` | project-standards |

## P2 — Moderate

| # | File | Line | Title | Severity | Confidence | autofix_class | Reviewer |
|---|------|------|-------|----------|------------|---------------|----------|
| 9 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/AsyncImageView.kt` | 511-513 | isLoading 硬编码 `RoundedCornerShape(8.dp)` 覆盖调用方 modifier 中的圆角设置 | P2 | 60 | `gated_auto` | correctness |
| 10 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootCards.kt` | 627 | `BootElevatedCard` under LIQUID_GLASS 路由到基础 `LiquidGlassCard`，elevated 样式未保留；骨架外观与普通 BootCard 无区别 | P2 | 75 | `advisory` | correctness |
| 11 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootText.kt` | 1076 | `loadingWidthFraction` 无边界校验；`fillMaxWidth(1.5f)` 或 NaN 行为未测试 | P2 | 100 | `gated_auto` | testing |
| 12 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootImage.kt` | 770-793 | `BootImage(isLoading=true, modifier=Modifier)` 无固有尺寸约束，骨架 Box 在无约束布局中高度为 0 | P2 | 80 | `gated_auto` | testing |
| 13 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootImage.kt` | 46 | `@OptIn(ExperimentalCoilApi::class)` 仅因预览函数需要，但 `AsyncImageView` 公共签名无此 OptIn，造成不一致 | P2 | 50 | `gated_auto` | maintainability |
| 14 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootImage.kt` | 83 | `BootImageLoadingPreview` 中 `Box(modifier = Modifier)` 无功能价值，零缩进价值 | P2 | 75 | `safe_auto` | maintainability |
| 15 | `docs/plans/2026-04-30-002-feat-shimmer-integrated-plan.md` | 79-82 | Scope Boundaries 描述封装外部库但实际使用自研 modifier；文档与实现不一致 | P2 | 80 | `manual` | project-standards, agent-native |

## P3 — Low

| # | File | Line | Title | Severity | Confidence | autofix_class | Reviewer |
|---|------|------|-------|----------|------------|---------------|----------|
| 16 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootShimmer.kt` | 77 | `shimmerLoading` 是 thin wrapper，未转发 `baseColor`/`highlightColor` | P3 | 50 | `advisory` | maintainability |
| 17 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootShimmer.kt` | 29 | `shimmerColors()` private 函数仅内部调用，无独立复用价值 | P3 | 25 | `advisory` | maintainability |
| 18 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootShimmer.kt` | — | shimmerEffect 在 visible=true 时每次重组创建新 infiniteTransition，轻度效率问题 | P3 | 50 | `advisory` | correctness |

---

## Pre-existing Issues

| # | File | Line | Title | Severity | Confidence | autofix_class | Reviewer |
|---|------|------|-------|----------|------------|---------------|----------|
| PE1 | `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootCards.kt` | 627 | LIQUID_GLASS 风格下 elevated/outlined 卡片路由到基础 LiquidGlassCard，样式无区分（架构层面预存问题） | P2 | 75 | `advisory` | correctness |

---

## Residual Risks

- `BootShimmer` gradient sort bug 可能在 iOS/WASM 上产生视觉伪影
- `BootImage(isLoading=true, modifier=Modifier)` 无高度约束时坍缩为 0
- shimmerEffect 动画在低性能设备（50+ shimmer 卡片列表）中可能掉帧
- 无跨平台 shimmer 视觉验证（iOS/Android/Web/JVM）
- 所有骨架 Box 元素缺少 contentDescription（无障碍问题）
- 每帧重组创建新 infiniteTransition，轻度效率问题

## Testing Gaps

- 无 shimmer 单元测试（渐变偏移、圆角传播、颜色选择）
- ui module 无 commonTest 目录
- 无亮/暗主题 shimmer 颜色截图测试
- 无 `loadingWidthFraction` 边界值测试
- 无 `BootImage(isLoading=true, modifier=Modifier)` 零尺寸退化测试
- 无 CI 预览渲染验证
- 无快速 isLoading 切换稳定性测试
- 无骨架高度与文本高度匹配测试

## Requirements Completeness (plan: explicit)

| ID | Requirement | 状态 |
|----|-------------|------|
| R1 | 为 BootCard、BootText、BootImage、BootSurface 添加 `isLoading: Boolean = false` 参数 | ✅ 已实现 |
| R2 | 骨架屏颜色方案与 UI 风格一致 | ⚠️ 部分（暗色主题下两种风格颜色相同） |
| R3 | `isLoading = false` 时组件行为不变 | ✅ 已实现 |
| R4 | 与主题系统集成，支持亮色/暗色模式 | ✅ 已实现 |
| R5 | 提供 `@Preview` 预览，文档说明使用方式 | ⚠️ Preview 存在，SKILL.md 未更新 |

## Verdict

**Not ready** — 1 个 P0 可自动修复 + 2 个架构问题需人工决策

### Auto-fix Queue (safe_auto)
- #1 BootShimmer.kt gradient stop 计算修复
- #14 BootImage.kt 无功能价值 Box 移除

### Downstream Resolver
- #2 ui module 测试基础设施创建（manual）
- #3 LiquidGlassSurface 预览恢复（gated_auto）
- #4 BootText 骨架高度匹配 fontSize（gated_auto）
- #5 骨架高度默认值不一致（gated_auto）
- #7 isLoading 时 content lambda 被丢弃（gated_auto）
- #8 架构违规：internal 层独立渲染 isLoading 内容（manual）
- #9 AsyncImageView cornerRadius 硬编码（gated_auto）
- #11 loadingWidthFraction 边界校验（gated_auto）
- #12 BootImage 无高度约束坍缩（gated_auto）
- #13 ExperimentalCoilApi 不一致（gated_auto）
- #15 计划文档更新（manual）

### Advisory / Human
- #6 暗色主题风格颜色相同（advisory）
- #10 elevated 样式未保留（advisory）
- #16 shimmerLoading thin wrapper（advisory）
- #17 shimmerColors private 函数无复用价值（advisory）
- #18 infiniteTransition 每帧重组（advisory）
- PE1 LIQUID_GLASS elevated 样式无区分（advisory）