package com.yuanjingtech.boot.app.kmp.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Configure the Kotlin toolchain for all JVM / Android targets in this project.
 *
 * Behaviour, from https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains
 *
 * 1. Sets the JDK used by Kotlin/JVM compilation tasks (and, transitively, the
 *    Java compile / test / javadoc tasks — "setting a toolchain via the
 *    `kotlin` extension updates the toolchain for Java compile tasks as well").
 * 2. Sets `compilerOptions.jvmTarget` to the toolchain's JDK version **only
 *    when the user has not set `jvmTarget` explicitly** — so consumers of the
 *    convention plugin can still override per-module if they really need to.
 * 3. JS and Native targets do NOT use the toolchain ("JS and Native tasks don't
 *    use toolchains") — so calling this from a multi-target KMP project is
 *    safe; it only affects the JVM and Android compilations.
 *
 * The Kotlin compiler itself still runs on the JDK that the Gradle daemon
 * runs on ("The Kotlin compiler always runs on the JDK the Gradle daemon is
 * running on"), so the daemon JDK should be ≥ 17. On developer machines the
 * Foojay resolver plugin (registered in `settings.gradle.kts`) downloads the
 * toolchain JDK 17 on demand.
 *
 * KGP 2.4 deprecates configuring the toolchain on individual targets; the
 * compiler error explicitly says "JVM toolchain feature should be configured
 * in the **extension** scope as it affects all JVM targets (JVM, Android)".
 * So we set it once on the extension, and it automatically applies to every
 * JVM / Android target inside the KMP project.
 */
internal fun Project.configureKotlinMultiplatform() {
    val javaTarget = 17

    // Multi-target KMP: configure toolchain at the extension level so it
    // applies uniformly to all JVM / Android targets. Native / JS / Wasm
    // targets are silently ignored by the toolchain machinery.
    extensions.findByType(KotlinMultiplatformExtension::class.java)?.apply {
        @Suppress("UnstableApiUsage", "DEPRECATION")
        jvmToolchain(javaTarget)
    }

    // Single-target Kotlin/JVM (e.g. :app:desktopApp). Also extension-level.
    extensions.findByType(KotlinJvmProjectExtension::class.java)?.apply {
        @Suppress("UnstableApiUsage", "DEPRECATION")
        jvmToolchain(javaTarget)
    }
}
