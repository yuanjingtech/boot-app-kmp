# boot gradle plugin

## Settings Plugin

The settings plugin provides common configuration for multi-project builds:
- Configures plugin management repositories (Google, Gradle Plugin Portal, Maven Central)
- Sets up dependency resolution management
- Applies common settings useful for KMP projects

ID: `com.yuanjingtech.boot.app.kmp.settings.gradle.plugin`

## App Plugin

The app plugin is designed for application projects and applies the following plugins:
- org.jetbrains.kotlin.multiplatform
- com.android.application
- com.google.devtools.ksp
- org.jetbrains.compose

ID: `com.yuanjingtech.boot.app.kmp.app.gradle.plugin`

## Lib Plugin

The lib plugin is designed for library projects and applies the following plugins:
- org.jetbrains.kotlin.multiplatform
- com.android.library
- com.google.devtools.ksp
- org.jetbrains.compose

ID: `com.yuanjingtech.boot.app.kmp.app.gradle.lib`

All plugins automatically:
1. Add KSP (Kotlin Symbol Processing) support
2. Include Koin annotations dependency for dependency injection
3. Configure KSP to generate metadata in the commonMain source set
4. Set up proper task dependencies for KSP processing

To use these plugins, simply add them to your build files:

For app projects:
```kotlin
// build.gradle.kts
plugins {
    id("com.yuanjingtech.boot.app.kmp.app.gradle.plugin")
}
```

For library projects:
```kotlin
// build.gradle.kts
plugins {
    id("com.yuanjingtech.boot.app.kmp.app.gradle.lib")
}
```

For settings configuration:
```kotlin
// settings.gradle.kts
plugins {
    id("com.yuanjingtech.boot.app.kmp.settings.gradle.plugin")
}
```
