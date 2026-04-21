# 参与贡献

## gradle插件
gradle插件已经发布到Gradle Plugin Portal，插件ID如下：
https://plugins.gradle.org/plugin/com.yuanjingtech.boot.app.kmp.settings.gradle.plugin
https://plugins.gradle.org/plugin/com.yuanjingtech.boot.app.kmp.application.gradle.plugin
https://plugins.gradle.org/plugin/com.yuanjingtech.boot.app.kmp.library.gradle.plugin
## 如何使用
在settings.gradle.kts中添加：

```kotlin
plugins {
    id("com.yuanjingtech.boot.app.kmp.settings.gradle.plugin") version "0.0.2-alpha.2"
}
```

## 如何发布插件到Gradle Plugin Portal

参考官方文档：
https://plugins.gradle.org/docs/publish-plugin

验证(不上传):

```shell
./gradlew :gradle-plugin:convention:publishPlugins --validate-only
```

发布:

```shell
./gradlew :gradle-plugin:convention:publishPlugins
```

## 如何统一版本号

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
