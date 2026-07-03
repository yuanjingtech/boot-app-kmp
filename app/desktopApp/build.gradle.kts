import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

// JVM toolchain (JDK 17) is configured in the root `build.gradle.kts`
// `subprojects { plugins.withId("org.jetbrains.kotlin.jvm") { ... } }`
// block. The toolchain propagates to the `java { }` extension and sets
// `sourceCompatibility` / `targetCompatibility` automatically.

compose.desktop {
    application {
        mainClass = "com.yuanjingtech.boot.app.kmp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.yuanjingtech.boot.app.kmp"
            packageVersion = "1.0.0"
        }
    }
}

dependencies {
    implementation(projects.app.composeApp)
    implementation(libs.compose.ui.desktop)
}
//webview-parkwoocheol依赖了sun.awt和sun.lwawt等模块，在Java 17中需要添加以下JVM参数来允许访问这些模块：
compose.desktop {
    application {
        jvmArgs += listOf(
            "--add-exports=java.desktop/sun.awt=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
            "--add-exports=java.desktop/sun.lwawt=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.lwawt=ALL-UNNAMED",
            "--add-exports=java.desktop/sun.lwawt.macosx=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.lwawt.macosx=ALL-UNNAMED",
        )
    }
}