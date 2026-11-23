# 已发布 klib 里的 worker URL 不可传递（2026-11-17）

## 症状

下游项目（`yuanjing-app-kmp`）在 `origin/main` 上构建 Web 目标必红，唯一失败步骤是
`Build web (WasmJS)`：

```
ERROR in ./build/wasm/packages/composeApp/kotlin/composeApp.js
Module not found: Error: Can't resolve 'sqlite-wasm-worker/worker.js'
  in '/home/runner/work/yuanjing-app-kmp/yuanjing-app-kmp/build/wasm/packages/composeApp/kotlin'
> Task :app:webApp:wasmJsProductionExecutableCompileSync FAILED
```

其余 4 个 job（Android / JVM / iOS / shared 测试）全绿，本地 `compileKotlinWasmJs` 也能过 ——
只有真正跑 webpack 的步骤才炸。下游既没有 import 任何 SQLite API，也没有调用
`bootPlatformModule`。

## 根因

`shared/src/{jsMain,wasmJsMain}/…/Platform.*.kt` 里写死了 worker 脚本 URL：

```kotlin
js("""new Worker(new URL("sqlite-wasm-worker/worker.js", import.meta.url))""")
```

`new URL("…", import.meta.url)` 是 webpack 唯一能识别 worker chunk 的写法，代价是
**这个字面量会被烤进 klib**。webpack 解析该库的 JS 时就会解析这个 specifier，与运行时是否
用到数据库无关。而两件事让库自己无法满足这个 specifier：

1. `sqlite-wasm-worker` 是仓库内的 **file 包**（`sqlite-wasm-worker/worker/`），从未发布：
   `npm view sqlite-wasm-worker versions` → `Not Found`，`registry.npmjs.org` → 404。
2. **npm 依赖不随 Maven 构件发布**。实测 `shared-wasm-js/0.0.2-alpha.40` 的 `.pom` 与
   `.module`，`grep -c 'npm|sqlite-wasm-worker'` 均为 `0`；而同一版本的 klib 内该 URL 字面量
   命中 `1` 次（alpha.35/37/39 同样 1 次，alpha.18 为 0 —— 该依赖是 alpha.35 之后引入的）。

结论：`implementation(npm(...))` 只对**本仓库自己的**构建有效，对任何下游都是 0 帮助。

## 修法：把 worker 的所有权交给应用层

- `shared`：新增 `BootSqliteWorker`（`src/webMain`），只有 `install(factory)` / `internal create()`；
  未安装时抛出可操作的错误文案，而不是 1 个 DSL 级别的构建失败。
- `shared`：`Platform.js.kt` / `Platform.wasmJs.kt` 改用 `BootSqliteWorker.create()`，
  `shared/build.gradle.kts` 删掉 `npm("sqlite-wasm-worker", …)`。
- `app/webApp`：在自己的 `webMain` 里写下字面量并 `install`，同时在 `webMain.dependencies`
  声明 `npm("sqlite-wasm-worker", file("../../sqlite-wasm-worker/worker"))` 与
  `npm("@sqlite.org/sqlite-wasm", "3.50.1-build1")`（KGP 只 link 本地包，不装其传递依赖）。

下游代价：真正使用 web 数据库的应用要加「声明 npm + install 一次」两件事；不使用数据库的
应用从「构建必红」变成「什么都不用做」。

## 教训

- 一个库能否把资源交给运行时，取决于 **消费者** 的打包器能解析什么；「我这边能构建」不是证据。
- 发布物里能携带的东西只有 Maven 元数据声明的那些。写 `npm(...)` 前先问：这些字节要跟着
  构件走吗？走不了就得想办法让应用层提供。
- 判据：`grep -c '<可疑字面量>' <published>.klib` 与 `grep -c 'npm|' <published>.pom` 一起看，
  两个数字才是「它到底传没传下去」的完整答案。