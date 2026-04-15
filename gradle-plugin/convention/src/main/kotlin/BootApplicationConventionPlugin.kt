import com.yuanjingtech.boot.app.kmp.gradle.configureKoin
import org.gradle.api.Plugin
import org.gradle.api.Project

class BootApplicationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("io.insert-koin.compiler.plugin")
            }
            configureKoin()
        }
    }
}
