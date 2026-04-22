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
        }
    }
}
