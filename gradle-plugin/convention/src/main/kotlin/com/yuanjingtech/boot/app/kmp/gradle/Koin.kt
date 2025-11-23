package com.yuanjingtech.boot.app.kmp.gradle


import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.gradle.api.Project

internal fun Project.configureKoin() {
    configureKsp()
    if (pluginManager.hasPlugin("kotlin")) {
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.named("commonMain") {
                dependencies {
                    implementation("io.insert-koin:koin-annotations:2.3.1") // Using version from bootlibs
                }
            }
        }

        // KSP Tasks
        dependencies {
            add("kspCommonMainMetadata", "io.insert-koin:koin-ksp-compiler:2.3.1") // Using version from bootlibs
        }
    }
}