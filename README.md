# a boot library for kmp app

boot 旨在通过默认的网络、存储、依赖注入、插件实现,简化kmp跨平台应用开发.

![Publish](https://github.com/yuanjingtech/boot-app-kmp/actions/workflows/publish.yml/badge.svg)
![Maven Central Version](https://img.shields.io/maven-central/v/com.yuanjingtech.boot.app.kmp/shared)

## Boot Starter 架构

boot-app-kmp 参考 Spring Boot Starter 理念设计，核心等价关系:

| Spring Boot | boot-app-kmp | 说明 |
|-------------|---------------|------|
| `spring-boot-starter-xxx` | `boot-xxx` 模块 | 模块化封装 |
| `@Configuration` + `AutoConfiguration` | `BootModule` + Koin | 依赖注入配置 |
| `spring.factories` / `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` | `META-INF/services/` + **SweetSPI** | 自动发现机制 |

### 自动配置原理

自动发现通过 **SweetSPI** (KMP ServiceLoader) 实现:

1. 各模块定义 `BootModule` 实现类
2. SweetSPI KSP 处理器在编译时生成 `META-INF/services/com.yuanjingtech.boot.app.kmp.BootModule` 文件
3. `PluginManager` 通过 `ServiceLoader.load(BootModule)` 在运行时加载所有模块
4. `pluginModule` 统一将所有模块注册到 Koin

参考 [docs/plugin.md](./docs/plugin.md)

## 企业常用 Starter 模块映射

参考 Spring Boot 官方 Starter 设计，映射到 KMP 模块:

### 已实现 ✅

| 排序 | Spring Boot Starter | boot-app-kmp 模块 | 说明 |
|------|---------------------|---------------------|------|
| 1 | spring-boot-starter-web | `network/` (Ktor) | RESTful API 构建 |
| 2 | spring-boot-starter-data-jpa | `sqldelight/` | SQL 数据库访问 |
| 3 | spring-boot-starter-logging | `logging/` (kotlin-logging) | 日志框架 |
| 4 | spring-boot-starter-validation | `ui/` (Material3 TextField) | 参数校验 UI |
| 5 | spring-boot-starter-aop | `plugin/` | 切面/插件化支持 |

### 已实现 (应用层) ✅

| Spring Boot Starter | boot-app-kmp 模块 | 说明 |
|---------------------|---------------------|------|
| UI 组件 | `ui/` | LiquidGlass / Material3 |
| 子应用 | `subapp/` | 模块化解耦 |
| WebView | `webview/` + `webview-parkwoocheol/` | Web 内容集成 |
| 依赖注入 | `shared/` (Koin) | DI 容器 |
| ORM | `shared/` (Room3) | 响应式数据库 |

### 待实现 (按优先级) ❌

| 优先级 | Spring Boot Starter | boot-app-kmp 模块 | 说明 |
|--------|---------------------|---------------------|------|
| P0 | spring-boot-starter-security | `auth/` | 认证/授权 (JWT/Session) |
| P0 | spring-boot-starter-actuator | `actuator/` | 健康检查/监控 |
| P1 | spring-boot-starter-data-redis | `cache/` | KV 缓存 (DataStore) |
| P1 | spring-boot-starter-cache | `store/` | 数据源抽象层 |
| P2 | 多租户 | `tenant/` | SaaS 多租户隔离 |
| P2 | spring-boot-starter-mail | `mail/` | 邮件发送 |
| P2 | spring-boot-starter-websocket | `websocket/` | WebSocket 通信 |
| P3 | spring-boot-starter-batch | `batch/` | 批处理任务 |
| P3 | coil 独立模块 | `image/` | 图像加载 |

> 聚焦 80/20 法则: P0/P1 模块覆盖 80% 企业场景，优先实现。

## 主要功能

### 设计资源

- [x] 图标 material icons
- [x] 字体 LXGWWenKaiMono

### 界面风格(UI组件库)
封装常用组件
ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui 组件定义
ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidgalass LiquidGlass组件实现
ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/material3 Material3组件实现
- [x] 跨平台统一的界面风格(默认LiquidGlass,可选Material3)
- [] 支持平台单独定制(ios/Android/Desktop/Web), 如默认LiquidGlass,但是在Android上使用Material3

### 基础框架

- [x] 日志 logging(kotlin-logging)
- [x] 网络请求 ktor (network 模块)
- [x] sqldelight 数据存储
- [x] wasm sqldelight (sqlite-wasm-worker)
- [] store 数据源
- [x] [插件框架](./docs/plugin.md)
    - [x] sweet-spi 自动发现
    - [x] koin 依赖注入
- [x] coil 图像加载 (ui 模块依赖)
- [x] webview (webview 模块)
    - [x] https://klibs.io/project/parkwoocheol/compose-webview
    - [x] https://klibs.io/project/KevinnZou/compose-webview-multiplatform

### 核心功能

- [] 多租户 tenant
- [] 认证 auth

## 主要修复

- [x] wasm/js 中文显示乱码问题

## 使用

gradle/libs.version.toml

```yaml
[ versions ]
  boot = "0.0.2-alpha.15"
  [ libraries ]
  boot-shared = { module = "com.yuanjingtech.boot.app.kmp:shared", version.ref = "boot" }
  [ bundles ]
  feature = [
  "boot-shared",
]
```

feature/build.gradle.kts

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.bundles.feature)
        }
    }
}
```

App.kt

```kotlin
@Composable
@Preview
fun App() {
    BootApp(config = KoinConfiguration {
        printLogger()
    }) {
        Content()
    }
}
```

## 参考

This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop (JVM), Server.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
      Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
      folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/server](./server/src/main/kotlin) is for the Ktor server application.

* [/shared](./shared/src) is for the code that will be shared between all targets in the project.
  The most important subfolder is [commonMain](./shared/src/commonMain/kotlin). If preferred, you
  can add code to the platform-specific folders here too.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

### Build and Run Server

To build and run the development version of the server, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :server:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :server:run
  ```

### Build and Run Web Application

To build and run the development version of the web app, use the run configuration from the run widget
in your IDE's toolbar or run it directly from the terminal:

- for the Wasm target (faster, modern browsers):
    - on macOS/Linux
      ```shell
      ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
      ```
    - on Windows
      ```shell
      .\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
      ```
- for the JS target (slower, supports older browsers):
    - on macOS/Linux
      ```shell
      ./gradlew :composeApp:jsBrowserDevelopmentRun
      ```
    - on Windows
      ```shell
      .\gradlew.bat :composeApp:jsBrowserDevelopmentRun
      ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).
