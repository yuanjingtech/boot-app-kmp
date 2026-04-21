package com.yuanjingtech.boot.app.kmp.gradle

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureNavigation3() {
    val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
    with(pluginManager) {
        apply(libs.findPlugin("kotlinx-serialization").get().get().pluginId)
    }
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.named("commonMain") {
            dependencies {
                @Suppress("UNCHECKED_CAST")
                implementation(libs.findBundle("nav3").get())
            }
        }
        sourceSets.named("webMain") {
            dependencies {
                @Suppress("UNCHECKED_CAST")
                implementation(libs.findBundle("nav3-web").get())
            }
        }
    }
}
