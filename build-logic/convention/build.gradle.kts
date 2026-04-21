import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.gradle.plugin.publish)
}

group = "com.yuanjingtech.boot.app.kmp"
version = "0.0.2-alpha.2"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}


// 插件的依赖 (插件实现代码中使用的依赖)
dependencies {
    compileOnly(libs.android.gradle.api)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.kotlin.jvm.gradle.plugin)
    compileOnly(libs.kotlin.multiplatform.gradle.plugin)
}

gradlePlugin {
    website = "https://github.com/yuanjingtech/boot-app-kmp"
    vcsUrl = "https://github.com/yuanjingtech/boot-app-kmp.git"
    plugins {
        create("bootApplicationPlugin") {
            id = libs.plugins.boot.application.get().pluginId
            implementationClass = "BootApplicationConventionPlugin"
            displayName = "Kotlin Multiplatform Boot Application Plugin"
            description =
                "A Gradle convention plugin for Kotlin Multiplatform application projects. " +
                "Automatically applies Android application, Jetpack Compose, and Kotlin Compose compiler plugins. " +
                "Includes opinionated defaults for Koin dependency injection and Navigation3. " +
                "Designed to reduce build script boilerplate in KMP projects."
            tags = listOf("kotlin", "multiplatform", "compose", "android", "koin", "navigation")
        }
        create("bootLibraryPlugin") {
            id = libs.plugins.boot.library.get().pluginId
            implementationClass = "BootLibraryConventionPlugin"
            displayName = "Kotlin Multiplatform Boot Library Plugin"
            description =
                "A Gradle convention plugin for Kotlin Multiplatform library projects. " +
                "Automatically applies Android library, Jetpack Compose, and Kotlin Compose compiler plugins. " +
                "Includes opinionated defaults for Koin dependency injection and Navigation3. " +
                "Designed to reduce build script boilerplate in KMP projects."
            tags = listOf("kotlin", "multiplatform", "compose", "android", "koin", "navigation")
        }
        create("bootSettingsPlugin") {
            id = libs.plugins.boot.settings.get().pluginId
            implementationClass = "BootSettingsConventionPlugin"
            displayName = "Kotlin Multiplatform Boot Settings Plugin"
            description =
                "A Gradle settings plugin that configures pluginManagement and dependencyResolutionManagement " +
                "with curated repositories for Kotlin Multiplatform projects. " +
                "Pre-configures Google, Gradle Plugin Portal, Maven Central, JetBrains KPM, JitPack, and JOGL repositories. " +
                "Also applies the Foojay toolchain resolver for consistent JDK management across builds. " +
                "Apply this plugin in settings.gradle.kts before any project-level plugins."
            tags = listOf("kotlin", "multiplatform", "settings", "repositories", "android", "dependency-management")
        }
    }
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
    }
}