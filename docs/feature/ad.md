# feature/ad — Google AdMob 跨平台封装

Kotlin Multiplatform 模块,业务方只需 `implementation(project(":feature:ad"))` 即可在 Android / iOS 上获得 Google Mobile Ads 集成,JVM / JS / WasmJS 自动获得占位实现。

## 支持的平台

| 平台 | 状态 | 集成方式 |
| --- | --- | --- |
| Android | ✅ 真实集成 | `com.google.android.gms:play-services-ads:25.3.0` |
| iOS (Arm64 / SimulatorArm64) | ✅ 真实集成 | `swiftPMDependencies` 下沉到 `feature/ad`,业务 iosApp 无需手动 SPM 配置 |
| Desktop/JVM | ⚠️ 占位 | `NoOpAdManager` + 占位 Composable |
| Web (JS / WasmJS) | ⚠️ 占位 | `NoOpAdManager` + 占位 Composable |

## 模块结构

```
feature/ad/
├── src/
│   ├── commonMain/
│   │   ├── AdManager.kt                 // 接口 + SharedFlow<AdEvent>
│   │   ├── AdType.kt / AdEvent.kt       // 枚举 + sealed interface
│   │   ├── AdUnitConfig.kt              // 多平台 ID(Android + iOS 同时持有)
│   │   ├── AdBuildConfig.kt             // debug 模式开关
│   │   ├── AdModule.kt                  // adModule(Koin 聚合入口)
│   │   ├── BaseAdManager.kt             // NoOpAdManager + 事件流基类
│   │   ├── AdBannerRenderer.kt          // 平台渲染器抽象
│   │   ├── AdBanner.kt                  // @Composable Banner
│   │   ├── AdInterstitialTrigger.kt     // @Composable Interstitial
│   │   ├── AdRewardedTrigger.kt         // @Composable Rewarded
│   │   └── preview/
│   │       ├── AdPreviewWrappers.kt     // @PreviewWrapper(dark / light)
│   │       └── AdPreviewParameters.kt   // @PreviewParameterProvider
│   ├── androidMain/                     // AndroidAdManager + AndroidAdBannerRenderer
│   ├── iosMain/                          // NoOpAdManager + IosAdBannerRenderer(占位)
│   ├── jvmMain/                          // NoOp + 占位
│   ├── jsMain/                           // NoOp + 占位
│   └── wasmJsMain/                       // NoOp + 占位
├── build.gradle.kts                      // Koin DSL + swiftPMDependencies
└── MIGRATION_REPORT.md                   // 集成探索记录
```

## 快速上手

### 1. 启用 Koin 模块

`feature/ad` 内部已注册完整 Koin 注入。业务方只需在启动时加载 `adModule`:

```kotlin
// composeApp/src/commonMain/kotlin/...
fun main() {
    startKoin {
        modules(adModule)  // 来自 :feature:ad
    }
    // ...
}
```

### 2. 配置生产 AdUnitConfig

`feature/ad` 提供 **debug / release 自动切换**机制,通过 `AdBuildConfig` 接口区分:

```kotlin
// debug 模式(isDebug = true)→ 自动返回 Google 测试 ID
// release 模式(isDebug = false)→ 使用你提供的生产 ID
```

**业务模块需要做两步:**

**a) 定义生产配置**(一次性):

```kotlin
// 你的业务模块(例如 composeApp/demo/ad/AdProductionConfig.kt)
object AdProductionConfig {
    val production = AdUnitConfig(
        appIdAndroid        = "ca-app-pub-XXXXXXXX~YYYYYY",
        appIdIos            = "ca-app-pub-XXXXXXXX~ZZZZZZ",
        bannerAndroid       = "ca-app-pub-XXXXXXXX/AAAAAAAA",
        bannerIos           = "ca-app-pub-XXXXXXXX/BBBBBBBB",
        interstitialAndroid = "ca-app-pub-XXXXXXXX/CCCCCCCC",
        interstitialIos     = "ca-app-pub-XXXXXXXX/DDDDDDDD",
        rewardedAndroid     = "ca-app-pub-XXXXXXXX/EEEEEEEE",
        rewardedIos         = "ca-app-pub-XXXXXXXX/FFFFFFFF",
    )
}
```

**b) 在 release 构建中覆盖 AdBuildConfig**(关键!):

```kotlin
// androidApp / iosApp 入口
startKoin {
    modules(
        module {
            // release 时返回 false → 使用生产 ID
            single<AdBuildConfig> { object : AdBuildConfig {
                override val isDebug = false
            } }
        },
        adModule,
    )
}
```

如果不覆盖,默认 `DefaultAdBuildConfig` 返回 `isDebug = true`,所有平台都用测试 ID。

### 3. 初始化 SDK

```kotlin
@Composable
fun App() {
    val manager: AdManager = koinInject()
    val provider: AdUnitConfigProvider = koinInject()
    LaunchedEffect(Unit) {
        manager.initialize(provider.current(AdProductionConfig.production))
    }
    // ...
}
```

### 4. 展示 Banner

```kotlin
@Composable
fun HomeScreen() {
    Column {
        Text("Hello")
        AdBanner(config = AdProductionConfig.production)
    }
}
```

当前平台强制覆盖:
```kotlin
AdBanner(
    config = AdProductionConfig.production,
    overrideAdUnitId = "ca-app-pub-XXX/YYY",
)
```

### 5. 展示 Interstitial / Rewarded

```kotlin
val manager: AdManager = koinInject()

// 预加载(可选,减少展示延迟)
LaunchedEffect(Unit) {
    manager.preload(AdType.INTERSTITIAL)
    manager.preload(AdType.REWARDED)
}

// 在合适时机(切页、退出)展示
Button(onClick = {
    scope.launch { manager.show(AdType.INTERSTITIAL) }
}) {
    Text("Next")
}

// 监听事件
val event by manager.events.collectAsState(initial = null)
LaunchedEffect(event) {
    when (event) {
        is AdEvent.Rewarded -> grantCoins(event.reward.amount)
        is AdEvent.Dismissed -> navigateNext()
        is AdEvent.LoadFailed -> log("load failed: ${event.message}")
        // ...
        else -> {}
    }
}
```

激励视频触发器:
```kotlin
AdRewardedTrigger(
    config = AdProductionConfig.production,
    trigger = triggerKey,  // 任意类型,值变化时尝试展示
    onReward = { reward -> grantCoins(reward?.amount ?: 0) },
)
```

## 平台集成细节

### Android

**已自动集成**,无需额外配置。`AndroidAdManager` 通过 `MobileAds.initialize()` 初始化,使用 `AdRequest` 加载广告。

**Manifest 配置**(在 `androidApp/build.gradle.kts`):

```kotlin
android {
    defaultConfig {
        manifestPlaceholders["ADMOB_APP_ID"] = "ca-app-pub-3940256099942544~3347511713"
        // release 应替换为生产 ID
    }
}
```

**AndroidManifest.xml** 已包含:

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="${ADMOB_APP_ID}"/>
```

### iOS

**已通过 `swiftPMDependencies` 自动集成**(`feature/ad/build.gradle.kts` 中配置),业务 iosApp 工程**无需手动添加 SPM 依赖**。

`feature/ad` 通过 KMP swiftPMDependencies 自动:
1. 拉取 Google 官方 SPM 包 `googleads/swift-package-manager-google-mobile-ads @ 11.13.0`
2. 生成 `GoogleMobileAds.framework` + `UserMessagingPlatform.framework` 链接到 `feature/ad` 的 iOS framework
3. 业务 iosApp 通过 `implementation(project(":app:composeApp"))` → `composeApp` 依赖 `feature/ad` → 间接获得 SDK

**Info.plist 必须配置**(`GADApplicationIdentifier` Google SDK 必读):

```xml
<key>GADApplicationIdentifier</key>
<string>ca-app-pub-3940256099942544~1458002511</string>
<!-- release 应替换为生产 ID -->
```

> ⚠️ iOS 已知限制:在 Xcode 27.0 beta 工具链下,swiftPMDependencies 的最后一步 dylib 链接有兼容性问题(`-emit-library` 参数不被识别)。`feature/ad/build.gradle.kts` 中的 `doFirst` workaround 已自动 patch 为 `static` 模式,业务方需配置 `xcodebuild` + `Xcode 26.x GA` 才能完整跑通。详见 `feature/ad/MIGRATION_REPORT.md`。

### JVM / JS / WasmJS

`NoOpAdManager` 自动返回:
- `initialize()` / `preload()`:no-op
- `show()`:立即发出 `LoadFailed("Ads are not supported on this platform")` 事件并返回 false

UI 组件 (`AdBanner` 等) 显示占位 Composable,标签 "Ad Banner (Desktop 暂不支持)" / "(Web 暂不支持)"。

## 公共 API 参考

### `AdType`
```kotlin
enum class AdType { BANNER, INTERSTITIAL, REWARDED }
```

### `AdUnitConfig`
```kotlin
data class AdUnitConfig(
    val appIdAndroid: String,
    val appIdIos: String,
    val bannerAndroid: String,
    val bannerIos: String,
    val interstitialAndroid: String,
    val interstitialIos: String,
    val rewardedAndroid: String,
    val rewardedIos: String,
) {
    val appId: String  // 当前平台的 App ID
    fun idFor(type: AdType): String  // 当前平台对应类型的 ID
    companion object {
        val TEST: AdUnitConfig  // Google 官方测试 ID
    }
}
```

### `AdEvent`(sealed interface)
```kotlin
sealed interface AdEvent {
    val type: AdType
    data class Loaded(override val type)
    data class LoadFailed(override val type, val message: String)
    data class Shown(override val type)
    data class Clicked(override val type)
    data class Dismissed(override val type)
    data class Rewarded(override val type, val reward: AdReward)
    data class ShowFailed(override val type, val message: String)
}
```

### `AdManager`(接口)
```kotlin
interface AdManager {
    val events: SharedFlow<AdEvent>
    suspend fun initialize(config: AdUnitConfig)
    suspend fun preload(type: AdType)
    suspend fun show(type: AdType): Boolean
}
```

### `AdBuildConfig`(接口)
```kotlin
interface AdBuildConfig {
    val isDebug: Boolean
}
class DefaultAdBuildConfig : AdBuildConfig { override val isDebug = true }
```

### `AdUnitConfigProvider`
```kotlin
interface AdUnitConfigProvider {
    fun current(productionConfig: AdUnitConfig): AdUnitConfig
}
class DefaultAdUnitConfigProvider(buildConfig: AdBuildConfig) : AdUnitConfigProvider {
    // isDebug=true → AdUnitConfig.TEST
    // isDebug=false → productionConfig
}
```

### Composable 组件
```kotlin
@Composable
fun AdBanner(
    config: AdUnitConfig,
    modifier: Modifier = Modifier,
    overrideAdUnitId: String? = null,
)

@Composable
fun AdInterstitialTrigger(
    config: AdUnitConfig,
    trigger: Any?,  // 值变化时尝试展示
    overrideAdUnitId: String? = null,
    onShown: (Boolean) -> Unit = {},
)

@Composable
fun AdRewardedTrigger(
    config: AdUnitConfig,
    trigger: Any?,
    overrideAdUnitId: String? = null,
    onReward: (AdReward?) -> Unit = {},
)
```

## Preview 支持

`feature/ad` 提供完整的 Compose Preview 体系:

- `@PreviewWrapper(dark / light)` 主题切换
- `@PreviewParameterProvider` 系统化多配置变体

```kotlin
@Preview(name = "AdBanner - test id (dark)", group = "feature/ad")
@Composable
@PreviewWrapper(wrapper = AdDarkPreviewWrapper::class)
private fun AdBannerDarkPreview() {
    KoinApplicationPreview(application = { modules(adModule) }) {
        AdBanner(config = AdUnitConfig.TEST)
    }
}
```

预览组:`feature/ad`(dark/light 主题)+ `feature/ad - variants`(per-config 变体)。

## 测试

```bash
# 单元测试(commonTest)
./gradlew :feature:ad:jvmTest

# 全平台编译
./gradlew :feature:ad:assemble

# iOS 构建(需 Xcode,详见 MIGRATION_REPORT.md)
cd app/iosApp && xcodebuild -project iosApp.xcodeproj -scheme iosApp \
  -destination 'generic/platform=iOS Simulator' build
```

## 集成探索记录

完整的 swiftPMDependencies + Kotlin 2.4 + koin-compiler 1.0.1 升级探索记录见:
[`feature/ad/MIGRATION_REPORT.md`](../../feature/ad/MIGRATION_REPORT.md)