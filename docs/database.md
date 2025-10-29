# database

## 使用
启用插件
build.gradle.kts
```kotlin
plugins {
  id("app.cash.sqldelight") version "2.1.0"
}

sqldelight {
  databases {
    create("Database") {
      packageName.set("com.example")
      generateAsync.set(true)
    }
  }
}
```
定义数据模型

创建数据库实例
di
```kotlin
val schema = Database.schema
val database_file_name = "test.db"
val sqlDriver =  provideDbDriver(schema,database_file_name)
```