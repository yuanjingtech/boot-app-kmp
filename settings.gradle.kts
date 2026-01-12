rootProject.name = "boot-app-kmp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("gradle-plugin")
    repositories {
        val artifactory_contextUrl: String by extra
        val artifactory_user: String by extra
        val artifactory_password: String by extra
        maven {
            url = uri("${extra["artifactory_contextUrl"]}/gradle-dev")
            credentials {
                username = artifactory_user
                password = artifactory_password
            }
            isAllowInsecureProtocol = true
        }
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

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        val artifactory_contextUrl: String by extra
        val artifactory_user: String by extra
        val artifactory_password: String by extra
        maven {
            url = uri("${extra["artifactory_contextUrl"]}/gradle-dev")
            credentials {
                username = artifactory_user
                password = artifactory_password
            }
            isAllowInsecureProtocol = true
        }
    }
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