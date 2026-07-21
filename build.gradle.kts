plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.publish) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.koinCompiler) apply false
    alias(libs.plugins.sweetspi) apply false
    alias(libs.plugins.boot.application) apply false
    alias(libs.plugins.boot.application.android) apply false
    alias(libs.plugins.boot.application.desktop) apply false
    alias(libs.plugins.boot.library) apply false
    alias(libs.plugins.room3) apply false
}
subprojects {
    // Apply the Kotlin JVM toolchain (JDK 17) to every KMP / Kotlin-JVM
    // module in this project.
    //
    // Why here and not in a convention plugin: the KMP modules in this
    // project do NOT use the `boot-library` / `boot-application` convention
    // plugins — they apply `kotlinMultiplatform` +
    // `android.kotlin.multiplatform.library` directly. So a convention
    // plugin would not reach them. Instead we use the root `subprojects`
    // hook (matched by plugin id) to configure the toolchain uniformly.
    //
    // KGP 2.4 deprecates configuring the toolchain per target — the
    // compiler says: "JVM toolchain feature should be configured in the
    // extension scope as it affects all JVM targets (JVM, Android)". So
    // we set it on the KotlinMultiplatformExtension / KotlinJvmExtension,
    // not on individual targets. This also covers `iosArm64` / `js` /
    // `wasmJs` silently — toolchains only affect JVM/Android compilations.
    //
    // AGP 9 + embedded Kotlin Android app modules (e.g. :app:androidapp)
    // are NOT covered here — they apply `com.android.application` without
    // a separate `kotlin.android` plugin, so there is no
    // `KotlinMultiplatformExtension` / `KotlinJvmExtension` to configure.
    // Those modules declare `kotlin { jvmToolchain(17) }` inside the
    // `android { }` block themselves; see `app/androidapp/build.gradle.kts`.
    plugins.withId("com.android.kotlin.multiplatform.library") {
        extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension> {
            @Suppress("UnstableApiUsage", "DEPRECATION")
            jvmToolchain(17)
        }
    }
    plugins.withId("org.jetbrains.kotlin.jvm") {
        extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
            @Suppress("UnstableApiUsage", "DEPRECATION")
            jvmToolchain(17)
        }
    }
}
val build_number = providers.gradleProperty("project.build_number").getOrElse("SNAPSHOT")

// `project.publish_to_github_packages=true` is set by `.github/workflows/alpha.yml`
// to route publishing to GitHub Packages instead of Maven Central.
// This keeps release publishing (Maven Central) and alpha publishing
// (GitHub Packages) fully orthogonal — neither workflow touches the other's
// credentials, and a misconfigured alpha run cannot accidentally publish to
// Maven Central.
val publishToGitHubPackages = providers.gradleProperty("project.publish_to_github_packages")
    .map { it.toBoolean() }
    .getOrElse(false)

subprojects {
    group = "com.yuanjingtech.boot.app.kmp"
    version = "0.0.2-${build_number}"
    plugins.withId("com.vanniktech.maven.publish") {
        configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
            // Alpha/internal CI builds go to GitHub Packages; release builds
            // go to Maven Central. The branches are mutually exclusive —
            // see `project.publish_to_github_packages` in alpha.yml.
            //
            // Note: vanniktech's `MavenPublishBaseExtension` only exposes
            // `publishToMavenCentral()` as a built-in target. For GitHub
            // Packages we register a maven repository directly via
            // `PublishingExtension.repositories` below — name MUST be
            // "githubPackages" so the plugin auto-wires credentials from
            // `githubPackagesUsername` / `githubPackagesPassword` (or the
            // `ORG_GRADLE_PROJECT_`-prefixed env-var equivalents).
            // See: https://vanniktech.github.io/gradle-maven-publish-plugin/other
            if (!publishToGitHubPackages) {
                publishToMavenCentral()
            }

            signAllPublications()

            coordinates(group.toString(), name, version.toString())

            pom {
                name = "yuanjingtech's boot library for kmp app"
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

    // GitHub Packages repository registration (alpha/internal only).
    //
    // Registered via `PublishingExtension.repositories` rather than a
    // `MavenPublishBaseExtension` helper because vanniktech's plugin does
    // not provide a `publishToGitHubPackagesRepo()` shortcut — GitHub
    // Packages is configured as a plain maven repo with the well-known
    // name `githubPackages`.
    //
    // Authentication is supplied at runtime by the alpha workflow through
    // the `GITHUB_TOKEN` secret, exposed to Gradle as
    // `ORG_GRADLE_PROJECT_githubPackagesUsername` /
    // `ORG_GRADLE_PROJECT_githubPackagesPassword`. Do NOT hardcode
    // credentials — GitHub Actions' OIDC token rotates per-run.
    //
    // Wrapped in `plugins.withId(...)` so we only touch the extension on
    // modules that actually apply maven-publish; on every other module
    // `PublishingExtension` doesn't exist yet and configure() would fail.
    plugins.withId("com.vanniktech.maven.publish") {
        if (publishToGitHubPackages) {
            extensions.configure<org.gradle.api.publish.PublishingExtension> {
                repositories {
                    maven {
                        name = "githubPackages"
                        url = uri("https://maven.pkg.github.com/yuanjingtech/boot-app-kmp")
                    }
                }
            }
        }
    }
}