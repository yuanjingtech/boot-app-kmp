# Residual Review Findings
## Branch: fix/sqlite-bundled-kmp-resolution
## Plan: docs/plans/2026-04-23-002-fix-sqlite-bundled-plan.md
## Review run: .context/compound-engineering/ce-code-review/20260423-215418-c83e6317/

---

## Residual Actionable Work

### P1 — iosMain missing BootDatabaseFactory.actual
- **Severity**: P1
- **File**: `shared/src/iosMain/kotlin/com/yuanjingtech/boot/app/kmp/data/room3/` (missing)
- **Title**: iosMain has no `BootDatabaseFactory.ios.kt`
- **Description**: The `commonMain` `expect fun createBootDatabase()` has actuals for `jvmMain`, `androidMain`, `jsMain`, `wasmJsMain`, `nativeMain`, but `iosMain` (a distinct KMP target in this project) has no corresponding actual. Gradle may fail to link iOS binaries.
- **autofix_class**: `manual` — create `BootDatabaseFactory.ios.kt` mirroring `nativeMain` pattern. Verify with `./gradlew :shared:compileKotlinIosArm64`.

### P1 — iOS compilation verification not performed
- **Severity**: P1
- **Description**: Plan R1 requires all platforms to compile. `./gradlew :shared:compileKotlinIosArm64` was not run.
- **autofix_class**: `manual` — run iOS compile task and fix any errors.

### P2 — jvmMain/androidMain byte-for-byte identical
- **Severity**: P2
- **File**: `shared/src/jvmMain/.../BootDatabaseFactory.jvm.kt` and `shared/src/androidMain/.../BootDatabaseFactory.android.kt`
- **Title**: Code duplication between JVM and Android actuals
- **Description**: Both files are identical. Any future DB config change must be applied to both in lockstep.
- **autofix_class**: `gated_auto` — consolidation requires architectural decision.

### P2 — No tests for `createBootDatabase()` on any platform
- **Severity**: P2
- **File**: `shared/src/commonTest/` (missing)
- **Title**: Zero test coverage for `BootDatabaseFactory`
- **Description**: The `expect`/`actual` pair has 6 implementations with divergent behavior. No test exercises any path.
- **autofix_class**: `manual` — add platform-specific tests or `commonTest` with `expect`/`actual`.

### P2 — No Koin DI integration test for `BootDatabase`
- **Severity**: P2
- **File**: `shared/src/commonMain/.../BootDataModule.kt`
- **Title**: `bootDataModule` Koin wiring untested
- **Description**: `single<BootDatabase> { createBootDatabase() }` is untested.
- **autofix_class**: `manual` — add Koin integration test.

### P3 — JS/WASM compile verification not in PR diff
- **Severity**: P3
- **Description**: `./gradlew :shared:compileKotlinJs :shared:compileKotlinWasmJs` should be verified.
- **autofix_class**: `manual`
