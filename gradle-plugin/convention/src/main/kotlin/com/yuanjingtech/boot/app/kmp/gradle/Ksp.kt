package com.yuanjingtech.boot.app.kmp.gradle

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureKsp() {
    configureKotlinMultiplatform()
    with(pluginManager) {
        apply("com.google.devtools.ksp")
    }
    extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension> {
        // KSP Common sourceSet
        sourceSets.named("commonMain").configure {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
    }

    // Trigger Common Metadata Generation from Native tasks
    tasks.matching {
        it.name.startsWith("ksp") && it.name != "kspCommonMainKotlinMetadata"
    }.configureEach {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}