plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.gradle.plugin.publish)
}

val build_number = providers.gradleProperty("project.build_number").getOrElse("SNAPSHOT")

group = "com.yuanjingtech.boot.app.kmp"
version = "0.0.2-${build_number}"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradle)
    compileOnly(libs.android.gradle.api)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.kotlin.multiplatform.gradle.plugin)
    compileOnly(libs.android.kotlin.multiplatform.library.gradle.plugin)
    compileOnly(libs.compose.multiplatform.gradle.plugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
    compileOnly(libs.koin.compiler.gradle.plugin)
    compileOnly(libs.publish.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.kotlin.jvm.gradle.plugin)
    compileOnly(libs.android.application.gradle.plugin)
}

gradlePlugin {
    website = "https://github.com/yuanjingtech/boot-app-kmp"
    vcsUrl = "https://github.com/yuanjingtech/boot-app-kmp.git"
    plugins {
        create("bootApplicationAndroidPlugin") {
            id = "com.yuanjingtech.boot.app.kmp.application.android"
            implementationClass = "BootApplicationAndroidConventionPlugin"
            displayName = "Kotlin Multiplatform Boot Application Plugin"
            description =
                "A Gradle convention plugin for Kotlin Multiplatform application projects. " +
                        "Automatically applies Android application, Jetpack Compose, and Kotlin Compose compiler plugins. " +
                        "Includes opinionated defaults for Koin dependency injection and Navigation3. " +
                        "Designed to reduce build script boilerplate in KMP projects."
            tags = listOf("kotlin", "multiplatform", "compose", "android", "koin", "navigation")
        }
        create("bootApplicationDesktopPlugin") {
            id = "com.yuanjingtech.boot.app.kmp.application.desktop"
            implementationClass = "BootApplicationDesktopConventionPlugin"
            displayName = "Kotlin Multiplatform Boot Application Plugin"
            description =
                "A Gradle convention plugin for Kotlin Multiplatform application projects. " +
                        "Automatically applies Android application, Jetpack Compose, and Kotlin Compose compiler plugins. " +
                        "Includes opinionated defaults for Koin dependency injection and Navigation3. " +
                        "Designed to reduce build script boilerplate in KMP projects."
            tags = listOf("kotlin", "multiplatform", "compose", "android", "koin", "navigation")
        }
        create("bootApplicationPlugin") {
            id = "com.yuanjingtech.boot.app.kmp.application"
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
            id = "com.yuanjingtech.boot.app.kmp.library"
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
            id = "com.yuanjingtech.boot.app.kmp.settings"
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
        create("bootPublishingPlugin") {
            id = "com.yuanjingtech.boot.app.kmp.publishing"
            implementationClass = "BootPublishingConventionPlugin"
            displayName = "Kotlin Multiplatform Boot Publishing Plugin"
            description =
                "A Gradle convention plugin that configures MavenCentral publishing for subprojects. " +
                        "Applies com.vanniktech.maven.publish with consistent POM metadata " +
                        "(MIT license, yuanjingtech developer, GitHub SCM). " +
                        "Designed to be applied alongside boot-library or boot-application plugins."
            tags = listOf("kotlin", "multiplatform", "publishing", "mavencentral")
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
    }
}
