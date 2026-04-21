import com.yuanjingtech.boot.app.kmp.gradle.configureKoin
import com.yuanjingtech.boot.app.kmp.gradle.configureNavigation3
import org.gradle.api.Plugin
import org.gradle.api.Project

class BootLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            configureKoin()
            configureNavigation3()
        }
    }
}
