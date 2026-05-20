# Skills

## kotlin-agent-skills

```shell
npx skills add Kotlin/kotlin-agent-skills
```

[kotlin-tooling-agp9-migration](../.claude/skills/kotlin-tooling-agp9-migration)
[kotlin-tooling-cocoapods-spm-migration](../.claude/skills/kotlin-tooling-cocoapods-spm-migration)

## koin

```shell
npx skills add InsertKoinIO/koin-migration
```

use skill di-migration

koin-compiler-plugin-migrateion
https://insert-koin.io/docs/migration/from-ksp-to-compiler-plugin

koin-compose-ui-preview
https://insert-koin.io/docs/4.1/reference/koin-compose/compose#compose-preview-with-koinapplicationpreview

**boot-app-kmp 简化用法**：shared 模块提供 `BootApplicationPreview` 封装，自动注入 bootModule + pluginModule：

```kotlin
@Preview
@Composable
private fun MyScreenPreview() {
    BootApplicationPreview {
        MyScreen()
    }
}
```

依赖（koin-compose 已包含在 boot-app-kmp 各模块中）：

```kotlin
dependencies {
    // 预览工具库
    androidRuntimeClasspath(libs.compose.ui.tooling.preview)
}
```

详情见 [boot-app-kmp SKILL.md](../../skills/boot-app-kmp/SKILL.md#36-koin-preview-依赖注入)

### auto discover koin modules migration

when use koin compiler plugin, the auto discover koin modules feature need to change from property to function,
before

```kotlin
@Module
@Configuration
class DemoModule {
    val module get() = demoModule
}
```

after

```kotlin
@Module
@Configuration
class DemoModule {
    fun module() = demoModule
}
```

## room3

jetpack-room3-migration
https://developer.android.com/jetpack/androidx/releases/room3
https://android-developers.googleblog.com/2026/03/room-30-modernizing-room.html

## compose

compose-compatibliity-check
https://kotlinlang.org/docs/multiplatform/compose-compatibility-and-versioning.html

compose-ui-preview
https://kotlinlang.org/docs/multiplatform/compose-previews.html#preview-setup
also see koin-compose-ui-preview above

```kotlin
// In the module's build.gradle.kts
dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling.preview)
}
```

compose-ui-preview-wrapper
https://juejin.cn/post/7618073813482799150