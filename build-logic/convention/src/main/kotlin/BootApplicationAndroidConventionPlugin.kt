import org.gradle.api.Plugin
import org.gradle.api.Project

class BootApplicationAndroidConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            // Android app uses AGP 9.x with built-in Kotlin support
            // (no separate `kotlin.android` plugin needed). The Kotlin
            // extension types (`KotlinMultiplatformExtension`,
            // `KotlinJvmProjectExtension`) are not present on the
            // `org.gradle.api.Project` instance, so the shared
            // `configureKotlinMultiplatform()` helper is a no-op here.
            // The per-module `kotlin { compilerOptions { jvmTarget = JVM_17 } }`
            // block inside `app/androidapp/build.gradle.kts` continues to be
            // the source of truth for AGP-9-embedded-Kotlin Android apps.
        }
    }
}
