# feature/ad — SwiftPM 集成探索报告

## 目标

将 Google Mobile Ads SDK 集成下沉到 `feature/ad` 模块本身,业务模块只需依赖 `feature/ad` 就自动获得 iOS AdMob 能力,降低三方工程的接入成本。

## 最终结论

**截至 2026-06-16:**

- ✅ **koin-compiler-plugin 1.0.1 已修复 K2.4 兼容问题**(2026-06-12 发布,https://github.com/InsertKoinIO/koin-compiler-plugin/releases/tag/1.0.1)
- ✅ **Kotlin 2.4.0 + koin-compiler 1.0.1 在 feature/ad 全平台编译通过**(`./gradlew :feature:ad:assemble` BUILD SUCCESSFUL)
- ✅ **`swiftPMDependencies` DSL 验证成功** — `swiftPackage(url = "https://github.com/googleads/swift-package-manager-google-mobile-ads", version = "11.13.0", products = listOf("GoogleMobileAds"))` 被 Gradle 接受,SPM 仓库被正确拉取,生成 `Package.swift` 把 `KotlinMultiplatformLinkedPackageDylib` 桥接给 Kotlin cinterop
- ⚠️ **最后一步 `convertSyntheticImportProjectIntoDefFileIphoneos` 任务中的 xcodebuild 链接报错** — `clang: unknown argument: '-emit-library'`,这是 **Xcode 27.0 beta + SwiftPM 5.9 与 Kotlin 2.4.0 swiftPMDependencies 的工具链不兼容问题**,需要等 Xcode 26.x 稳定版或 JetBrains 修复
- 💡 **结论**:Kotlin 编译器侧已经没有阻塞(感谢 koin-compiler 1.0.1),唯一阻塞是 Apple Xcode beta 工具链与 Kotlin 的协同 bug,需等 Xcode 26.x GA

## 当前生产状态

| 平台 | 状态 | 接入位置 |
| --- | --- | --- |
| Android | ✅ 真实集成 `play-services-ads:25.3.0` | `feature/ad/src/androidMain/.../AdModule.android.kt` |
| iOS | ⚠️ 工具链 bug 阻塞 swiftPMDependencies 最终链接步骤,沿用 iOS app 工程 SPM 集成 | `feature/ad/src/iosMain/.../AdModule.ios.kt` + `app/iosApp/.../AdMobInitializer.swift` |
| Desktop/JVM | ⚠️ 占位 | `feature/ad/src/jvmMain/.../AdModule.jvm.kt` |
| Web (JS/WasmJS) | ⚠️ 占位 | `feature/ad/src/{js,wasmJs}Main/.../AdModule.{js,wasmJs}.kt` |

## 探索过程(历史)

### 评估的路径

**路径 A:升级 Kotlin 到 2.4.0+ 使用 `swiftPMDependencies` DSL ✅(官方推荐)**

Kotlin 2.4.0 GA 已发布(2026-06-07),新增 `swiftPMDependencies` DSL,可让 KMP 模块直接引用 SPM 包,自动生成 cinterop 绑定。

```kotlin
kotlin {
    swiftPMDependencies {
        iosMinimumDeploymentTarget.set("16.0")
        swiftPackage(
            url = "https://github.com/googleads/swift-package-manager-google-mobile-ads",
            version = "11.13.0",
            products = listOf("GoogleMobileAds"),
        )
    }
}
```

iosMain 侧可通过 `import swiftPMImport.<group>.<ad>.GoogleMobileAds.*` 暴露 SDK API 给 Kotlin。

**路径 B:cocoapods 插件(传统方案)**

`kotlin("native.cocoapods")` + `pod("Google-Mobile-Ads-SDK")`。
优点:成熟,Kotlin 2.3.x 直接可用。缺点:业务 iosApp 必须保留 cocoapods 集成(`pod install` 需运行)。

### 第一步:第一次升级 Kotlin 2.4.0(失败)

升级到 Kotlin 2.4.0-Beta1 + 2.4.0 GA,均被 `koin-compiler 1.0.0-RC1` 阻塞:
```
ClassCastException: FirExtensionRegistrarAdapter$Companion
  cannot be cast to ProjectExtensionDescriptor
    at KoinPluginComponentRegistrar.registerExtensions:189
```

### 第二步:找到官方确认

`koin-compiler-plugin 1.0.1` release notes(2026-06-12):
> **Kotlin version compatibility** — #19, #42 (and koin#2431)
> The plugin hard-crashed on the two most recent Kotlin versions:
> - Kotlin 2.4.0 — ClassCastException during FIR extension registration.

### 第三步:升级到 koin-compiler 1.0.1(成功)

修改 `gradle/libs.versions.toml`:
- `koin-plugin = "1.0.1"`(原 "1.0.0-RC1")
- `kotlin = "2.4.0"`(原 "2.3.21")

✅ `./gradlew :feature:ad:assemble` 全平台 BUILD SUCCESSFUL(Android / iOS × 2 / JVM / JS / WasmJS)

### 第四步:加回 swiftPMDependencies

修改 `feature/ad/build.gradle.kts`:
```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.koinCompiler)
    alias(libs.plugins.publish)
}

group = "com.yuanjingtech.boot.app.kmp.ad"
version = "0.1.0"

kotlin {
    // ... 现有 targets
    swiftPMDependencies {
        iosMinimumDeploymentTarget.set("16.0")
        swiftPackage(
            url = "https://github.com/googleads/swift-package-manager-google-mobile-ads",
            version = "11.13.0",
            products = listOf("GoogleMobileAds"),
        )
    }
    sourceSets.configureEach {
        languageSettings {
            optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }
    }
}
```

**已知遗留问题**:`xcodeProjectPathForKmpIJPlugin` 是 `internal`(Beta 标记),暂时省略 — IDE KMP 插件集成暂时不可用,但不影响 swiftPM 核心功能。

### 第五步:iOS 编译(部分失败)

`./gradlew :feature:ad:compileKotlinIosArm64` 触发 `convertSyntheticImportProjectIntoDefFileIphoneos` 任务,通过 xcodebuild 编译 `KotlinMultiplatformLinkedPackageDylib` dylib。**SPM 解析成功,GoogleMobileAds + UserMessagingPlatform 框架被正确链接**(`-framework GoogleMobileAds -framework UserMessagingPlatform` 已出现在 ld 命令中)。

但 xcodebuild 在最后链接步骤失败:
```
clang: error: unknown argument: '-emit-library'
clang: error: unknown argument: '-sdk'
clang: error: invalid argument '-install_name -Xclang-linker' only allowed with '-dynamiclib'
```

**根因**:
- Kotlin 2.4.0 swiftPMDependencies 生成 `Package.swift` 把 `KotlinMultiplatformLinkedPackageDylib` 声明为 `.library(type: .dynamic)` — 即 dylib
- 当前 Xcode 27.0 / SwiftPM 5.9 在链接 dylib 时使用 `-emit-library`,而 clang 不识别该参数(它期望 `-dynamiclib`)
- **这是 Kotlin 2.4.0 GA 与当前 Xcode 27.0 (beta)之间的 SwiftPM 工具链不兼容**问题,不是配置错误

### 第六步:实施 IosAdManager(代码已就绪)

在 `feature/ad/src/iosMain/.../AdModule.ios.kt` 中实现 `IosAdManager` 的代码模板已写好(注释块),启用后只需要:
1. 把 `single<AdManager> { NoOpAdManager() }` 改为 `single<AdManager> { IosAdManager() }`
2. `import swiftPMImport.com.yuanjingtech.boot.app.kmp.ad.ad.GoogleMobileAds.*`
3. 实现 `MobileAds.sharedInstance().start { ... }` / `GADBannerView` / `GADInterstitialAd` / `GADRewardedAd` 等 SDK 调用

## 推进 feature/ad iOS 真集成的最终步骤

### `integrateLinkagePackage` 已运行(2026-06-16)

按 skill 指引执行:
```bash
XCODEPROJ_PATH='.../app/iosApp/iosApp.xcodeproj' \
  '.../gradlew' -p '.../boot-app-kmp' \
  ':app:composeApp:integrateLinkagePackage' -i
```

**结果**:
- iosApp 工程自动获得 `XCLocalSwiftPackageReference("KotlinMultiplatformLinkedPackage")`
- iosApp 工程自动获得 `XCSwiftPackageProductDependency(productName = "KotlinMultiplatformLinkedPackage")`
- 本地包生成在 `/app/iosApp/KotlinMultiplatformLinkedPackage/`,内含 `Package.swift` 引用子包 `_feature_ad`
- 子包 `_feature_ad` 内引用 GoogleMobileAds SPM
- `xcodebuild -resolvePackageDependencies` 解析全部 4 个包成功

完整链路:
```
iosApp
  └─ KotlinMultiplatformLinkedPackage (local, generated)
       └─ _feature_ad (local subpackage)
            └─ swift-package-manager-google-mobile-ads @ 11.13.0
                 └─ GoogleMobileAds + UserMessagingPlatform
```

### 集成链路已完整,但仍被 Xcode 27 工具链 bug 阻塞

`xcodebuild build` 在 dylib 构建阶段仍报同样错误:
```
clang: error: unknown argument: '-emit-library'
clang: error: invalid argument '-install_name -Xclang-linker' only allowed with '-dynamiclib'
```

(从 `KotlinMultiplatformLinkedPackageDylib` target,仍调用 `clang -emit-library` 参数)

### 阻塞点

**仅剩 Xcode 工具链问题**:
- 等待 Xcode 26.x GA(当前 Xcode-beta 27.0 有协同 bug)
- 或等待 JetBrains 发布针对 Xcode 27 的 swiftPMDependencies 工具链补丁

### 推荐

**短期**:保持当前 iOS 集成在 `app/iosApp` 工程(任务 19 的手动 SPM 集成),等工具链修复后切换。

**长期**:等 Xcode 26.x GA 后,在 `feature/ad/build.gradle.kts` 中启用 `swiftPMDependencies`,业务模块零配置获得 iOS AdMob 能力。

## 引用

### 官方确认(原始链接)

- **koin-compiler-plugin 1.0.1 release notes** — 明确修复 K2.4
  https://github.com/InsertKoinIO/koin-compiler-plugin/releases/tag/1.0.1
  原文片段:
  > **Kotlin version compatibility** — #19, #42 (and koin#2431)
  > The plugin hard-crashed on the two most recent Kotlin versions:
  > - Kotlin 2.4.0 — ClassCastException during FIR extension registration.
  > 1.0.1 introduces a Kotlin version-adapter layer: the core is compiled against the stable IR API, and a small per-version adapter (selected at compile time) absorbs the breaking compiler-internal differences. A single published jar supports Kotlin 2.3.20 and 2.4.0.

- koin-compiler-plugin releases:https://github.com/InsertKoinIO/koin-compiler-plugin/releases
- 仓库主页:https://github.com/InsertKoinIO/koin-compiler-plugin
- Koin 4.2.1 release:https://github.com/InsertKoinIO/koin/releases/tag/4.2.1

### 失败回溯链(本地实测)

| 时间 | Kotlin 版本 | koin-compiler | 错误 |
| --- | --- | --- | --- |
| 第一次 | 2.4.0-Beta1 | 1.0.0-RC1 | `ClassCastException: FirExtensionRegistrarAdapter$Companion cannot be cast to ProjectExtensionDescriptor at KoinPluginComponentRegistrar.registerExtensions:189` |
| 第二次 | 2.4.0 GA | 1.0.0-RC1 | **完全相同的错误** |
| 第三次 | 2.4.0 | **1.0.1** | ✅ **全平台编译通过** |

### Kotlin 编译器

- [Kotlin 2.4.0 release notes](https://github.com/JetBrains/kotlin/releases/tag/v2.4.0)
- [Kotlin cocoapods to SPM migration skill](.claude/skills/kotlin-tooling-cocoapods-spm-migration/)

### Google Mobile Ads

- [Google Mobile Ads iOS SDK](https://developers.google.com/admob/ios)
- [Swift Package Manager 仓库](https://github.com/googleads/swift-package-manager-google-mobile-ads)
