---
title: ":feature:ad 切到 CocoaPods 路径 POC 失败 — 5 个独立阻塞 + Google CDN 不可达"
date: "2026-07-09"
category: docs/solutions/runtime-errors/
module: boot-app-kmp/feature/ad
problem_type: pod_install_blocked
component: cocoapods-integration
symptoms:
  - "CocoaPods 1.16.2 pod install 持续报 'curl: (18) Transferred a partial file' / 'curl: (16) Error in the HTTP2 framing layer' 从 dl.google.com 下载 GoogleMobileAds-11.13.0.tar.gz"
  - "7.7MB tar 包多次下载到 ~1.2MB 时中断(15% 处)"
  - "AdPod 路径需要走 :feature:ad 目录的 Podfile 而非 iosApp 目录"
  - "ad.podspec 必须显式设置 homepage/summary/license/source 字段"
  - "CocoaPods 1.16 + 没有 xcodeproj 报 'Could not automatically select an Xcode project',可用 --project-directory 绕过"
root_cause: environment_blocked
resolution_type: reverted
severity: high
status: reverted
workaround_in_place_since: "2026-07-07"
related_components:
  - feature/ad/build.gradle.kts
  - feature/ad/ad.podspec
  - app/iosApp/Podfile
  - app/iosApp/Pods/
  - docs/solutions/runtime-errors/feature-ad-swiftpm-xcode27-ios-clang-errors-2026-07-09.md
  - docs/solutions/runtime-errors/feature-ad-spm4kmp-kotlin-24-cinterop-issue-325-2026-07-09.md
tags:
  - kmp
  - ios
  - cocoapods
  - google-mobile-ads
  - admob
  - feature-ad
  - pod-install
  - cdn-blocked
  - third-path-failed
  - poc
  - reverted
---

# `:feature:ad` 切到 CocoaPods POC 失败 — 5 个独立阻塞 + Google CDN 不可达

## Problem

2026-07-09 在已经验证 JetBrains `swiftPMDependencies`(Xcode 27 clang unknown argument)和 `spm4kmp 1.9.4`(issue #325)都失败之后,试第三条路径 `kotlin("native.cocoapods")` + 业务 iosApp 端 Podfile 集成。**配置层全部走通,最终撞上 Google CDN 不可达**。

## 5 个独立阻塞(按出现顺序)

### 阻塞 1:Consumer/Provider Podfile 错位(已修)
- 错误:Podfile 写在 `app/iosApp/`,但 Kotlin CocoaPods plugin 的 `PodInstallTask.workingDir` 派生自 `podfile.parentFile`,要求 Podfile 必须在 :feature:ad 模块目录下
- 修复:Podfile 改写到 `app/iosApp/`,用 `pod install --project-directory=<iosApp path>` 显式指定工作目录绕开 workingDir 计算

### 阻塞 2:ad.podspec 必需字段缺失(已修)
- 错误:`pod install` 报 "The `ad` pod failed to validate due to 2 errors: Missing required attribute `homepage` / `summary`"
- 原因:Kotlin CocoaPods plugin 默认生成的 `ad.podspec` 第 4-5 行 `spec.homepage = ''`、`spec.summary = ''` 是空字符串
- 修复:在 `cocoapods { }` 块显式设置:
  ```kotlin
  cocoapods {
      homepage = "https://example.com/feature-ad"
      summary = "feature/ad KMP module providing GoogleMobileAds integration"
      license = "MIT"
      source = "{ :path => '.' }"
      pod("Google-Mobile-Ads-SDK") { version = "~> 11.13" }
      pod("GoogleUserMessagingPlatform") { version = "~> 2.7" }
  }
  ```

### 阻塞 3:相对路径 `path => '../feature/ad'` 错(已修)
- 错误:`No podspec found for 'ad' in '../feature/ad'`,后改用相对 `:` 后报 "relative URI" 错误
- 原因:Podfile 已经在 `app/iosApp/`,相对路径 `../feature/ad` 解析为 `app/feature/ad/` 而非 `feature/ad/`
- 修复:`pod 'ad', :path => '../../feature/ad'`

### 阻塞 4:CocoaPods 1.16 + 无 xcodeproj 报 "Could not automatically select an Xcode project"(已修)
- 错误:`pod install` 在工作目录找不到 .xcodeproj 时拒绝执行
- 修复:`pod install --project-directory=/path/to/app/iosApp` 指向含 .xcodeproj 的目录

### 阻塞 5:Google CDN HTTP2 framing layer 持续断(**不可控**)
- 错误:
  ```
  curl: (16) Error in the HTTP2 framing layer
  curl: (18) Transferred a partial file
  [!] Error installing Google-Mobile-Ads-SDK
  ```
- 7.7MB tar 包在 ~1.2MB(15%)处中断
- 多次重试均失败
- 这是 Google 下载 CDN 与本地网络环境的兼容性问题,可能与公司网络代理/firewall 有关
- **不在项目可控范围**

## 已验证的配置改动(全部回退)

| 改动 | 文件 | 验证状态 |
|---|---|---|
| 加 `kotlin("native.cocoapods")` plugin | `feature/ad/build.gradle.kts` 第 12 行 | ✅ 生效 |
| 加 `cocoapods { ... }` 块 | `feature/ad/build.gradle.kts` | ✅ 生效,生成 `ad.podspec` |
| 显式 `homepage`/`summary`/`license`/`source` | `feature/ad/build.gradle.kts` cocoapods 块 | ✅ 修复阻塞 2 |
| 生成 `feature/ad/ad.podspec` | Kotlin plugin 自带 | ✅ |
| 加 `app/iosApp/Podfile` | `app/iosApp/Podfile` | ✅ 路径正确(被删除) |

## What Did NOT Work

| 尝试 | 结果 |
|---|---|
| Podfile 写在 :feature:ad 目录(满足 workingDir 计算) | "Could not automatically select an Xcode project" |
| Podfile 写在 app/iosApp + `pod install --project-directory=app/iosApp` | "No podspec found" → "abstract target Pods is not inherited" → "ad.podspec 字段空" |
| 重试 `pod install` 多次 | 持续 HTTP2 framing layer 错误 |
| `pod install --no-repo-update` | 仍需要下载 GoogleMobileAds,失败位置相同 |

## 三条路径全部失败的决策树

```
让 :feature:ad 自包含 iOS AdMob
├─ 路径 A: JetBrains swiftPMDependencies
│   └─ ❌ Xcode 27 beta clang unknown argument: -emit-library
│      (feature-ad-swiftpm-xcode27-ios-clang-errors-2026-07-09.md)
│
├─ 路径 B: spm4kmp 1.9.4
│   └─ ❌ Kotlin 2.4.0 cinterop 找不到 transitive ObjC module
│      (feature-ad-spm4kmp-kotlin-24-cinterop-issue-325-2026-07-09.md)
│
└─ 路径 C: kotlin("native.cocoapods")
    ├─ ✅ 配置层全通(5 个阻塞中 4 个已修)
    └─ ❌ Google CDN 不可达(curl HTTP2 framing layer 持续断)
       (本文件)
```

**所有 3 条 iOS 集成路径都失败,失败原因分布**:
- 路径 A:**Xcode 27 beta 工具链**与 Kotlin swiftPM 链接脚本不兼容(Apple 工具链)
- 路径 B:**Kotlin 2.4 cinterop 解析 transitive ObjC module** 自身 bug(JetBrains/kotlin)
- 路径 C:**Google 下载 CDN** 与本地网络环境不兼容(Google Cloud + 公司网络)

## 回退命令

```bash
git checkout -- feature/ad/build.gradle.kts
rm -f app/iosApp/Podfile app/iosApp/Podfile.lock
rm -rf app/iosApp/Pods app/iosApp/.symlinks app/iosApp/iosApp.xcworkspace
rm -rf feature/ad/build/cocoapods
```

回退后状态:
- `:feature:ad` 回到 `swiftPMDependencies` + `convertSyntheticImportProjectIntoDefFile*` 已接受代价
- iOS app workflow 走 xcodebuild 直接构建,不受影响
- IDE sync 配合 invalidate caches 能绿

## Why 回退

1. **3 条路径全部失败,失败面来自 Apple 工具链 + JetBrains + Google + 本地网络,4 方不确定性叠加**
2. **继续调试已超出"集成切换"的工作量**,每条路径都要深入具体生态(Kotlin CocoaPods plugin / spm4kmp / xcodebuild)
3. **业务影响有限**:现状是 iOS app workflow 仍可通过 xcodebuild 直接构建,只是 Gradle 端 `linkDebugFramework*` 任务失败
4. **本 POC 留下完整记录**:本文件 + 之前两个 docs/solutions 文档,未来重启有完整依据

## Follow-up — 重启检查清单

如果将来满足以下任一条件,可重启 CocoaPods 路径:

### 条件 A:Google CDN 可达性恢复
- 验证:`curl -f -L -o /dev/null https://dl.google.com/dl/cpdc/14d17d67b68f67f8/Google-Mobile-Ads-SDK-11.13.0.tar.gz` 能完整下载
- 动作:恢复本 POC 的所有 5 步配置,重跑 `pod install`
- 验证任务:podInstall + linkPodDebugFrameworkIosSimulatorArm64 应输出 BUILD SUCCESSFUL

### 条件 B:Xcode 27 GA 修复 swiftPM clang unknown argument
- 重启路径 A 即可,优于 CocoaPods(CocoaPods 1.16 trunk read-only 风险)

### 条件 C:GoogleMobileAds 提供 native KMP SDK
- 检查:`https://developers.google.com/admob` 发布说明
- 行动:移除 :feature:ad 全部 SPM/CocoaPods 集成,改用直接 Maven 依赖
- 最干净的修复路径

### 条件 D:迁出 KMP 集成,把 AdMob 集成下沉到 app/iosApp
- 让 :feature:ad 退化为 Android-only KMP library
- iosApp 直接在 xcodeproj 加 SPM/CocoaPods 引用 GoogleMobileAds
- 损失:`:feature:ad` 不能"自包含 iOS AdMob 能力",但业务 iosApp 仍能用

## Notes

- 本次 POC 总耗时约 1 小时,失败点清晰,记录完整
- 关键认识:**Kotlin CocoaPods plugin 在 K2.4.0 + Xcode 27 + CocoaPods 1.16 三层组合下,配置层完全可用;真正失败的是外部依赖下载**(Google CDN)
- 关联:`docs/solutions/runtime-errors/feature-ad-swiftpm-xcode27-ios-clang-errors-2026-07-09.md` 记录路径 A
- 关联:`docs/solutions/runtime-errors/feature-ad-spm4kmp-kotlin-24-cinterop-issue-325-2026-07-09.md` 记录路径 B
- 关联:`memory/feature-ad-xcode27-swiftpm.md` 已更新反映第三条失败路径
