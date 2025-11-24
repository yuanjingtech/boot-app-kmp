plugins {
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.publish) apply false
}
val build_number = providers.gradleProperty("project.build_number").getOrElse("SNAPSHOT")
subprojects {
    group = "com.yuanjingtech.boot.app.kmp"
    version = "0.0.2-${build_number}"
    plugins.withId("com.vanniktech.maven.publish") {
        configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
            publishToMavenCentral()
            signAllPublications()
            coordinates(group.toString(), name, version.toString())

            pom {
                name = "yuanjingtech's boot gradle plugin for kmp app"
                description = "yuanjingtech's boot library for kmp app."
                inceptionYear = "2025"
                url = "https://github.com/yuanjingtech/boot-app-kmp"
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://github.com/yuanjingtech/boot-app-kmp/blob/main/LICENSE"
                        distribution = "https://github.com/yuanjingtech/boot-app-kmp/blob/main/LICENSE"
                    }
                }
                developers {
                    developer {
                        id = "yuanjingtech"
                        name = "yuanjingtech"
                        url = "https://github.com/yuanjingtech"
                    }
                }
                scm {
                    url = "https://github.com/yuanjingtech/boot-app-kmp"
                    connection = "scm:git:git://github.com/yuanjingtech/boot-app-kmp.git"
                    developerConnection = "scm:git:ssh://git@github.com/yuanjingtech/boot-app-kmp.git"
                }
            }
        }
    }
}