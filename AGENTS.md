# AGENTS.md

Guidance for AI coding agents working in this repository.

## Project Overview

- `boot-app-kmp` is a Kotlin Multiplatform library/application scaffold for Android, iOS, Desktop JVM, JS, and Wasm JS.
- The published library group is `com.yuanjingtech.boot.app.kmp`; subproject versions are derived from `project.build_number` and default to `0.0.2-SNAPSHOT`.
- The build uses Gradle Kotlin DSL, version catalogs, included build logic, Compose Multiplatform, AGP 9.x Kotlin Multiplatform support, Koin, KSP, SQLDelight, Room 3, and SweetSPI.

## Repository Layout

- `build.gradle.kts` / `settings.gradle.kts`: root Gradle configuration and project includes.
- `gradle/libs.versions.toml`: centralized dependency and plugin versions. Prefer adding aliases here instead of hardcoding module versions.
- `build-logic/convention`: included build containing custom convention plugins.
- `shared`: umbrella KMP module exporting core Boot APIs and most feature modules.
- `composeApp`: shared Compose application module for sample/runtime app code.
- `androidapp`: Android application wrapper.
- `desktopApp`: Desktop JVM application wrapper.
- `logging`, `network`, `sqldelight`, `plugin`, `runblocking`, `subapp`, `ui`, `webview`: focused KMP library modules.
- `sqlite-wasm-worker`: JS worker package used by web/wasm SQLite integrations.
- `docs`: project documentation and reviews.

## Build System Rules

- Use Gradle wrapper commands from the repository root.
- Do not replace `com.android.kotlin.multiplatform.library` with legacy Android/Kotlin plugin combinations in KMP library modules.
- Do not apply `kotlin-android` in `androidapp`; AGP 9 provides built-in Kotlin support there.
- Keep JVM bytecode targets at Java 17 unless a module already has stricter local requirements.
- Prefer existing convention plugins and version catalog aliases over duplicated Gradle configuration.
- Preserve generated-resource wiring for KSP/SweetSPI service files in Android source sets when touching affected modules.
- For AGP 9/KMP migrations or related failures, follow the local `kotlin-tooling-agp9-migration` skill if available.

## Source Set Conventions

- Put shared code in `src/commonMain/kotlin` and shared tests in `src/commonTest/kotlin`.
- Use platform source sets only for platform-specific APIs: `androidMain`, `jvmMain`, `iosMain`, `nativeMain`, `jsMain`, `wasmJsMain`, or shared intermediate sets such as `webMain` when already configured.
- Keep package names under `com.yuanjingtech.boot.app.kmp` unless an existing module uses a narrower package.
- For Compose UI components, follow existing module structure under `ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui`.
- Avoid adding platform dependencies to `commonMain`; place them in the narrowest applicable source set.

## Coding Style

- Match existing Kotlin style: Kotlin DSL for Gradle, explicit source-set dependency blocks, and idiomatic Kotlin naming.
- Keep changes minimal and scoped to the requested task.
- Prefer public API stability in published modules; avoid renaming exported symbols without explicit request.
- Do not add license headers or broad formatting-only rewrites unless requested.
- Do not commit build outputs, Gradle caches, generated KSP output, IDE metadata, or local environment files.

## Validation Commands

Run the narrowest relevant command first, then broaden only when needed:

- Full build/check: `./gradlew build`
- Compile/check a module: `./gradlew :<module>:build`
- Android debug app: `./gradlew :androidapp:assembleDebug`
- Desktop app run: `./gradlew :desktopApp:run`
- Compose app web dev run: `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`
- Tests for a module: `./gradlew :<module>:check`

Network access may be required for dependency resolution. If the environment is sandboxed, ask before running commands that download dependencies or write outside the workspace.

## Dependency Notes

- Kotlin, AGP, Compose, KSP, Koin, SQLDelight, and Room versions are tightly coupled. Before changing one, check compatibility across the catalog and affected Gradle plugins.
- KSP configurations are target-specific in multiplatform modules, e.g. `kspAndroid`, `kspJvm`, `kspIosArm64`, `kspIosSimulatorArm64`, `kspJs`, and `kspWasmJs`.
- Web/Wasm database support involves both Gradle dependencies and `sqlite-wasm-worker/worker`; keep them in sync.
- Publishing is configured through `com.vanniktech.maven.publish`; do not change coordinates casually.

## Agent Workflow

- Check `git status --short` before editing and avoid overwriting unrelated user changes.
- Search with `rg`/`rg --files` before adding new patterns or APIs.
- Read nearby module build files before adding dependencies or targets.
- If touching `build-logic`, verify the plugin ID and version catalog alias used by consuming modules.
- If changing UI behavior, check shared Compose code and platform wrappers for assumptions.
- When tests/builds cannot be run, state the exact command that should be run and why it was skipped.


<claude-mem-context>
# Memory Context

# [boot-app-kmp] recent context, 2026-05-16 9:56am GMT+8

Legend: 🎯session 🔴bugfix 🟣feature 🔄refactor ✅change 🔵discovery ⚖️decision
Format: ID TIME TYPE TITLE
Fetch details: get_observations([IDs]) | Search: mem-search skill

Stats: 50 obs (11,606t read) | 444,559t work | 97% savings

### Apr 22, 2026
128 7:47a 🔴 KMP build plugin fails with missing 'webMain' source set
129 " 🔴 KMP build plugin fails with missing 'webMain' source set
130 7:48a 🔄 Removed KMP plugin application from convention plugins
131 7:49a 🔴 KMP build plugin fails with missing 'webMain' source set
132 " 🔴 KMP build plugin fails with missing 'webMain' source set
133 " 🔴 KMP build plugin fails with missing 'webMain' source set
134 7:50a 🔴 KMP build plugin fails with missing 'webMain' source set
135 " 🔴 KMP build plugin fails with missing 'webMain' source set
136 7:56a 🔵 KMP build fails with missing 'webMain' KotlinSourceSet
137 7:57a 🔴 KMP build plugin fails with missing 'webMain' source set
138 " 🔴 KMP build plugin fails with missing 'webMain' source set
139 7:58a 🔴 KMP build plugin fails with missing 'webMain' source set
140 " 🔴 KMP build plugin fails with missing 'webMain' source set
141 7:59a 🔴 KMP build plugin fails with missing 'webMain' source set
142 " 🔴 KMP build plugin fails with missing 'webMain' source set
143 8:00a 🔴 KMP build plugin fails with missing 'webMain' source set
144 " 🔴 KMP build plugin fails with missing 'webMain' source set
145 8:01a 🔴 KMP build plugin fails with missing 'webMain' source set
146 8:02a 🔴 KMP build plugin fails with missing 'webMain' source set
147 " 🔴 KMP build plugin fails with missing 'webMain' source set
148 8:03a 🔴 KMP build plugin fails with missing 'webMain' source set
149 " 🔵 KMP build fails with missing 'webMain' source set
150 8:04a 🔴 KMP build plugin fails with missing 'webMain' source set
151 " 🔴 AGP version downgraded from 9.3.0 to 9.2.0
152 " 🔴 KMP build plugin fails with missing 'webMain' source set
153 8:05a 🔴 KSP version downgrade attempted to resolve build failure
154 " 🔵 Kotlin 2.1.50 multiplatform plugin cannot be resolved
155 " 🔴 KMP build plugin fails with missing 'webMain' source set
156 8:06a 🔴 KMP build plugin fails with missing 'webMain' source set
157 8:07a 🔵 Kotlin 2.1.50 version not available in Maven Central
158 " 🔴 KMP build plugin fails with missing 'webMain' source set
159 8:08a 🔵 KSP version 2.1.21-1.0.28 does not exist in Maven
160 8:09a 🔴 KMP build plugin fails with missing 'webMain' source set
161 3:05p 🔵 Cross-AI Review Infrastructure Detected
162 " 🔵 No .planning Directory Found
163 3:06p 🔵 boot-app-kmp Project Structure Revealed
164 3:15p 🔵 boot-app-kmp is a Kotlin Multiplatform library project
### Apr 29, 2026
287 9:10p 🟣 GitHub Actions workflow for PR build and review
289 9:27p 🟣 GitHub workflow for build & PR review initiated
290 9:58p 🟣 Automated semantic versioning enabled
304 10:19p 🟣 GitHub Actions CI/CD and MiniMax AI Review Workflows Added
312 10:47p 🔵 GitHub Actions YAML syntax error on heredoc nesting
313 10:48p 🔴 GitHub Actions workflow YAML syntax fixed using Python heredoc approach
314 10:49p 🔴 GitHub Actions minimax-pr-fix.yml workflow YAML syntax also fixed
315 10:51p 🔄 GitHub Actions minimax-pr-review.yml simplified to inline Python and base64 encoding
317 10:57p 🔄 GitHub Actions workflows refactored to write Python scripts via intermediate step
### May 16, 2026
529 9:55a ✅ AGENTS.md documentation file initialized
530 " 🔵 boot-app-kmp is a KMP application framework library
531 9:56a ✅ Skeleton loading UI component added
532 " 🟣 AGENTS.md documentation file created with comprehensive project guidance

Access 445k tokens of past work via get_observations([IDs]) or mem-search skill.
</claude-mem-context>