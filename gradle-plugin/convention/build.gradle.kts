import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlinJvm)
}

group = "com.yuanjingtech.boot.app.kmp"
version = "1.0.0"

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
    plugins {
        create("bootAppPlugin") {
            id = libs.plugins.boot.application.get().pluginId
            implementationClass = "BootApplicationConventionPlugin"
        }
        create("bootLibPlugin") {
            id = libs.plugins.boot.library.get().pluginId
            implementationClass = "BootLibraryConventionPlugin"
        }
        create("bootSettingsPlugin") {
            id = libs.plugins.boot.settings.get().pluginId
            implementationClass = "BootSettingsConventionPlugin"
        }
    }
}