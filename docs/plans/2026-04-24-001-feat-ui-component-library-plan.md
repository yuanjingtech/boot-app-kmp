---
title: "feat: UI Component Library with LiquidGlass/Material3 Dual Style"
type: feat
status: active
date: 2026-04-24
---

# feat: UI Component Library with LiquidGlass/Material3 Dual Style

## Overview

Build a cross-platform UI component library in the `ui` module with two visual styles: `LiquidGlass` (default, pure Compose glass-morphism) and `Material3`. Components (`BootButton`, `BootCard`, `BootSurface`, `BootTextField`) route to the correct style implementation at runtime via `CompositionLocal<BootUiStyle>`. Style preference is persisted via the existing `ThemeSettings` Room entity.

---

## Problem Frame

The `ui` module already has a `BootUiStyle` enum (`LIQUID_GLASS`, `MATERIAL3`) but no routing logic, no style implementations, and no persistence. `BootAppTheme` wraps `MaterialTheme` directly — there is no dual-style theming layer. A unified component library that supports both styles with zero API surface changes for consumers is needed.

---

## Requirements Trace

- R1. 跨平台统一的界面风格(默认 LiquidGlass, 可选 Material3)
- R2. 支持平台单独定制 — 运行时风格切换持久化，默认值按平台可配置
- R3. 组件列表：`BootButton`, `BootCard`, `BootSurface`, `BootTextField`
- R4. 风格切换 UI — 在现有 `BootThemeSettingScreen` 中增加 UI 风格选项

---

## Scope Boundaries

- 不实现平台原生模糊 API（iOS `UIVisualEffectView` / Android `RenderEffect`）— 使用纯 Compose 实现 LiquidGlass
- 不迁移现有组件到新 `Boot*` API — 新组件独立提供
- 不实现暗色/亮色 LiquidGlass 调色板 — 与 `BootThemeMode` 的 light/dark 共用 `BootAppTheme` 配色

---

## Context & Research

### Relevant Code and Patterns

- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/BootUiStyle.kt` — existing enum, unchanged
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/UiModule.kt` — existing Koin module
- `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/AsyncImageView.kt` — existing component pattern
- `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/data/theme/room3/ThemeSettings.kt` — entity to extend with `uiStyle`
- `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/data/theme/BootThemeStore.kt` — store to extend with `uiStyleFlow`
- `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/theme/BootThemeSettingScreen.kt` — settings UI to extend with style radio buttons
- `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/theme/BootAppTheme.kt` — theme composable; uses `LocalDarkTheme` pattern as precedent
- `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/data/theme/room3/ThemeDao.kt` — DAO to add style query

### Institutional Learnings

- `docs/solutions/runtime-errors/theme-settings-null-flow-runtime-crash-2026-04-24.md` — Room `Flow<T?>` on iOS/WASM returns null at runtime; always use `?.let { } ?: default` pattern (not `!!`)
- `BootThemeStore` pattern: `Flow<T>` at consumer, `Flow<T?>` at DAO, safe-call with default fallback

### Key Technical Decisions

- **Style routing via `CompositionLocal<BootUiStyle>`** — follows `LocalDarkTheme` precedent in `BootAppTheme`. Each `Boot*` component calls `when (LocalUiStyle.current)` inside its composable body. No `expect/actual` needed since LiquidGlass is pure Compose.
- **LiquidGlass via layered semi-transparent surfaces** — `Modifier.background(Color.White.copy(alpha=0.15f))` + rounded corners + subtle border. No `Modifier.blur` (not universally supported). Mimics iOS VisualEffectView aesthetic using Compose primitives.
- **Persistence via `ThemeSettings` table** — add `uiStyle: String = "LIQUID_GLASS"` column to existing entity; add `uiStyleFlow` and `setUiStyle()` to `BootThemeStore`; DAO query updated to fetch both columns. Single-table co-location avoids cross-module Room complexity.
- **Style injection via `BootAppTheme` `CompositionLocalProvider`** — `LocalUiStyle` is defined in `ui` module (`ui/src/commonMain/.../LocalUiStyle.kt`) and imported by `BootAppTheme` in `shared`. The module dependency is correct: `shared` has `api(projects.ui)` in `build.gradle.kts`, so `shared` can see `ui`'s public API. No circular dependency.
- **Platform default via `expect val defaultUiStyle`** — declared in `BootUiStyle.kt` (ui module, commonMain), actuals per platform: Android → `MATERIAL3` (per requirements), iOS → `LIQUID_GLASS`, JVM/JS/WASM → `LIQUID_GLASS`. All platforms must provide an actual to avoid linker errors. `BootThemeStore` uses `defaultUiStyle` as the Room fallback.
- **Parameter fallback vs store-driven**: `BootAppTheme` collects `uiStyleFlow` from `BootThemeStore` — the store drives the value. The `bootUiStyle` parameter (if provided) overrides only when the store value is unavailable during initial composition — consistent with `BootThemeMode` pattern in this codebase.

---

## Open Questions

### Resolved During Planning

- **Q: LiquidGlass 用 Compose 还是平台原生 API？** → 纯 Compose 实现，无 native 依赖，落地最快
- **Q: 风格持久化放在哪个模块？** → 复用 `ThemeSettings` 表（shared/data），避免 ui 模块引入 Room 依赖
- **Q: `BootUiStyle` 作为 `CompositionLocal` 还是参数传递？** → `CompositionLocal<BootUiStyle>`，与 `LocalDarkTheme` 模式一致

### Deferred to Implementation

- **Q: LiquidGlass 边框颜色 / 模糊强度参数是否暴露？** → V1 固定参数，后续按需开放 `BootButtonConfig`
- **Q: 是否需要 `BootIconButton` / `BootFAB`？** → V1 仅实现 R3 列表中的 4 个组件

---

## Output Structure

    ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/
    ├── BootUiStyle.kt                    # existing: enum + CompositionLocal
    ├── UiModule.kt                       # existing: Koin module (no change needed)
    ├── AsyncImageView.kt                # existing
    ├── LocalUiStyle.kt                   # NEW: CompositionLocal<BootUiStyle> + Provider
    ├── components/                        # NEW: component definitions + style routing
    │   ├── BootButton.kt
    │   ├── BootCard.kt
    │   ├── BootSurface.kt
    │   └── BootTextField.kt
    ├── liquidglass/                      # NEW: LiquidGlass style implementation
    │   ├── LiquidGlassColors.kt
    │   ├── LiquidGlassComponents.kt      # LiquidGlassButton, LiquidGlassCard, etc.
    │   └── LiquidGlassModifiers.kt
    └── material3/                        # NEW: Material3 style implementation
        ├── Material3Components.kt        # Material3Button, Material3Card, etc.
        └── Material3Colors.kt

    ui/src/androidMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/  # NEW platform source sets
    ui/src/iosMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/      #   (defaultUiStyle actuals)
    ui/src/jvmMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/
    ui/src/jsMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/
    ui/src/wasmJsMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/

    shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/
    ├── data/theme/room3/
    │   ├── ThemeSettings.kt              # MODIFY: add uiStyle column
    │   └── ThemeDao.kt                  # MODIFY: add getUiStyle / insertOrUpdate (same method)
    ├── data/theme/
    │   └── BootThemeStore.kt            # MODIFY: add uiStyleFlow + setUiStyle()
    └── theme/
        ├── BootThemeSettingScreen.kt     # MODIFY: add UI style radio group
        └── BootAppTheme.kt               # MODIFY: provide LocalUiStyle

---

## High-Level Technical Design

> *This illustrates the intended approach and is directional guidance for review, not implementation specification.*

### Style Routing Pattern

```kotlin
// LocalUiStyle.kt — CompositionLocal with platform compile-time default
val LocalUiStyle = compositionLocalOf { defaultUiStyle }  // defaultUiStyle is expect

// components/BootButton.kt — routes to correct implementation
@Composable
fun BootButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit = {},
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassButton(onClick, modifier, enabled, content)
        BootUiStyle.MATERIAL3 -> Material3Button(onClick, modifier, enabled, content)
    }
}
```

### Persistence Flow

```
ThemeSettings (id=1, themeMode="DARK", uiStyle="LIQUID_GLASS")
         ↓
ThemeDao.getThemeSettings(): Flow<ThemeSettings?>
         ↓
BootThemeStore.uiStyleFlow: Flow<BootUiStyle>
    (safe-call: ThemeSettings?.uiStyle ?: defaultUiStyle)
         ↓
BootThemeSettingScreen — reads flow, renders radio group
         ↓
BootThemeStore.setUiStyle(mode: BootUiStyle)
         ↓
ThemeDao.insertOrUpdate(settings.copy(uiStyle = mode.name))
```

---

## Implementation Units

- [ ] U1. **[Extend ThemeSettings and BootThemeStore with uiStyle]**

**Goal:** Add `uiStyle` persistence to the existing theme settings table and store.

**Requirements:** R1, R2, R4

**Dependencies:** None

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/data/theme/room3/ThemeSettings.kt`
- Modify: `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/data/theme/room3/ThemeDao.kt`
- Modify: `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/data/theme/BootThemeStore.kt`

**Approach:**
- Add `uiStyle: String = "LIQUID_GLASS"` to `ThemeSettings` data class. No schema migration needed (v1, `OnConflictStrategy.REPLACE` handles existing rows).
- `ThemeDao` query unchanged — `getThemeSettings()` already returns `Flow<ThemeSettings?>`.
- Add to `BootThemeStore`:
  ```kotlin
  val uiStyleFlow: Flow<BootUiStyle> = themeDao.getThemeSettings().map { settings ->
      settings?.uiStyle?.let { uiStyle -> BootUiStyle.valueOf(uiStyle) } ?: BootUiStyle.LIQUID_GLASS
  }
  suspend fun setUiStyle(mode: BootUiStyle) {
      val current = themeDao.getThemeSettings().first()
      themeDao.insertOrUpdate(
          ThemeSettings(
              id = 1,
              themeMode = current?.themeMode ?: "FOLLOW_SYSTEM",
              uiStyle = mode.name
          )
      )
  }
  ```

**Patterns to follow:**
- `BootThemeStore` existing pattern for `themeModeFlow` / `setThemeMode`
- Defensive `?.let { } ?: default` (not `!!`) — platform divergence risk per `docs/solutions/runtime-errors/theme-settings-null-flow-runtime-crash-2026-04-24.md`

**Test scenarios:**
- Happy path: `uiStyleFlow` emits `LIQUID_GLASS` after insert
- Edge case: null `uiStyle` in DB → defaults to `LIQUID_GLASS`
- Edge case: invalid `uiStyle` string → catch `IllegalArgumentException` → default to `LIQUID_GLASS`
- Integration: `setUiStyle` preserves existing `themeMode` value

**Verification:**
- `uiStyleFlow.first()` returns correct `BootUiStyle` after store operations

---

- [ ] U2. **[Create LocalUiStyle, platform source sets, and expect/actual defaults]**

**Goal:** Establish the style routing infrastructure: `CompositionLocal<BootUiStyle>`, platform source sets in `ui` module, and `expect val defaultUiStyle` actuals per platform.

**Requirements:** R1, R2

**Dependencies:** None

**Files:**
- Create: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/LocalUiStyle.kt`
- Modify: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/BootUiStyle.kt` — add `expect val defaultUiStyle: BootUiStyle`
- Create: `ui/src/androidMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/BootUiStyle.android.kt` — `actual val defaultUiStyle = BootUiStyle.MATERIAL3`
- Create: `ui/src/iosMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/BootUiStyle.ios.kt` — `actual val defaultUiStyle = BootUiStyle.LIQUID_GLASS`
- Create: `ui/src/jvmMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/BootUiStyle.jvm.kt` — `actual val defaultUiStyle = BootUiStyle.LIQUID_GLASS`
- Create: `ui/src/jsMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/BootUiStyle.js.kt` — `actual val defaultUiStyle = BootUiStyle.LIQUID_GLASS`
- Create: `ui/src/wasmJsMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/BootUiStyle.wasmJs.kt` — `actual val defaultUiStyle = BootUiStyle.LIQUID_GLASS`

**Approach:**
```kotlin
// BootUiStyle.kt (commonMain) — add expect:
expect val defaultUiStyle: BootUiStyle

// LocalUiStyle.kt (commonMain):
val LocalUiStyle = compositionLocalOf { defaultUiStyle }
```

Each platform source set provides its `actual val`. The `compositionLocalOf { defaultUiStyle }` call uses the platform's actual at compile time. `BootAppTheme` provides the runtime value from the store; the CompositionLocal default only fires when no provider is in scope.

**Patterns to follow:**
- `shared/src/commonMain/kotlin/.../Platform.kt` — `expect fun getPlatform()` with per-platform actuals in `androidMain`, `iosMain`, `jvmMain`, etc.

**Test scenarios:**
- Edge case: Consuming composable reads `LocalUiStyle.current` before any provider — uses platform's `actual val defaultUiStyle` (compile-time default)

**Patterns to follow:**
- `LocalDarkTheme` in `BootAppTheme.kt` — same `compositionLocalOf { }` pattern

**Test scenarios:**
- Edge case: consuming composable reads `LocalUiStyle.current` before any provider → uses `defaultUiStyle`

**Verification:**
- Each platform's actual matches the expected default

---

- [ ] U3. **[Implement LiquidGlass style components]**

**Goal:** Create LiquidGlass visual style using pure Compose layered surfaces.

**Requirements:** R1

**Dependencies:** U2

**Files:**
- Create: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidgalass/LiquidGlassColors.kt`
- Create: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidgalass/LiquidGlassModifiers.kt`
- Create: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidgalass/LiquidGlassComponents.kt`

**Approach:**
LiquidGlass visual language: semi-transparent white surface + rounded corners + subtle border. Inspired by iOS VisualEffectView aesthetic.

```kotlin
// LiquidGlassColors.kt
data class LiquidGlassColors(
    val surface: Color = Color.White.copy(alpha = 0.12f),
    val surfaceDark: Color = Color.Black.copy(alpha = 0.24f),
    val border: Color = Color.White.copy(alpha = 0.18f),
    val content: Color = Color.White,
    val contentDark: Color = Color.White.copy(alpha = 0.87f),
)
val LocalLiquidGlassColors = compositionLocalOf { LiquidGlassColors() }

// LiquidGlassModifiers.kt
fun Modifier.liquidGlassSurface(isDark: Boolean): Modifier = this
    .background(
        brush = Brush.verticalGradient(
            colors = if (isDark) listOf(
                Color.White.copy(alpha = 0.16f),
                Color.White.copy(alpha = 0.05f)
            ) else listOf(
                Color.White.copy(alpha = 0.20f),
                Color.White.copy(alpha = 0.08f)
            )
        ),
        shape = RoundedCornerShape(16.dp)
    )
    .border(
        width = 0.5.dp,
        color = Color.White.copy(alpha = 0.20f),
        shape = RoundedCornerShape(16.dp)
    )
```

Components (`LiquidGlassComponents.kt`):
```kotlin
@Composable fun LiquidGlassButton(
    onClick: () -> Unit, modifier: Modifier, enabled: Boolean, content
)
@Composable fun LiquidGlassCard(modifier: Modifier, content: @Composable ColumnScope.() -> Unit)
@Composable fun LiquidGlassSurface(modifier: Modifier, content: @Composable () -> Unit)
@Composable fun LiquidGlassTextField(...)
```

**Patterns to follow:**
- `AsyncImageView.kt` — `@Preview`, standard Compose `@Composable` conventions
- `LXGWWenKaiTypography.kt` — color handling inside `BootAppTheme`

**Test scenarios:**
- Happy path: `LiquidGlassButton` renders with correct transparency and rounded corners
- Edge case: `LiquidGlassSurface` with very long content — respects bounds
- Visual: Dark/light mode — uses `LocalDarkTheme.current`

**Verification:**
- `@Preview` composables render correctly in Android Studio / IDEA
- Compose preview screenshot matches design intent

---

- [ ] U4. **[Implement Material3 style components]**

**Goal:** Create Material3 wrapper components for `BootUiStyle.MATERIAL3`.

**Requirements:** R1

**Dependencies:** U2

**Files:**
- Create: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/material3/Material3Colors.kt`
- Create: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/material3/Material3Components.kt`

**Approach:**
Thin wrappers around existing Material3 components. Use `MaterialTheme.colorScheme` and `MaterialTheme.typography` — these are already configured by `BootAppTheme`.

```kotlin
@Composable fun Material3Button(
    onClick: () -> Unit, modifier: Modifier, enabled: Boolean, content: @Composable RowScope.() -> Unit
) {
    androidx.compose.material3.Button(onClick = onClick, modifier = modifier, enabled = enabled) {
        content()
    }
}
// Similarly: Material3Card, Material3Surface, Material3TextField
```

**Patterns to follow:**
- `BootThemeSettingScreen.kt` — direct usage of `MaterialTheme`, `RadioButton`, `Text` from Material3
- `LXGWWenKaiTypography.kt` — compose.material3 imports

**Test scenarios:**
- Happy path: `Material3Button` passes clicks and enabled state correctly
- Edge case: Empty `content` — `Button` renders enabled with no content (Material3 default)

**Verification:**
- `@Preview` composables render with standard Material3 appearance

---

- [ ] U5. **[Create Boot* component wrappers with style routing]**

**Goal:** Define the public `Boot*` component API that routes to LiquidGlass or Material3 based on `LocalUiStyle`.

**Requirements:** R1, R3

**Dependencies:** U2, U3, U4

**Files:**
- Create: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootButton.kt`
- Create: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootCard.kt`
- Create: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootSurface.kt`
- Create: `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/BootTextField.kt`

**Approach:**
Each `Boot*` component is a thin router:
```kotlin
@Composable
fun BootButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit = {},
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassButton(onClick, modifier, enabled, content)
        BootUiStyle.MATERIAL3 -> Material3Button(onClick, modifier, enabled, content)
    }
}
```

The `Modifier` parameter is passed through to the underlying implementation — each implementation applies its own style modifiers internally.

**Patterns to follow:**
- `BootThemeSettingScreen.kt` — parameter naming convention (`onClick`, `modifier`, `enabled`, `content`)
- `AsyncImageView.kt` — public composable API design with defaults

**Test scenarios:**
- Happy path: `BootButton` calls `LiquidGlassButton` when `LocalUiStyle.current == LIQUID_GLASS`
- Happy path: `BootButton` calls `Material3Button` when `LocalUiStyle.current == MATERIAL3`
- Edge case: No `LocalUiStyleProvider` in tree → uses `defaultUiStyle` (CompositionLocal default)
- Integration: Consumer replaces `Material3.Button` with `BootButton` — no API change needed

**Verification:**
- Replace one `Material3.Button` in `BootThemeSettingScreen.kt` with `BootButton`; verify style switches correctly

---

- [ ] U6. **[Integrate uiStyle into BootAppTheme and BootThemeSettingScreen]**

**Goal:** Provide `LocalUiStyle` at theme root and add style selection UI.

**Requirements:** R1, R2, R4

**Dependencies:** U1, U5

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/theme/BootAppTheme.kt`
- Modify: `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/theme/BootThemeSettingScreen.kt` — add radio group to `BootThemeSettingScreenWithStore` (the store-backed composable, not the stateless `BootThemeSettingScreen`)
- Modify: `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/di/BootDataModule.kt` (add `uiStyleFlow` binding if needed)

**Approach:**

`BootAppTheme.kt`:
```kotlin
@Composable
fun BootAppTheme(
    themeStore: BootThemeStore = koinInject(),
    bootUiStyle: BootUiStyle = defaultUiStyle,  // parameter, not injected
    ...
) {
    val themeMode by themeStore.themeModeFlow.collectAsState(initial = BootThemeMode.FOLLOW_SYSTEM)
    val uiStyle by themeStore.uiStyleFlow.collectAsState(initial = defaultUiStyle)
    CompositionLocalProvider(
        LocalDarkTheme provides isDarkTheme,
        LocalUiStyle provides uiStyle,
    ) {
        MaterialTheme(...) { content() }
    }
}
```

`BootThemeSettingScreen.kt` — add RadioGroup for `BootUiStyle`:
```kotlin
@Composable
fun BootThemeSettingScreenWithStore(...) {
    val uiStyle by themeStore.uiStyleFlow.collectAsState(initial = defaultUiStyle)
    // in the settings list:
    RadioGroup(
        options = BootUiStyle.entries,
        selected = uiStyle,
        onSelect = { themeStore.setUiStyle(it) }
    )
}
```

**Patterns to follow:**
- `BootAppTheme.kt` — `CompositionLocalProvider` + `collectAsState` + `koinInject()` pattern
- `BootThemeSettingScreen.kt` — existing radio group for `BootThemeMode`

**Test scenarios:**
- Happy path: Selecting `MATERIAL3` in settings → `BootButton` in same screen switches to Material3
- Happy path: Android default `MATERIAL3` — no style selected yet → uses `MATERIAL3`
- Edge case: `uiStyleFlow` emits null → `defaultUiStyle` used (CompositionLocal default)

**Verification:**
- Style switch reflects immediately in the settings screen (same composition)

---

- [ ] U7. **[Unit tests for BootThemeStore uiStyle]**

**Goal:** Add unit tests covering `uiStyleFlow` and `setUiStyle()`.

**Requirements:** R1, R2

**Dependencies:** U1

**Files:**
- Create: `shared/src/commonTest/kotlin/com/yuanjingtech/boot/app/kmp/data/theme/BootThemeStoreUiStyleTest.kt`

**Approach:**
Extend the existing `BootThemeStoreTest.kt` pattern with `FakeThemeDao` supporting `uiStyle` column.

**Test scenarios:**
- `themeStore_uiStyleFlow_initialValue_isLiquidGlass`
- `themeStore_setUiStyleToMaterial3_updatesFlow`
- `themeStore_invalidUiStyleString_defaultsToLiquidGlass`
- `themeStore_nullSettings_defaultsToLiquidGlass`
- `themeStore_setUiStyle_preservesExistingThemeMode`

**Verification:**
- All tests pass via `./gradlew :shared:jvmTest`

---

## System-Wide Impact

- **Interaction graph:** `BootAppTheme` now reads `uiStyleFlow` alongside `themeModeFlow`; both collected at theme root. No component above `BootAppTheme` needs changes.
- **Error propagation:** If `uiStyleFlow` throws (invalid enum string), `BootAppTheme` catches and uses `LIQUID_GLASS` — style errors never propagate to components.
- **API surface parity:** All new `Boot*` components follow the same parameter conventions (`onClick`, `modifier`, `enabled`, `content`) — consistent with existing Compose conventions.
- **Unchanged invariants:** `BootThemeMode` behavior unchanged. `BootAppTheme`'s `MaterialTheme` wrapper unchanged. `BootThemeSettingScreen` layout unchanged except for added radio group.

---

## Risks & Dependencies

| Risk | Mitigation |
|------|------------|
| `ThemeSettings` schema change with existing users | `OnConflictStrategy.REPLACE` on `insertOrUpdate` — existing rows get new `uiStyle` column with default Kotlin value `"LIQUID_GLASS"` |
| `BootUiStyle` unused in `ui` module causes unused import warning | Only add `uiModule` factory for `BootThemeStore` when components consume it |
| Android defaulting to `MATERIAL3` but not importing it | `ui/build.gradle.kts` already has `compose-material3` in `commonMain` |
| Style switch not working across app restart | `uiStyle` persisted to Room via `setUiStyle`; survives restart |

---

## Sources & References

- Existing code: `BootUiStyle.kt`, `UiModule.kt`, `BootThemeStore.kt`, `ThemeDao.kt`, `ThemeSettings.kt`, `BootThemeSettingScreen.kt`, `BootAppTheme.kt`
- Pattern: `LocalDarkTheme` in `BootAppTheme.kt`
- Learning: `docs/solutions/runtime-errors/theme-settings-null-flow-runtime-crash-2026-04-24.md`
- Feature description: user-provided in `/compound-engineering:lfg` invocation
