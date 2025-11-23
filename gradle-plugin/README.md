# boot gradle plugin

## Settings Plugin

Settings插件为多项目构建提供通用配置：
- 配置插件管理仓库 (Google, Gradle Plugin Portal, Maven Central)
- 设置依赖解析管理
- 应用适用于KMP项目的通用设置

ID: `com.yuanjingtech.boot.app.kmp.settings.gradle.plugin`

## App Plugin

App插件专为应用程序项目设计，应用以下插件：
- org.jetbrains.kotlin.multiplatform
- com.android.application
- com.google.devtools.ksp
- org.jetbrains.compose

ID: `com.yuanjingtech.boot.app.kmp.app.gradle.plugin`

## Lib Plugin

Lib插件专为库项目设计，应用以下插件：
- org.jetbrains.kotlin.multiplatform
- com.android.library
- com.google.devtools.ksp
- org.jetbrains.compose

ID: `com.yuanjingtech.boot.app.kmp.lib.gradle.plugin`

所有插件都会自动：
1. 添加KSP (Kotlin Symbol Processing) 支持
2. 包含Koin注解依赖以支持依赖注入
3. 配置KSP在commonMain源集生成元数据
4. 设置正确的KSP处理任务依赖关系

使用这些插件时，只需将它们添加到构建文件中：

对于应用程序项目：
```kotlin
// build.gradle.kts
plugins {
    id("com.yuanjingtech.boot.app.kmp.app.gradle.plugin")
}
```

对于库项目：
```kotlin
// build.gradle.kts
plugins {
    id("com.yuanjingtech.boot.app.kmp.lib.gradle.plugin")
}
```

对于settings配置：
```kotlin
// settings.gradle.kts
plugins {
    id("com.yuanjingtech.boot.app.kmp.settings.gradle.plugin")
}
```