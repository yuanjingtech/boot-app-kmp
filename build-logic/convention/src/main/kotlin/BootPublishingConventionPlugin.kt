
import org.gradle.api.Plugin
import org.gradle.api.Project

class BootPublishingConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withId("com.vanniktech.maven.publish") {
            project.afterEvaluate {
                val ext = project.extensions.getByName("MavenPublishBaseExtension")
                ext::class.java.methods.find { it.name == "publishToMavenCentral" && it.parameterCount == 0 }
                    ?.invoke(ext)
                ext::class.java.methods.find { it.name == "signAllPublications" && it.parameterCount == 0 }
                    ?.invoke(ext)
                ext::class.java.methods.find { m ->
                    m.name == "coordinates" &&
                        m.parameterTypes.size == 3 &&
                        m.parameterTypes.all { it == String::class.java }
                }?.invoke(ext, project.group.toString(), project.name, project.version.toString())

                val pom = ext::class.java.methods.find { it.name == "pom" && it.parameterCount == 0 }?.invoke(ext) ?: return@afterEvaluate
                setPom(pom, project)
            }
        }
    }

    private fun setPom(pom: Any, project: Project) {
        invoke(pom, "name", project.name)
        invoke(pom, "description", "${project.name} by yuanjingtech")
        invoke(pom, "inceptionYear", "2025")
        invoke(pom, "url", "https://github.com/yuanjingtech/boot-app-kmp")

        invoke(pom, "licenses") {
            invoke(it, "license") {
                invoke(it, "name", "MIT License")
                invoke(it, "url", "https://github.com/yuanjingtech/boot-app-kmp/blob/main/LICENSE")
                invoke(it, "distribution", "https://github.com/yuanjingtech/boot-app-kmp/blob/main/LICENSE")
            }
        }

        invoke(pom, "developers") {
            invoke(it, "developer") {
                invoke(it, "id", "yuanjingtech")
                invoke(it, "name", "yuanjingtech")
                invoke(it, "url", "https://github.com/yuanjingtech")
            }
        }

        invoke(pom, "scm") {
            invoke(it, "url", "https://github.com/yuanjingtech/boot-app-kmp")
            invoke(it, "connection", "scm:git:git://github.com/yuanjingtech/boot-app-kmp.git")
            invoke(it, "developerConnection", "scm:git:ssh://git@github.com:yuanjingtech/boot-app-kmp.git")
        }
    }

    private fun invoke(obj: Any, name: String, value: String) {
        try {
            obj::class.java.methods.find { it.name == name && it.parameterCount == 1 }?.invoke(obj, value)
        } catch (_: Exception) {}
    }

    private fun invoke(obj: Any, name: String, block: (Any) -> Unit) {
        try {
            obj::class.java.methods.find { it.name == name && it.parameterCount == 0 }?.invoke(obj)?.let(block)
        } catch (_: Exception) {}
    }
}
