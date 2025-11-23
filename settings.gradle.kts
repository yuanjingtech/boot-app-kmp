rootProject.name = "boot-app-kmp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("gradle-plugin")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("com.yuanjingtech.boot.app.kmp.settings.gradle.plugin")
}

include(":composeApp")
include(":runblocking")
include(":logging")
include(":sqldelight")
include(":subapp")
include(":network")
include(":ui")
include(":webview")
include(":sample:plugin")
include(":shared")