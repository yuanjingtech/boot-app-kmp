import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.maven

class BootSettingsConventionPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        with(settings) {
            pluginManagement {
                repositories {
                    google {
                        mavenContent {
                            includeGroupAndSubgroups("androidx")
                            includeGroupAndSubgroups("com.android")
                            includeGroupAndSubgroups("com.google")
                        }
                    }
                    gradlePluginPortal()
                    mavenCentral()
                }
            }

            dependencyResolutionManagement {
                repositories {
                    google {
                        mavenContent {
                            includeGroupAndSubgroups("androidx")
                            includeGroupAndSubgroups("com.android")
                            includeGroupAndSubgroups("com.google")
                        }
                    }
                    mavenCentral()
                    maven("https://packages.jetbrains.team/maven/p/kpm/public/")
                    maven("https://jitpack.io")
                    maven("https://jogamp.org/deployment/maven")
                }
            }
        }
        // Apply common settings that are useful for KMP projects
        println("Boot Settings Plugin applied")
    }
}