rootProject.name = "boot-app-kmp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://packages.jetbrains.team/maven/p/kpm/public/") }
    }
    includeBuild("build-logic")
}

plugins {
    id("com.yuanjingtech.boot.app.kmp.settings")
}

include(":plugin")
include(":app:composeApp")
include(":app:androidApp")
include(":app:desktopApp")
include(":runblocking")
include(":logging")
include(":sqldelight")
include(":subapp")
include(":network")
include(":ui")
include(":webview")
// include(":webview-kevinnzou")
include(":webview-parkwoocheol")
include(":sqlite-wasm-worker")
//include(":sample:plugin")
include(":shared")