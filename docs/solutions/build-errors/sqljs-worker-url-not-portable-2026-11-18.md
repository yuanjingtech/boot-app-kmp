# SQLDelight js 驱动把 worker URL 烤进了 klib（2026-11-18）

这是 `klib-baked-worker-url-2026-11-17.md`（`:shared` 的 `sqlite-wasm-worker`）的**同构缺陷**：
同一个机制，不同的库、不同的 npm 包。上一份修的是 Room 的 worker，这一份修 SQLDelight 的。

> 合并顺序：`klib-baked-worker-url-2026-11-17.md` 目前只存在于 PR #18，尚未进 main —— 本 PR 应与 #18 一起合（#18 先），否则 main 上这条链接悬空。

## 症状

`sqldelight-js` 的发布构件的 klib 里含有唯一一处写死的 worker 脚本 URL：

```
new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)
```

实测（从 Maven Central 下载后 `strings` / `unzip -l`）：

| 版本 | klib 大小 | 字面量命中 |
| --- | --- | --- |
| `0.0.1` | — | 该版本在 Maven Central 为 404（不存在） |
| `0.0.2-alpha.18` | 7534 B | **1** |
| `0.0.2-alpha.38` | 8827 B | **1** |
| `0.0.2-alpha.42` | 8826 B | **1** |

`sqldelight-wasm-js` 为 **0**（wasmJs 侧的 `DriverFactory` 是 `TODO()`，字面量本来就被注释掉）。

下游 js 消费者一旦在同一个 bundle 里带上 `DriverFactory`，webpack 就会去解析这个 specifier。
仓库自己的 `app/webApp` 目前没受影响（产物里 `sqljs` / `WebWorkerDriver` 命中 0，代码不可达，
被摇掉），但这只是「还没人用」，不是「没问题」。

## 根因

来源在本仓库，不在上游：

- `sqldelight/src/jsMain/…/DriverFactory.js.kt`：
  `WebWorkerDriver(Worker(js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")))`。
- 上游 `app.cash.sqldelight:web-worker-driver` 是干净的：`2.3.2` / `2.4.1` 的 `.module` 里
  `npm|` 命中 **0**，`web-worker-driver-2.3.2-sources.jar` 内 `new URL` / `sqljs.worker` 命中 **0**
  —— 它只负责「把一个 Worker 包成驱动」，worker 脚本是谁的、从哪来，全部由调用方决定，
  而我们把这个决定写死在了库里。

为什么写死在这里是可传不出去的：

1. **npm 依赖不随 Maven 构件发布**（与 `:shared` 完全相同的机制）。消费者必须在自己的
   `jsMain.dependencies` 里声明 `@cashapp/sqldelight-sqljs-worker`，`api(npm("sql.js", …))`
   帮不上忙。
2. `import.meta.url` 在消费者 bundle 里解析的是**消费者模块**的位置，语义上是对的，
   但它要求消费者磁盘上恰好有那个包 —— 库里没有任何办法保证。

一个附带发现：本模块的 `jsMain.dependencies` 声明的是 `api(npm("sql.js", "1.6.2"))`，而
`@cashapp/sqldelight-sqljs-worker@2.4.1` 的 `peerDependencies` 是 `sql.js: ^1.8.0` ——
`1.6.2` 本身也不满足要求。这条声明同样不会传递到下游，所以它今天的实际作用是「本仓库自己的
js 构建用它」，而不是「替消费者选版本」。

## 修法：worker 的所有权交给应用层

- `sqldelight`：新增 `SqldelightWorker`（`src/jsMain`）——只有 `install(factory)` 与
  `internal create()`；未安装时抛出可操作的错误文案（写明要声明哪两个 npm、要在哪里 install），
  而不是让打包器报一句 `Module not found`。
- `sqldelight`：`DriverFactory.js.kt` 改成 `WebWorkerDriver(SqldelightWorker.create())`，
  删掉 `org.w3c.dom.Worker` 的 import。
- `sqldelight`：`DriverFactory.wasmJs.kt` 的 `TODO()` 补上文案，并把注释掉的字面量删掉
  —— 那行注释是下一个「抄回来」的入口，留着等于留坑。
- 应用层（下游）：在自己的 js 源集里写下字面量并 install 一次：

```kotlin
// consumer's jsMain
import org.w3c.dom.Worker

fun newSqldelightWorker(): Worker =
    Worker(js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)"""))

fun installSqldelightWorker() {
    SqldelightWorker.install { newSqldelightWorker() }
}
```

```kotlin
// consumer's jsMain dependencies
api(npm("@cashapp/sqldelight-sqljs-worker", "2.4.1"))
api(npm("sql.js", "1.8.0"))   // 满足 worker 的 peerDependencies
```

还要让 `sql-wasm.wasm` 能被 worker 取到：worker 内部是 `locateFile: file => "/sql-wasm.wasm"`（**硬编码常量**，不是 `"/" + file`），所以必须把 `sql.js/dist/sql-wasm.wasm` 拷到**站点根目录**、文件名保持 `sql-wasm.wasm`。`@cashapp/sqldelight-sqljs-worker` 包里只有 `sqljs.worker.js`，没有 `.wasm`（`npm pack` 实测 4 个文件：`LICENSE.txt` / `README.md` / `package.json` / `sqljs.worker.js`）。

与 `:shared` 那次相比，这次**不需要 vendored 包**：`@cashapp/sqldelight-sqljs-worker` 是真实
发布在 npm 上的（最新稳定 `2.4.1`），消费者直接声明版本即可；`:shared` 那个
`sqlite-wasm-worker` 是仓库内 file 包、npm 上 404，所以才必须 vendored。

版本口径：npm 包版本要与 `app.cash.sqldelight:web-worker-driver` 对齐（本仓库 `gradle/libs.versions.toml` 钉的是 2.3.2，npm 上 `@cashapp/sqldelight-sqljs-worker@2.3.2` 也存在）；上文写 `2.4.1` 是以最新稳定版为例。

> js 目标允许把 `js(…)` 直接写在 lambda 里；**Kotlin/Wasm 不允许**（`Calls to 'js(code)' must be a single expression inside a top-level function body or a property initializer in Kotlin/Wasm.`）。上面的示例按顶层函数写，将来搬到 wasmJs 源集不用重写。

## 兼容性说明（改的是已发布库的公开面）

- 今天能跑通的消费者，是那些**自己声明了** `@cashapp/sqldelight-sqljs-worker` 的；
  升级后它们必须再加一次 `SqldelightWorker.install { … }`。
- 今天跑不通的消费者（没声明 npm，或者只是把库拖进 bundle 而没用到 js 驱动），
  升级后从「打包失败」变成「不调用就不用做任何事」。
- 仓库内部的 `app/webApp` 不受影响：它没有使用 SQLDelight 的 js 驱动。

## 教训

- **同构缺陷要一起修**：`sqlite-wasm-worker` 修完不等于「库把 worker 塞进 klib」这件事修完。
  判据不是「还有没有别的库这么写」，而是「还有没有别的 klib 里有这种字面量」——
  一条 `strings <all>-*.klib | grep 'new URL(' …` 扫一遍发布物，比读源码可靠。
- **上游干净不代表自己干净**：先解包上游 `-sources.jar` 确认字面量是自家代码引入的，再谈修法；
  否则很容易把 PR 提到别人的仓库去。
- 交付物里能携带的东西只有 Maven 元数据声明的那些。`api(npm(...))` 是「本仓库构建用」，
  不是「随构件交付」。
- 不要让「注释掉的坏代码」留在库里：它是最容易被下一个人抄回去的形态。