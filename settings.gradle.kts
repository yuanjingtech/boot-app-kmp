rootProject.name = "boot-app-kmp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("gradle-plugin")
    includeBuild("build-settings")
}

plugins {
    id("com.yuanjingtech.boot.app.kmp.default")
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("com.yuanjingtech.boot.app.kmp.settings.gradle.plugin")
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