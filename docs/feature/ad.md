# feature/ad — Google AdMob 跨平台封装

Kotlin Multiplatform 封装,统一 API 调用 Google AdMob(Banner / Interstitial / Rewarded)。
各平台 actual 实现见 `src/<platform>Main/`,桌面 / Web 平台使用 `NoOpAdManager` 占位。

## 支持的形态

| 形态 | 状态 | 依赖 |
| --- | --- | --- |
| Android | 真实接入 | `com.google.android.gms:play-services-ads:24.5.0` |
| iOS (Arm64 / SimulatorArm64) | 占位 (NoOp),TODO 接入 cinterop | — |
| Desktop/JVM | 占位 | — |
| Web (JS / WasmJS) | 占位 | — |

## 平台抽象

```
commonMain                   AdManager (interface), AdType, AdUnitConfig, AdEvent, AdBannerRenderer
 ├─ androidMain              AndroidAdManager (play-services-ads) + AndroidAdBannerRenderer (AndroidView<AdView>)
 ├─ iosMain                  NoOpAdManager + IosAdBannerRenderer (占位)
 ├─ jvmMain                  NoOpAdManager + JvmAdBannerRenderer
 ├─ jsMain                   NoOpAdManager + WebAdBannerRenderer
 └─ wasmJsMain               NoOpAdManager + WasmJsAdBannerRenderer
```

`AdUnitConfig` 同时持有 Android / iOS 两组 App ID 与广告位 ID,运行时通过
`expect val currentPlatform: PlatformType` 选择当前平台的 ID。

## 快速上手

### 1. 启用 Koin 模块

```kotlin
// composeApp/src/commonMain/kotlin/...
fun main() {
    startKoin {
        modules(adModule)
    }
    // ...
}
```

`adModule` 默认注册了 `AdBuildConfig`(`isDebug = true`)。Release 构建应覆盖:

```kotlin
modules(
    module {
        single<AdBuildConfig> { object : AdBuildConfig { override val isDebug = BuildConfig.DEBUG } }
    },
    adModule,
)
```

### 2. 初始化

```kotlin
val manager: AdManager = koinInject()
lifecycleScope.launch {
    val production = AdUnitConfig(
        appIdAndroid = "ca-app-pub-XXXXXXXX~YYYYYY",
        appIdIos = "ca-app-pub-XXXXXXXX~ZZZZZZ",
        bannerAndroid = "ca-app-pub-XXXXXXXX/AAAAAAAA",
        bannerIos = "ca-app-pub-XXXXXXXX/BBBBBBBB",
        interstitialAndroid = "ca-app-pub-XXXXXXXX/CCCCCCCC",
        interstitialIos = "ca-app-pub-XXXXXXXX/DDDDDDDD",
        rewardedAndroid = "ca-app-pub-XXXXXXXX/EEEEEEEE",
        rewardedIos = "ca-app-pub-XXXXXXXX/FFFFFFFF",
    )
    val effective = koinInject<AdUnitConfigProvider>().current(production)
    manager.initialize(effective)
}
```

### 3. 展示 Banner

```kotlin
@Composable
fun HomeScreen() {
    val productionConfig = remember { /* 见第 2 步 */ }
    Column {
        Text("Hello")
        // 多平台:传入完整 AdUnitConfig,库内部根据 currentPlatform 选择
        AdBanner(config = productionConfig)
    }
}
```

可选覆盖:
```kotlin
AdBanner(
    config = productionConfig,
    overrideAdUnitId = "ca-app-pub-XXX/YYY", // 当前平台强制使用此 ID
)
```

### 4. 展示 Interstitial / Rewarded

```kotlin
val manager: AdManager = koinInject()
val productionConfig = remember { /* 见第 2 步 */ }

// 提前预加载
lifecycleScope.launch { manager.preload(AdType.INTERSTITIAL) }

// 在合适的时机(切页/退出)展示
Button(onClick = {
    lifecycleScope.launch { manager.show(AdType.INTERSTITIAL) }
}) {
    Text("Next")
}

// 监听事件
val event by manager.events.collectAsState(initial = null)
LaunchedEffect(event) {
    if (event is AdEvent.Dismissed) {
        // 跳转下一页
    }
}
```

激励视频:

```kotlin
AdRewardedTrigger(
    config = productionConfig,
    trigger = unit, // 变化时尝试展示
    onReward = { reward -> grantCoins(reward?.amount ?: 0) },
)
```

## Android 配置

1. `androidApp/build.gradle.kts` 添加:
   ```kotlin
   defaultConfig {
       manifestPlaceholders["ADMOB_APP_ID"] = "ca-app-pub-XXXXXXXX~YYYYYY"
   }
   ```
2. `AndroidManifest.xml`:
   ```xml
   <application>
       <meta-data
           android:name="com.google.android.gms.ads.APPLICATION_ID"
           android:value="${ADMOB_APP_ID}"/>
   </application>
   ```

## 测试 ID

`AdUnitConfig.TEST` 内置 Google 官方测试 ID(`ca-app-pub-3940256099942544/...`),
仅在 `AdBuildConfig.isDebug == true` 时由 `DefaultAdUnitConfigProvider.current()` 返回。
**禁止在 release 构建中手动使用 `AdUnitConfig.TEST`**。

## iOS 接入计划(待办)

1. 引入 cocoapods 插件或 swiftPMDependencies 暴露 `GoogleMobileAds` 给 iosMain
2. 用 `UIKitView { GADBannerView(adSize: kGADAdSizeBanner) }` 替换占位
3. 集成 `GADInterstitialAd` / `GADRewardedAd` 实现 `load/show` 流程
4. 在 iosApp 的 `Info.plist` 中添加 `GADApplicationIdentifier`

## 单元测试

```bash
./gradlew :feature:ad:jvmTest
```

覆盖:
- `AdUnitConfigTest` — 平台 ID 选择、TEST 常量结构
- `NoOpAdManagerTest` — 占位实现的初始化 / 预加载 / 展示失败事件
