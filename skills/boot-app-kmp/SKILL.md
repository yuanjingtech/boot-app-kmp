---
name: boot-app-kmp
description: Use when working on boot-app-kmp Kotlin Multiplatform project. Provides project structure, UI component library patterns (LiquidGlass/Material3), KMP module boundaries, and development workflow guidance.
license: Apache-2.0
metadata:
  author: Yuanjing Tech
  version: "1.0.0"
---

# boot-app-kmp Development Guide

Use this skill when working on the boot-app-kmp Kotlin Multiplatform project.

---

## Project Overview

**Type**: Kotlin Multiplatform Library + Application
**Purpose**: Base shared library with UI component system supporting LiquidGlass and Material3 styles
**Structure**: Multi-module KMP with composeApp as main application

---

## Module Structure

```
boot-app-kmp/
├── composeApp/          # Main application (Compose Multiplatform)
│   ├── src/
│   │   ├── androidMain/     # Android-specific code
│   │   ├── commonMain/      # Shared code (Kotlin + Compose)
│   │   ├── iosMain/         # iOS-specific code
│   │   ├── desktopMain/     # Desktop-specific code
│   │   └── wasmJsMain/      # WebAssembly-specific code
│   └── build.gradle.kts
├── shared/              # Shared business logic module
├── ui/                  # UI component library
│   └── src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/components/
│       ├── liquidglass/     # LiquidGlass component implementations
│       └── material3/       # Material3 component wrappers
├── webview/             # WebView integration module
├── webviewParkwoocheol/ # External webview integration
└── build.gradle.kts
```

---

## UI Component Library Architecture

### Component Style System

The UI component library supports dual-style theming:

1. **LiquidGlass Style** - Glassmorphism-inspired components with frosted glass effects
2. **Material3 Style** - Standard Material Design 3 components

### Preview System

All UI components use `@PreviewWrapper` for IDE preview with style switching:

```kotlin
@PreviewWrapper
@Composable
fun MyComponentPreview() {
    // Preview code here
}
```

### Key Components

| Component | Location | Style Support |
|-----------|----------|---------------|
| LiquidGlassScaffold | ui/src/commonMain/.../liquidglass/ | LiquidGlass only |
| LiquidGlassCard | ui/src/commonMain/.../liquidglass/ | LiquidGlass only |
| LiquidGlassButton | ui/src/commonMain/.../liquidglass/ | LiquidGlass only |
| PreviewWrapper | ui/src/commonMain/.../ | Both styles |

---

## KMP Source-Set Discipline

### Common Rules

- **Business logic** → `shared/` module
- **Platform-agnostic UI** → `ui/src/commonMain/`
- **Platform-specific code** → respective platform source sets
- **Compose UI** → `commonMain` for cross-platform, platform-specific for exceptions

### Module Dependencies

```
composeApp (app)
  └── shared (business logic)
  └── ui (UI components)
  └── webview
  └── webviewParkwoocheol

ui (library)
  └── shared
```

### Import Conventions

```kotlin
// UI components
import com.yuanjingtech.boot.app.kmp.ui.components.liquidglass.*
import com.yuanjingtech.boot.app.kmp.ui.components.material3.*

// Shared utilities
import com.yuanjingtech.boot.app.kmp.shared.*
```

---

## Development Workflow

### Building

```bash
# Build all targets
./gradlew build

# Build specific platform
./gradlew :composeApp:assembleDebug        # Android
./gradlew :composeApp:linkDebugFrameworkIosArm64  # iOS
./gradlew :composeApp:wasmJsBrowserDebug   # Web

# Run tests
./gradlew :composeApp:testDebugUnitTest
```

### Code Style

- Follow Kotlin conventions (ktlint)
- Use semantic versioning for public API
- Document breaking changes
- Add KDoc for public APIs

### Testing

- Unit tests in `commonTest` source set
- Integration tests where appropriate
- UI preview testing via `@PreviewWrapper`

---

## Known Issues & Solutions

See `docs/solutions/` for documented solutions to known problems:

- **Compose Preview API** - Common Preview API misuse patterns and fixes
- **KSP Resource Packaging** - Generated META-INF/services resource packaging
- **iOS Framework Linking** - Debug framework linking issues

---

## Git Workflow

1. Create feature branch from `main`
2. Implement with incremental commits
3. Run full build before PR
4. Request review
5. Squash and merge

### Commit Message Format

```
type(scope): description

[Optional body]

[Optional footer]
```

Types: feat, fix, docs, refactor, test, chore

---

## Dependencies

- Kotlin 2.0+
- Compose Multiplatform
- Koin (dependency injection)
- Navigation 3
- SQLDelight (database)
- Compose Resources for i18n

---

## Key Files

- `composeApp/build.gradle.kts` - App module configuration
- `ui/build.gradle.kts` - UI library configuration
- `gradle/libs.versions.toml` - Version catalog
- `docs/solutions/` - Known issue solutions