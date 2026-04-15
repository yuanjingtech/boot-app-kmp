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