---
title: ":feature:ad 切到 spm4kmp 1.9.4 POC 失败 — Kotlin 2.4.0 cinterop 不能消费 transitive SwiftPM Clang module(issue #325),已回退"
date: "2026-07-09"
category: docs/solutions/runtime-errors/
module: boot-app-kmp/feature/ad
problem_type: cinterop_error
component: spm4kmp-integration
symptoms:
  - "SwiftPackageConfigAppleAdGenerateCInteropDefinitionIosSimulatorArm64 FAILED"
  - "Module map file not found for module: Ad"
  - "Config: ModuleConfig(isFramework=false, name=Ad, alias=null, spmPackageName=null, packageName=, buildDir=, definitionFile=, linkerOpts=[], compilerOpts=[], swiftDependency=null, isCLang=false, customSearchHeaderPath=[])"
  - "Note: Kotlin 2.4.x + cinterop cannot resolve transitive Clang/Objective-C module from SwiftPM product"
root_cause: upstream_library_bug
resolution_type: reverted
severity: high
status: reverted
workaround_in_place_since: "2026-07-07"
related_components:
  - feature/ad/build.gradle.kts
  - feature/ad/src/swift/Ad/
  - gradle/libs.versions.toml
  - gradle.properties
  - docs/solutions/runtime-errors/feature-ad-swiftpm-xcode27-ios-clang-errors-2026-07-09.md
tags:
  - kmp
  - ios
  - cinterop
  - spm4kmp
  - swiftpm
  - xcode27
  - kotlin2.4
  - google-mobile-ads
  - admob
  - feature-ad
  - upstream-issue-325
  - poc
  - reverted
---

# `:feature:ad` 切到 spm4kmp 1.9.4 POC 失败 — 已回退

## Problem

2026-07-09 尝试把 `:feature:ad` 从 JetBrains 官方 `swiftPMDependencies` 切换到 `io.github.frankois944.spmForKmp:1.9.4` 以绕开 Xcode 27 beta clang `unknown argument: -emit-library` 错误。**前两阶段成功,第三阶段(cinterop 生成)命中 spm4kmp 官方 issue #325**。所有改动已 `git checkout --` 回退。

## Symptoms

`./gradlew :feature:ad:linkDebugFrameworkIosSimulatorArm64` 最终失败于 `SwiftPackageConfigAppleAdGenerateCInteropDefinitionIosSimulatorArm64`:

```
> Task :feature:ad:SwiftPackageConfigAppleAdGenerateCInteropDefinitionIosSimulatorArm64 FAILED
Can't generate definition for  Ad
Expected file:
Config: ModuleConfig(isFramework=false, name=Ad, alias=null, spmPackageName=null, packageName=,
                     buildDir=, definitionFile=, linkerOpts=[], compilerOpts=[],
                     swiftDependency=null, isCLang=false, customSearchHeaderPath=[])
Exception: java.lang.IllegalStateException: Module map file not found for module: Ad
```

注意关键字段:
- `isFramework=false` — Ad 被当作 static library 而不是 framework
- `spmPackageName=null`、`packageName=` — 模块元数据没填进 `ModuleConfig`
- `customSearchHeaderPath=[]` — 没找到 transitive ObjC header 路径

## Root Cause

`https://github.com/frankois944/spm4Kmp/issues/325`(2026-07-05 创建,2026-07-08 由 maintainer 关闭)原文:

> After upgrading Kotlin from `2.3.20` to `2.4.0`, cinterop generation fails for a Swift bridge target that depends on a SwiftPM product with a transitive Clang/Objective-C module.
> ...
> `nearbyIos` bridge target → `NearbyConnections` SwiftPM product → `NearbyCoreAdapter` Clang/Objective-C target
> ...
> The generated `nearbyIos_bridge.def` does not include the transitive module map/include path, so Kotlin/Native 2.4.0 cinterop cannot resolve `NearbyCoreAdapter`.

`:feature:ad` 与此场景**精确匹配**:
- `Ad` 桥 target → `GoogleMobileAds` SwiftPM product → GoogleMobileAds 的 ObjC module
- Kotlin 2.4.20-Beta1 cinterop 找不到 transitive module map
- spm4kmp 1.9.4 是当前最新稳定版(2026-06-21 发布),该 bug 未修复

**这是 spm4kmp 自身 bug,不是配置错误**。

## POC 3 阶段进展(都是真实可复现的)

| 阶段 | 任务 | 结果 |
|---|---|---|
| 1. Gradle 配置 | swiftPackageConfig DSL 编译 | ✅ 通过(需要 `import io.github.frankois944.spmForKmp.swiftPackageConfig` 顶层 import) |
| 2. Swift 包编译 | `SwiftPackageConfigAppleAdCompileSwiftPackageIosSimulatorArm64` | ✅ 通过,产出 `libAd.a` + `GoogleMobileAds.framework` + `UserMessagingPlatform.framework` |
| 3. cinterop 生成 | `SwiftPackageConfigAppleAdGenerateCInteropDefinitionIosSimulatorArm64` | ❌ 命中 issue #325 |

### 阶段 1 关键点
- spm4kmp 的 `swiftPackageConfig` 是**顶层 extension function**(`io.github.frankois944.spmForKmp.ExtensionHelperKt`),不是 Project 级的 plugin extension
- `cinteropName` 是 `swiftPackageConfig` 的命名参数,在 `(cinteropName = "Ad")` 中传
- `minIos` 是 `PackageRootDefinitionExtension` 的属性,在 lambda 里设(不是 swiftPackageConfig 的参数)

### 阶段 2 关键点
- spm4kmp 生成 `Package.swift` 用 `type: .static`(不是 `.dynamic`),所以**绕开了 Xcode 27 beta clang unknown argument 错误**
- 但 spm4kmp 默认 `platforms: [.iOS("12.0"),...]`,**Xcode 27 SDK 最低 iOS 15 部署目标**,`IPHONEOS_DEPLOYMENT_TARGET=12.0` 会触发 `error: The iOS Simulator deployment target is set to 12.0, but the range of supported deployment target versions is 15.0 to 27.0.x`
- 修复:`swiftPackageConfig` 块内显式 `minIos = "16.0"` 重写默认 iOS 12

### 阶段 3 关键点(失败)
- SPM 编译产出布局与 spm4kmp 1.9.4 期望布局不匹配
- 实际:`out/Products/Release-iphonesimulator/libAd.a`(静态库)
- 期望:`arm64-apple-ios-simulator/release/libAd.a`(spm4kmp 计算的旧布局)
- workaround symlink 解决 .a 路径,但 `Module map file not found` 仍触发
- 根本不是路径问题,是 cinterop 解析 transitive ObjC module 失败

## 改动清单(已全部回退)

| 文件 | 改动 | 状态 |
|---|---|---|
| `gradle/libs.versions.toml` | + `spm4kmp = "1.9.4"` + plugin alias | 回退 |
| `gradle.properties` | + `kotlin.mpp.enableCInteropCommonization=true` | 回退 |
| `feature/ad/build.gradle.kts` | + plugin alias + import + `swiftPackageConfig { ... }` 块;删除 `swiftPMDependencies` 块 | 回退 |
| `feature/ad/src/swift/Ad/StartYourBridgeHere.swift` | spm4kmp 自动生成的 placeholder(空类) | 删除 |
| `feature/ad/build/spmKmpPlugin/` | spm4kmp 编译缓存 | 删除 |

回退命令:
```bash
git checkout -- gradle/libs.versions.toml gradle.properties feature/ad/build.gradle.kts
rm -rf feature/ad/src/swift feature/ad/build/spmKmpPlugin
```

## What Did NOT Work

| 尝试 | 结果 |
|---|---|
| `swiftPackageConfig(cinteropName = "Ad")` 默认配置 | 命中 iOS 12 部署目标错误 + module map 错误 |
| `swiftPackageConfig(cinteropName = "Ad") { minIos = "16.0" }` 显式最低 iOS | 编译通过,仍命中 module map 错误 |
| symlink `libAd.a` 到 spm4kmp 期望路径 | `Module map file not found` 仍在(根因不是路径) |
| 修改 `cinteropName`(改成 `nativeIosShared` 等 example 风格) | 未尝试(已确认是 spm4kmp 1.9.4 已知 bug,改名字无济于事) |
| 写本地 Swift 桥把 GoogleMobileDs ObjC API 用 `@objcMembers` 暴露 | 未尝试(超过 POC 范围;需手写大量包装代码且 spm4kmp 文档说不一定能解决) |

## Why 回退而不是继续挖

1. **确认是上游 bug**(spm4kmp issue #325,2026-07-08 由 maintainer 关闭)而非配置错误
2. **修复需要 spm4kmp 维护者**改 spm4kmp 内部 cinterop 算法,不是简单配置
3. **维护风险**:社区维护的 spm4kmp + 仍为 Experimental 的 JetBrains swiftPMDependencies + 工具链不兼容 — 三方不确定性叠加
4. **业务影响有限**:JetBrains swiftPMDependencies + 接受 `convertSyntheticImportProjectIntoDefFile*` 失败的"现状"已被 docs/solutions 充分记录,iOS app workflow 走 xcodebuild 不受影响
5. **POC 留下完整记录**:本文件 + `feature-ad-swiftpm-xcode27-ios-clang-errors-2026-07-09.md`,未来重启有完整依据

## Follow-up — 重启检查清单

如果将来满足以下任一条件,可重启 SPM 路径切换:

### 条件 A:spm4kmp 修复 issue #325
- 检查:`https://github.com/frankois944/spm4Kmp/issues/325` 状态 + 后续 release notes
- 动作:重新跑本 POC 3 阶段验证
- 验证任务:SwiftPackageConfigAppleAdGenerateCInteropDefinition* 应输出 `Config: ModuleConfig(... spmPackageName="...", packageName="...", ...)` 而不是空

### 条件 B:JetBrains 修复 swiftPMDependencies
- 检查:Kotlin release notes 中 swiftPMDependencies / dynamic dylib 子包相关
- 动作:从 spm4kmp 切回 swiftPMDependencies(改 build.gradle.kts 第 24-44 行即可)
- 验证任务:convertSyntheticImportProjectIntoDefFile* 不再触发 clang unknown argument

### 条件 C:Xcode 27 GA
- 检查:`-emit-library` 是否在 Xcode 27 GA 中被 clang 接受
- 动作:与 B 类似
- 优先级低于 A/B(因为 GA 时间不可控)

### 条件 D:GoogleMobileAds 提供 native KMP SDK
- 检查:`https://developers.google.com/admob` 发布说明
- 动作:从 `:feature:ad` 移除 SPM 集成,改用直接 Maven 依赖
- 最干净的修复路径

## Notes

- 本次 POC 总耗时约 30 分钟,失败点精确,记录完整
- 反编译 `plugin-1.9.4.jar` 确认:
  - `swiftPackageConfig` 是 `ExtensionHelperKt` 的顶层 extension function(不是 Project plugin extension)
  - `GenerateCInteropDefinitionTask` 在 `out/Products/<config>/` 下查找 `module.modulemap` / `include/module.modulemap` / `Modules/module.modulemap` / `Modules/include/module.modulemap` 之一
  - 但 spm4kmp 1.9.4 计算 `compiledBinary` 路径在 `arm64-apple-ios-simulator/release/`(与 Xcode 27 实际产物布局不匹配)
- 关联:`docs/solutions/runtime-errors/feature-ad-swiftpm-xcode27-ios-clang-errors-2026-07-09.md` 记录回退前的旧 swiftPMDependencies 错误
- 关联:`memory/feature-ad-xcode27-swiftpm.md` 已更新反映本 POC 结果
