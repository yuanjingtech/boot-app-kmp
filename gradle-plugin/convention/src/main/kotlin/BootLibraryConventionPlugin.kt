import com.yuanjingtech.boot.app.kmp.gradle.configureKoin
import org.gradle.api.Plugin
import org.gradle.api.Project

class BootLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose.hot-reload")
            }
            configureKoin()
        }
    }
}