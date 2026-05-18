------
name: boot-app-kmp
description: 当第三方项目需要集成或使用 boot-app-kmp Kotlin Multiplatform 库时使用。提供依赖配置、组件使用、Koin DI 配置、主题设置等集成指南。
license: Apache-2.0
metadata:
  author: Yuanjing Tech
  version: "1.0.0"
------

# boot-app-kmp 集成指南

本文档描述第三方项目如何集成和使用 boot-app-kmp 框架与库。

---

## 1. 项目结构

boot-app-kmp 是一个 Kotlin Multiplatform 库，包含以下模块：

| 模块 | 用途 | 产出 |
|------|------|------|
| `composeApp` | 主应用程序模块 | 可执行的 App |
| `shared` | 业务逻辑层 | 跨平台库（数据库、网络、DI 等） |
| `ui` | UI 组件库 | LiquidGlass/Material3 组件 |
| `webview` | WebView 集成 | WebView 支持 |
| `webviewParkwoocheol` | 外部 WebView | 第三方 WebView 集成 |

---

## 2. 添加依赖

### 2.1 设置 Kotlin Multiplatform 项目

确保你的项目使用 Kotlin Multiplatform 插件：

```kotlin
// settings.gradle.kts
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
```

### 2.2 添加 boot-app-kmp 依赖

**方式一：通过源码模块依赖**

如果你将 boot-app-kmp 作为 monorepo 的一部分：

```kotlin
// build.gradle.kts (根项目)
pluginManagement {
    includeBuild("../boot-app-kmp")
}

// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

// 模块 build.gradle.kts
dependencies {
    // UI 组件库（推荐）
    implementation("com.yuanjingtech.boot.app.kmp:ui:1.0.0")

    // 共享业务逻辑
    implementation("com.yuanjingtech.boot.app.kmp:shared:1.0.0")

    // Compose BOM（推荐版本）
    implementation(libs.compose.bom)
}
```

**方式二：通过发布仓库**

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/your-org/boot-app-kmp")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

### 2.3 iOS CocoaPods 集成（可选）

```ruby
# Podfile
pod 'BootAppKmp', :git => 'https://github.com/your-org/boot-app-kmp.git', :branch => 'main'
```

---

## 3. 使用 UI 组件库

### 3.1 组件导入

```kotlin
import com.yuanjingtech.boot.app.kmp.ui.components.liquidglass.*
import com.yuanjingtech.boot.app.kmp.ui.components.material3.*
import com.yuanjingtech.boot.app.kmp.ui.components.PreviewWrapper
```

### 3.2 LiquidGlass 风格组件

LiquidGlass 提供毛玻璃风格的 UI 组件：

```kotlin
@Composable
fun MyScreen() {
    LiquidGlassScaffold(
        topBar = {
            LiquidGlassTopAppBar(title = "标题")
        },
        bottomBar = {
            LiquidGlassBottomNavigation(items = items)
        }
    ) { paddingValues ->
        // 内容区域
        Box(modifier = Modifier.padding(paddingValues)) {
            // ...
        }
    }
}
```

**常用 LiquidGlass 组件：**

| 组件 | 用途 |
|------|------|
| `LiquidGlassScaffold` | 毛玻璃风格脚手架 |
| `LiquidGlassTopAppBar` | 顶部导航栏 |
| `LiquidGlassBottomNavigation` | 底部导航 |
| `LiquidGlassCard` | 毛玻璃卡片 |
| `LiquidGlassButton` | 按钮 |
| `LiquidGlassTextField` | 文本输入框 |
| `LiquidGlassSwitch` | 开关 |
| `LiquidGlassSlider` | 滑块 |

### 3.3 Material3 风格组件

Material3 组件提供标准 Material Design 3 风格：

```kotlin
@Composable
fun MyMaterialScreen() {
    Material3Scaffold(
        topBar = {
            TopAppBar(title = { Text("标题") })
        }
    ) { paddingValues ->
        // 内容
    }
}
```

### 3.4 主题配置

```kotlin
// AppTheme.kt
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        LiquidGlassDarkColorScheme
    } else {
        LiquidGlassLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
```

### 3.5 预览组件

使用 `@PreviewWrapper` 进行跨风格预览：

```kotlin
@PreviewWrapper
@Composable
fun MyComponentPreview() {
    AppTheme {
        MyComponent()
    }
}
```

### 3.6 Koin Preview 依赖注入

使用 `BootApplicationPreview` 简化 Compose 预览的 Koin 依赖注入配置：

```kotlin
@Preview
@Composable
private fun MyScreenPreview() {
    BootApplicationPreview {
        MyScreen()
    }
}
```

**前置依赖**：模块的 `build.gradle.kts` 需要添加预览工具库：

```kotlin
// composeApp/build.gradle.kts
dependencies {
    // 预览支持（androidTest + preview 源码集）
    androidRuntimeClasspath(libs.compose.ui.tooling.preview)
    // 或在 commonMain 需要时
    implementation(libs.compose.ui.tooling.preview)
}
```

**与手动配置对比**：

```kotlin
// ❌ 手动配置（需重复声明 bootModule + pluginModule）
@Preview
@Composable
private fun MyScreenPreview() {
    KoinApplicationPreview(application = { modules(bootModule, pluginModule) }) {
        BootAppTheme {
            MyScreen()
        }
    }
}

// ✅ 使用 BootApplicationPreview（自动包含 bootModule + pluginModule + BootAppTheme）
@Preview
@Composable
private fun MyScreenPreview() {
    BootApplicationPreview {
        MyScreen()
    }
}
```

**适用场景**：预览使用 Koin 注入的 ViewModel 或依赖项的 Composable 组件。

---

## 4. 使用共享模块

### 4.1 数据库配置

boot-app-kmp 使用 Room 3 进行数据库管理：

```kotlin
// AppDatabase.kt
@OptIn(ExperimentalForeignApi::class)
object AppDatabase {
    private val dbFactory = BootDatabaseConstructor()

    val database: Database
        get() = dbFactory.createDatabase()
}

// 在 Koin 模块中注册
val databaseModule = module {
    single { AppDatabase.database }
}
```

### 4.2 网络配置

```kotlin
// NetworkModule.kt
val networkModule = module {
    single { NetworkClient(get()) }
}
```

### 4.3 Koin 依赖注入

```kotlin
// AppModule.kt
val appModule = module {
    // 数据层
    single { AppDatabase.database }
    single { UserRepository(get()) }

    // 网络层
    single { NetworkClient(get()) }

    // ViewModel
    viewModel { MainViewModel(get(), get()) }
}
```

启动应用：

```kotlin
fun Main() {
    KoinApplication(application {
        modules(appModule, databaseModule, networkModule)
    }) {
        App()
    }
}
```

---

## 5. KMP Source-Set 规则

### 5.1 代码放置规范

| 功能类型 | 放置位置 |
|----------|----------|
| 业务逻辑 | `shared/src/commonMain/` |
| 跨平台 UI | `ui/src/commonMain/` |
| Android 特定 | `shared/src/androidMain/` |
| iOS 特定 | `shared/src/iosMain/` |
| 平台测试 | `shared/src/androidTest/` 等 |

### 5.2 依赖层级

```
你的 App
  └── boot-app-kmp:shared (业务逻辑)
  └── boot-app-kmp:ui (UI 组件)
        └── boot-app-kmp:shared
```

---

## 6. 构建和运行

### 6.1 Gradle 构建命令

```bash
# 构建所有平台
./gradlew build

# Android
./gradlew :shared:assembleDebug

# iOS
./gradlew :shared:linkDebugFrameworkIosArm64

# Web
./gradlew :shared:wasmJsBrowserDebug

# 运行测试
./gradlew :shared:testDebugUnitTest
```

### 6.2 Android Studio / IntelliJ 配置

1. 同步 Gradle 项目
2. 选择运行配置（Android/iOS/Web）
3. 运行或调试

---

## 7. 常见问题

### Q: 如何获取 API Key？
A: 在 GitHub Settings → Secrets 中配置 `MINIMAX_API_KEY`

### Q: 如何处理平台特定代码？
A: 使用 `expect` / `actual` 机制在 commonMain 中定义接口，在各平台实现

### Q: 如何自定义组件样式？
A: 覆盖 MaterialTheme 的颜色和类型，或创建自定义 modifier

---

## 8. 示例项目结构

```
your-project/
├── app/
│   ├── src/
│   │   ├── commonMain/
│   │   │   └── kotlin/
│   │   │       └── com/example/app/
│   │   │           ├── App.kt
│   │   │           └── MainScreen.kt
│   │   └── androidMain/
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 9. API 参考

### UI 组件包

```kotlin
// LiquidGlass 组件
com.yuanjingtech.boot.app.kmp.ui.components.liquidglass.*

// Material3 组件
com.yuanjingtech.boot.app.kmp.ui.components.material3.*

// 预览工具
com.yuanjingtech.boot.app.kmp.ui.components.PreviewWrapper
```

### Shared 模块包

```kotlin
// 数据库
com.yuanjingtech.boot.app.kmp.shared.database.*

// 网络
com.yuanjingtech.boot.app.kmp.shared.network.*

// DI
com.yuanjingtech.boot.app.kmp.shared.di.*
```

---

## 10. 版本兼容性

| boot-app-kmp | Kotlin | Compose | 最低 Android SDK |
|---------------|--------|---------|------------------|
| 1.0.x | 2.0+ | 1.6+ | API 24 |
| 0.9.x | 1.9+ | 1.5+ | API 21 |

---

## 12. SweetSPI 插件使用

[SweetSPI](https://github.com/whyoleg/sweetspi) 是一个 KMP ServiceLoader 插件，用于自动生成 `META-INF/services` 文件，实现跨平台的服务发现机制。

### 12.1 添加依赖

```kotlin
// gradle/libs.versions.toml
[versions]
sweetspi = "0.1.3"

[libraries]
sweetspi-processor = { module = "dev.whyoleg.sweetspi:sweetspi-processor", version.ref = "sweetspi" }
sweetspi-runtime = { module = "dev.whyoleg.sweetspi:sweetspi-runtime", version.ref = "sweetspi" }

[plugins]
sweetspi = { id = "dev.whyoleg.sweetspi", version.ref = "sweetspi" }
```

### 12.2 配置模块

在需要使用服务发现的模块 `build.gradle.kts` 中：

```kotlin
import dev.whyoleg.sweetspi.gradle.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.sweetspi)
    // ... 其他插件
}

kotlin {
    // 在 kotlin 块顶部调用
    withSweetSpi()

    android {
        namespace = "com.example.app"
        compileSdk = 35
        // ...
        // 在 android 块内也要调用
        withSweetSpi()
    }

    // iOS, JVM, JS, WASM 目标会自动配置
}
```

### 12.3 定义服务接口

在 `commonMain` 中定义服务接口：

```kotlin
// commonMain/kotlin/com/example/Logger.kt
package com.example

interface Logger {
    fun log(message: String)
}
```

### 12.4 实现服务

创建多个平台实现：

```kotlin
// androidMain/kotlin/com/example/AndroidLogger.kt
package com.example

import android.util.Log

class AndroidLogger : Logger {
    override fun log(message: String) {
        Log.d("App", message)
    }
}

// jvmMain/kotlin/com/example/JvmLogger.kt
package com.example

class JvmLogger : Logger {
    override fun log(message: String) {
        println("[JVM] $message")
    }
}
```

### 12.5 注册实现

在服务发现文件中注册：

```
# src/commonMain/resources/META-INF/services/com.example.Logger
com.example.AndroidLogger
com.example.JvmLogger
```

### 12.6 使用服务

```kotlin
// commonMain/kotlin/com/example/ServiceLocator.kt
package com.example

import dev.whyoleg.sweetspi.ServiceLoader

object ServiceLocator {
    private val loggerLoader = ServiceLoader<Logger>()

    val logger: Logger
        get() = loggerLoader.loadFirst()
            ?: error("No Logger implementation found")
}
```

### 12.7 Android 资源打包配置

由于 KSP 生成的 `META-INF/services/` 文件不会被 `androidResources` 自动包含，需要额外配置：

```kotlin
kotlin {
    sourceSets {
        androidMain {
            // 注册 KSP 生成的服务文件目录
            resources.srcDir(layout.buildDirectory.dir("generated/ksp/android/androidMain/resources"))
        }
    }
}
```

### 12.8 完整示例

```kotlin
// build.gradle.kts
import dev.whyoleg.sweetspi.gradle.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.sweetspi)
}

kotlin {
    withSweetSpi()  // 全局启用

    android {
        namespace = "com.example"
        compileSdk = 35
        minSdk = 24
        withSweetSpi()  // Android 特定配置
    }

    iosArm64()
    iosSimulatorArm64()
    jvm()
    js { browser() }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }
}

kotlin {
    sourceSets {
        androidMain {
            resources.srcDir(layout.buildDirectory.dir("generated/ksp/android/androidMain/resources"))
        }
    }
}
```

### 12.9 注意事项

1. **服务接口必须在 `commonMain`**，才能被各平台实现
2. **实现类必须在对应平台的 source set** 中（如 `androidMain`、`jvmMain`）
3. **服务注册文件在 `commonMain/resources/META-INF/services/`**
4. **Android 必须配置 KSP 资源目录**，否则服务发现会失败

---

## 13. 联系方式

- 文档问题：`docs/solutions/`
- 已知问题：`docs/solutions/` 中的解决方案