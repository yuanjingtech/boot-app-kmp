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
## Web（js / wasmJs）

Web 端用 `WebWorkerSQLiteDriver` 把 SQLite 放在 worker 里跑。worker 脚本由**应用**提供，
库里不带（原因见 `docs/solutions/build-errors/klib-baked-worker-url-2026-11-17.md`）。

应用要做两件事。

构建脚本里声明 npm 依赖：

```kotlin
kotlin {
  sourceSets {
    webMain.dependencies {
      implementation(npm("sqlite-wasm-worker", file("../../sqlite-wasm-worker/worker")))
      implementation(npm("@sqlite.org/sqlite-wasm", "3.50.1-build1"))
    }
  }
}
```

工厂必须写成 `expect`/`actual` 对，**不能**写成内联 `js(...)` 的 lambda ——
Kotlin/Wasm 只接受「顶层函数体里的单个表达式或属性初始化器」形式：

```kotlin
// webMain（js 与 wasmJs 共用的中间源集）
internal expect fun newSqliteWorker(): Worker

// 启动时、第一次解析数据库之前安装一次
fun main() {
  BootSqliteWorker.install { newSqliteWorker() }
  ComposeViewport { App() }
}

// jsMain
@Suppress("UnsafeCastFromDynamic")
internal actual fun newSqliteWorker(): Worker =
  Worker(js("""new URL("sqlite-wasm-worker/worker.js", import.meta.url)"""))

// wasmJsMain
internal actual fun newSqliteWorker(): Worker =
  js("""new Worker(new URL("sqlite-wasm-worker/worker.js", import.meta.url))""")
```

写成 `install { js("""new Worker(new URL(...))""") }` 会在 Wasm 目标直接编译失败：
`Calls to 'js(code)' must be a single expression inside a top-level function body or a property initializer in Kotlin/Wasm.`

URL 必须保持 `new Worker(new URL("…", import.meta.url))` 的字面量形状：那是 webpack 识别
worker chunk 的唯一签名。`new Worker("path")` 会留下解析不了的运行时 URL，
`external fun createWorker()` 只会在某个脚本恰好定义了该全局时才工作。

不用 web 数据库的应用什么都不用做。
