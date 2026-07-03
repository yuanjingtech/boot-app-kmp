import com.yuanjingtech.boot.app.kmp.gradle.configureKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

class BootApplicationDesktopConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            configureKotlinMultiplatform()
//            dependencies {
//                "implementation"(libs.compose.ui.desktop)
//            }
        }
    }
}
