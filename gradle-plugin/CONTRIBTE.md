# 参与贡献

settings.gradle.kts中使用

```kotlin
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
```
来加载boot的version catalog
