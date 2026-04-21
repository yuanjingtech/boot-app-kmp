rootProject.name = "boot-app-kmp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    // 开发阶段使用本地插件(取消下面一行的注释)，发布后使用远程插件
    includeBuild("build-logic")
}

plugins {
    id("com.yuanjingtech.boot.app.kmp.settings.gradle.plugin") version "0.0.2-alpha.2"
}

include(":plugin")
include(":composeApp")
include(":androidApp")
include(":desktopApp")
include(":runblocking")
include(":logging")
include(":sqldelight")
include(":subapp")
include(":network")
include(":ui")
include(":webview")
// include(":webview-kevinnzou")
include(":webview-parkwoocheol")
//include(":sample:plugin")
include(":shared")