---
title: ":feature:ad swiftPMDependencies 生成 dynamic dylib 触发 Xcode 27 beta clang 链接错误(已知/已接受)"
date: "2026-07-09"
category: docs/solutions/runtime-errors/
module: boot-app-kmp/feature/ad
problem_type: link_error
component: swiftpm-integration
symptoms:
  - "./gradlew :feature:ad:convertSyntheticImportProjectIntoDefFileIphonesimulator BUILD FAILED"
  - "clang: error: unknown argument: '-emit-library'"
  - "clang: error: unknown argument: '-sdk'"
  - "clang: error: unknown argument: '-Xclang-linker'"
  - "clang: error: no such file or directory: '@rpath/KotlinMultiplatformLinkedPackageDylib.framework/KotlinMultiplatformLinkedPackageDylib'"
  - "clang: error: invalid argument '-install_name -Xclang-linker' only allowed with '-dynamiclib'"
  - "Build settings from command line: CC=.../clangDump.sh LD=.../ldDump.sh(Kotlin swiftImportClangDump wrapper)"
  - "note: Explicit modules is enabled but the compiler was not recognized; disable explicit modules with CLANG_ENABLE_EXPLICIT_MODULES=NO"
  - "Likely identical failure on Iphoneos variant"
root_cause: toolchain_incompatibility
resolution_type: accepted_limitation
severity: medium
status: work_in_progress
last_verified: "2026-07-15"
verification: "Xcode 27.0 (27A5218g) GA 仍复现 -emit-library 错误,GA 未修复"
workaround_in_place_since: "2026-07-07"
cleaned_up_since: "2026-07-15"
cleanup_action: "删除 feature/ad/build.gradle.kts 中 convertSyntheticImportProjectIntoDefFile workaround block(2026-07-07 已删但注释/逻辑残留至 2026-07-15)"
related_components:
  - feature/ad/build.gradle.kts
  - feature/ad/build/kotlin/swiftImportDd/dd_iphonesimulator/Build/Intermediates.noindex/KotlinMultiplatformLinkedPackageDylib.build/.../KotlinMultiplatformLinkedPackageDylib.LinkFileList
  - feature/ad/build/kotlin/swiftImport/subpackages/KotlinMultiplatformLinkedPackageDylib/Package.swift
  - app/iosApp
tags:
  - kmp
  - ios
  - link-error
  - swiftpm
  - xcode27
  - kotlin2.4
  - google-mobile-ads
  - admob
  - feature-ad
  - known-limitation
---

# `:feature:ad` `swiftPMDependencies` 与 Xcode 27 beta 的链接不兼容(已接受)

## Problem

Kotlin Multiplatform 2.4.0 的 `swiftPMDependencies { ... }` 在 Xcode 27 beta 上调用 `xcodebuild` 链接 `KotlinMultiplatformLinkedPackageDylib` 时,Xcode 27 自带的 clang 不识别 Kotlin 生成的链接参数,导致 `:feature:ad:convertSyntheticImportProjectIntoDefFileIphonesimulator`(以及对应的 Iphoneos 变体)BUILD FAILED。

错误信息(摘自 `xcodebuild` 输出):

```
KotlinMultiplatformLinkedPackageDylib-product: clang: error: unknown argument: '-emit-library'
KotlinMultiplatformLinkedPackageDylib-product: clang: error: unknown argument: '-sdk'
KotlinMultiplatformLinkedPackageDylib-product: clang: error: unknown argument: '-Xclang-linker'
KotlinMultiplatformLinkedPackageDylib-product: clang: error: no such file or directory: '@rpath/KotlinMultiplatformLinkedPackageDylib.framework/KotlinMultiplatformLinkedPackageDylib'
KotlinMultiplatformLinkedPackageDylib-product: clang: error: invalid argument '-install_name -Xclang-linker' only allowed with '-dynamiclib'
```

并伴随:

```
note: Explicit modules is enabled but the compiler was not recognized;
disable explicit modules with CLANG_ENABLE_EXPLICIT_MODULES=NO, or use
C_COMPILER_LAUNCHER with CLANG_ENABLE_EXPLICIT_MODULES_WITH_COMPILER_LAUNCHER=YES
if using a compatible launcher
```

## Symptoms

- `./gradlew :feature:ad:convertSyntheticImportProjectIntoDefFileIphonesimulator` BUILD FAILED
- 同样的失败预期会出现在 `convertSyntheticImportProjectIntoDefFileIphoneos` 上
- 在 LinkFileList 中看到动态 dylib 产物,例如:
  ```
  .../Build/Products/Debug-iphonesimulator/KotlinMultiplatformLinkedPackageDylib.o
  .../Build/Products/Debug-iphonesimulator/GoogleMobileAdsTarget.o
  .../Build/Products/Debug-iphonesimulator/UserMessagingPlatformTarget.o
  ```
  (注意第一行末尾是 `KotlinMultiplatformLinkedPackageDylib.o`,即 dylib 形态而非静态 framework)
- `xcodebuild` 命令行被 Kotlin 注入为:
  ```
  CC=.../swiftImportClangDump/iphonesimulator/clangDump.sh
  LD=.../swiftImportClangDump/iphonesimulator/ldDump.sh
  ```
  这两个 shell wrapper 本应透明转发参数,但实际转发到的就是 Xcode 27 beta 的 clang。
- iOS app 通过 Xcode / `xcodebuild` 直接构建的 workflow **不受影响**(下文详述)

## Root Cause

`:feature:ad/build.gradle.kts` 通过 `swiftPMDependencies { ... }` 引入 `GoogleMobileAds` 11.13.0。Kotlin 2.4.0 的 `swiftPMDependencies` 会在 `build/kotlin/swiftImport/subpackages/KotlinMultiplatformLinkedPackageDylib/Package.swift` 里**额外**生成一个 `KotlinMultiplatformLinkedPackageDylib` 子包,其 target `KotlinMultiplatformLinkedPackageDylib` 走 `type: .dynamic`(即使 host 模块本身 `isStatic = true` 也无法改变这个子包)。

随后 Gradle 调起 `xcodebuild` 让 `KotlinMultiplatformLinkedPackageDylib-product` 链接成 `.framework`,而该链接命令原本假设使用支持 `-emit-library` / `-Xclang-linker` / `-install_name -Xclang-linker` 等 GNU-style 包装参数的 linker 包装脚本 —— 这些参数在 Xcode 27 beta 的 clang 看来是 unknown argument,且 `-install_name -Xclang-linker` 必须在 `-dynamiclib` 模式下使用,直接抛错。

简单说:**这是一个工具链不兼容问题**(Kotlin 2.4.0 的 swiftPM 链接脚本假设的 clang 行为 vs. Xcode 27 beta 的 clang 实际行为),不是项目代码问题。

## Workaround Applied — 已删除

2026-07-07 之前的临时方案是:在 `convertSyntheticImportProjectIntoDefFile*` 任务执行时把生成的 `Package.swift` 中 `type: .dynamic` 反向改写为 `type: .static`,让 `xcodebuild` 改走 `-r`(relocatable object link)而不是 `-emit-library` 链 dylib,从而绕开 Xcode 27 beta 不识别的链接参数。

**该 workaround 已于 2026-07-07 被彻底删除**,原因:
- 它虽然让模拟器变体能 BUILD SUCCESSFUL,但反向修改引入了新的二级错误 `XcodebuildDefFileWorkAction` 的 "List is empty"(`KotlinMultiplatformLinkedPackage-product` 找不到 entry)
- 维护成本高(每次 Kotlin 重新生成 `Package.swift` 都要再 hack)
- 真正的修复需要等 Kotlin 升级 `swiftPMDependencies` 实现或 Xcode 27 GA

详见 git 历史及 `feature-ad-xcode27-swiftpm` 内存记录。

## Current Status — 已接受代价

`:feature:ad:convertSyntheticImportProjectIntoDefFileIphoneos` 与 `Iphonesimulator` 任务在 `linkDebugFramework*` 阶段**仍会失败**,这是**已记录并接受的代价**,不是新 bug。

### 什么能正常工作

- `./gradlew :feature:ad:help` / `tasks` — Gradle 配置阶段正常
- `./gradlew :feature:ad:build` 跑 Android/JVM/JS/WASM 产物 — 正常
- IDE sync(配合 invalidate caches) — 正常
- iOS app workflow(`app/iosApp`)通过 Xcode / `xcodebuild` 直接构建 — 正常,AdMob framework 仍能正常产出、签名并打包进 ipa
- `compileKotlinIosArm64` / `compileKotlinIosSimulatorArm64`(K/N 编译产物 klib) — 正常

### 什么会失败

- `./gradlew :feature:ad:linkDebugFrameworkIphoneos` / `linkDebugFrameworkIphonesimulator` 及其依赖 `convertSyntheticImportProjectIntoDefFile*` 任务 — 失败并产生本文记录的错误

## What Did NOT Work

| 尝试 | 结果 |
|---|---|
| Gradle 端加 `swiftPMDependencies { iosMinimumDeploymentTarget = "16.0" }` | 不能影响 dylib 子包生成方式 |
| 调整 `iosTarget.binaries.framework { isStatic = true }` | 仅影响 host framework;`KotlinMultiplatformLinkedPackageDylib` 子包仍走 `.dynamic` |
| 删除 `swiftPMDependencies` 块让 iOS app 通过 CocoaPods 引入 GoogleMobileAds | 工作量大,回归 `kotlin-tooling-cocoapods-spm-migration` 路径,需要单独决策 |
| 给 `xcodebuild` 加 `CLANG_ENABLE_EXPLICIT_MODULES=NO` | 仅抑制 explicit modules 提示,无法解决 unknown argument |

## How to Apply / 遇到此错误时怎么办

1. **确认是已知问题**:把报错信息和本文件 `## Symptoms` 段对一下,完全一致即可不再深入排查
2. **不要重新加回 2026-07-07 删除的 workaround**(已确认产生更麻烦的二级错误)
3. **IDE sync**:在 Android Studio / IntelliJ 中 File → Invalidate Caches → Restart,确认 sync 在 `convertSyntheticImportProjectIntoDefFile*` 任务之外的项目范围是绿的;如果 sync 阻塞,可在 Gradle 面板里 `:feature:ad` 上右键 → `Unload` 临时跳过同步(代价:本模块编辑体验下降)
4. **iOS app 构建**:直接走 `xcodebuild` 或 Xcode IDE 打开 `app/iosApp/iosApp.xcodeproj`,不要依赖 Gradle 的 iOS framework 任务
5. **如果阻塞发布**:评估以下替代方案(目前**未采用**)
   - 临时把 `feature/ad/build.gradle.kts` 的 `swiftPMDependencies { ... }` 块注释掉 → iOS app 需自行管理 GoogleMobileAds 依赖(CocoaPods / SwiftPM 直引)
   - 升级 Kotlin 到 `swiftPMDependencies` 已修复的版本(待官方 release)
   - 等 Xcode 27 GA(可能也修复 unknown argument 行为)

## Follow-up — Long-term Solution

1. 跟踪 JetBrains Kotlin issue:`swiftPMDependencies` 生成的 `KotlinMultiplatformLinkedPackageDylib` 子包应能通过配置切换 `static` / `dynamic`,或默认生成静态 + 静态 link 流程
2. 跟踪 Xcode 27 GA release notes:unknown argument `-emit-library` 是否是 beta bug
3. 当上游修复后:
   - 不需要重新引入 `Package.swift` 反向 hack
   - 跑 `./gradlew :feature:ad:linkDebugFrameworkIphonesimulator` 验证 BUILD SUCCESSFUL
   - 删除本文档(将"已接受代价"恢复为"正常")

## Notes

- 该问题与 [[backdrop-ios-colormatrix-irlinkageerror-2026-07-03]] 同属"工具链版本不兼容 → 报错 → 接受绕行"模式,但根因不同:backdrop 是 skia cinterop 桥缺失,本案是 Xcode 27 beta clang 与 Kotlin 链接脚本的接口不匹配
- 临时方案(2026-07-07 之前)和最终接受现状(2026-07-07 之后)的关键变化是:不再 hack Kotlin 生成的 `Package.swift`,改为接受 `convertSyntheticImportProjectIntoDefFile*` 失败,代价是 Gradle 端无法用一条命令产出 iOS framework,iOS app workflow 必须走 Xcode
