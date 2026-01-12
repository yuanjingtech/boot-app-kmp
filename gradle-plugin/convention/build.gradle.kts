import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.gradle.plugin.publish)
}

group = "com.yuanjingtech.boot.app.kmp"
version = "0.0.2-alpha.1"

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
            description = "Kotlin Multiplatform Boot Application Plugin"
            tags = listOf("boot", "application")
        }
        create("bootLibraryPlugin") {
            id = libs.plugins.boot.library.get().pluginId
            implementationClass = "BootLibraryConventionPlugin"
            displayName = "Kotlin Multiplatform Boot Library Plugin"
            description = "Kotlin Multiplatform Boot Library Plugin"
            tags = listOf("boot", "library")
        }
        create("bootSettingsPlugin") {
            id = libs.plugins.boot.settings.get().pluginId
            implementationClass = "BootSettingsConventionPlugin"
            displayName = "Kotlin Multiplatform Boot Settings Plugin"
            description = "Kotlin Multiplatform Boot Settings Plugin"
            tags = listOf("boot", "settings")
        }
    }
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
    }
}