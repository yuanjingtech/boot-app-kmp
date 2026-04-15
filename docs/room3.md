# room3
https://android-developers.googleblog.com/2026/03/room-30-modernizing-room.html

https://developer.android.com/jetpack/androidx/releases/room3

## 配置
build.gradle.kts
```kotlin
plugins {
    id("androidx.room3") version "3.0.0-alpha03" apply false
}
```
feature/build.gradle.kts
```kotlin
plugins {
    id("androidx.room3")
}

room3 {
    schemaDirectory("$projectDir/schemas")
}
```