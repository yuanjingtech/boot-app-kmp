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
    // Foojay Toolchain Resolver: lets Gradle automatically download the JDK
    // versions requested by `kotlin { jvmToolchain(17) }` / `java { toolchain { ... } }`
    // when the local machine does not have them pre-installed.
    // Required for Gradle 8.0.2+ to resolve toolchains from a remote source.
    // See: https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":plugin")
include(":app:composeApp")
// On case-sensitive filesystems (Linux CI runners, GitHub Actions)
// `:app:androidApp` does not resolve because the actual directory is
// `androidapp` (lowercase `a`). macOS HFS+/APFS is case-insensitive
// by default, which masked the mismatch locally.
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
include(":feature:ad")