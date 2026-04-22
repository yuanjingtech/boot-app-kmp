
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import java.net.URI

class BootSettingsConventionPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) = with(settings) {
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
                maven { url = URI("https://packages.jetbrains.team/maven/p/kpm/public/") }
                maven { url = URI("https://jitpack.io") }
                maven { url = URI("https://jogamp.org/deployment/maven") }
            }
        }
    }
}
