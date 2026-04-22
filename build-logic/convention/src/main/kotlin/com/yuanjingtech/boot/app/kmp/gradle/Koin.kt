package com.yuanjingtech.boot.app.kmp.gradle

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

private const val KOIN_VERSION = "4.2.1"

internal fun Project.configureKoin() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.named("commonMain") {
            dependencies {
                implementation("io.insert-koin:koin-annotations:$KOIN_VERSION")
            }
        }
    }
}
