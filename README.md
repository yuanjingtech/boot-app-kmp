# a boot library for kmp app

boot 旨在通过默认的网络、存储、依赖注入、插件实现,简化kmp跨平台应用开发.

![Publish](https://github.com/yuanjingtech/boot-app-kmp/actions/workflows/publish.yml/badge.svg)
![Maven Central Version](https://img.shields.io/maven-central/v/com.yuanjingtech.boot.app.kmp/shared)

## 主要功能

### 设计资源

- [x] 图标 material icons
- [x] 字体 LXGWWenKaiMono

### 界面风格(UI组件库)
封装常用组件
ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui 组件定义
ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidgalass LiquidGlass组件实现
ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/material3 Material3组件实现
- [] 跨平台统一的界面风格(默认LiquidGlass,可选Material3)
- [] 支持平台单独定制(ios/Android/Desktop/Web), 如默认LiquidGlass,但是在Android上使用Material3

### 基础框架

- [x] 日志 logging(kotlin-logging)
- []  网络请求 ktor
- [x] sqdelight 数据存储
    - [] wasm sqldelight
- [] store 数据源
- [] [插件框架](./docs/plugin.md)
    - [x] sweet-spi
    - [] koin(koin compiler plugin not supported yet)(https://github.com/InsertKoinIO/koin-annotations/issues/320)
- [x] koin 依赖注入
- [] coil 图像加载
- [] webview
    - [] https://klibs.io/project/parkwoocheol/compose-webview
    - [] https://klibs.io/project/KevinnZou/compose-webview-multiplatform

### 核心功能

- [] 多租户 tenant
- [] 认证 auth

## 主要修复

- [x] wasm/js 中文显示乱码问题

## 使用

gradle/libs.version.toml

```yaml
[ versions ]
  boot = "0.0.1"
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
