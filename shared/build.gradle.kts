import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("com.vanniktech.maven.publish") version "0.34.0"
}
group = "com.yuanjingtech.boot.app.kmp"
version = "0.0.1"
kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            api(libs.bundles.koin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.yuanjingtech.boot.app.kmp.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
// <module directory>/build.gradle.kts

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "shared", version.toString())

    pom {
        name = "yuanjingtech's boot library for kmp app"
        description = "yuanjingtech's boot library for kmp app\"."
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