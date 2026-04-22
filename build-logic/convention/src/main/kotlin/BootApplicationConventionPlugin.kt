import com.yuanjingtech.boot.app.kmp.gradle.configureKoin
import com.yuanjingtech.boot.app.kmp.gradle.configureNavigation3
import org.gradle.api.Plugin
import org.gradle.api.Project

class BootApplicationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            configureKoin()
            configureNavigation3()
        }
    }
}
