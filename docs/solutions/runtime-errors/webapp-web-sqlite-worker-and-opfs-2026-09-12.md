---
title: ":app:webApp crashes at startup — createWorker is not defined, and the SQLite web worker was never wired up"
date: "2026-09-12"
category: docs/solutions/runtime-errors/
module: boot-app-kmp/app/webApp
problem_type: runtime_error
component: web-target
symptoms:
  - "kotlin.js.JsException: createWorker is not defined / ReferenceError: createWorker is not defined"
  - "'Error was captured in composition.' immediately after Koin finishes starting, with a stack through bootPlatformModule -> getDatabaseBuilder() -> WebWorkerSQLiteDriver"
  - "The JS/Wasm web app renders a blank page; nothing but Koin's log output reaches the console"
  - "jsBrowserDevelopmentWebpack fails with \"Can't resolve '@sqlite.org/sqlite-wasm' in '<repo>/sqlite-wasm-worker/worker'\""
  - "After the worker is wired up: \"SQLiteException: sqlite3.oo1.OpfsDb is not a constructor\" + \"Cannot install OPFS: Missing SharedArrayBuffer and/or Atomics\""
root_cause: unimplemented_platform_actual
resolution_type: code_fix
severity: high
related_components:
  - shared/src/jsMain/kotlin/com/yuanjingtech/boot/app/kmp/Platform.js.kt
  - shared/src/wasmJsMain/kotlin/com/yuanjingtech/boot/app/kmp/Platform.wasmJs.kt
  - sqlite-wasm-worker/worker/worker.js
  - sqlite-wasm-worker/worker/package.json
  - sqlite-wasm-worker/build.gradle.kts
  - app/webApp/webpack.config.d/node-core-modules.js
  - app/webApp/webpack.config.d/npm-workspace-resolution.js
tags:
  - kmp
  - webapp
  - kotlin-js
  - kotlin-wasm
  - webpack
  - web-worker
  - sqlite
  - sqlite-wasm
  - opfs
  - room3
  - koin
---

# `:app:webApp` crashes at startup — `createWorker is not defined`

## Problem

Loading the web app produced a blank page and this in the browser console:

```
[INFO] [Koin] Started 8 definitions in 0.6 ms
[INFO] [Koin] Started 13 definitions in 1.1 ms
Error was captured in composition.
kotlin.js.JsException: createWorker is not defined
ReferenceError: createWorker is not defined
    at com.yuanjingtech.boot.app.kmp.createWorker_$external_fun (webApp.js:17500:72)
    at com.yuanjingtech.boot.app.kmp.bootPlatformModule$lambda$lambda.invoke
    at org.koin.core.instance.SingleInstanceFactory.get
```

`BootApplication` wraps every screen in `BootAppTheme(themeStore = koinInject())`, so the theme
store — and therefore `BootDatabase` and its driver — is created on the very first composition.
The failure therefore happened before *any* UI could appear, on both the `js` and `wasmJs`
targets.

## Root Cause

`shared/src/{jsMain,wasmJsMain}/.../Platform.*.kt` declared the driver's worker as a call to an
undefined **global**:

```kotlin
@Suppress("ACTUAL_FUNCTION_WITH_DEFAULT_ARGUMENTS")
external fun createWorker(): Worker
```

`external fun` compiles to a call to a JavaScript global named `createWorker`. Nothing in the
project — no script, no `index.html` tag, no webpack entry — ever defined it, so every attempt to
build the database threw `ReferenceError`. The vendor placeholder for the worker
(`shared/src/{jsMain,wasmJsMain}/resources/worker/`) was an empty directory, and the local npm
package that was supposed to provide it was never connected.

Four further problems were hidden behind that first one, and all of them had to be fixed before
the database worked:

1. **The vendored worker implemented a different protocol.** `sqlite-wasm-worker/worker/worker.js`
   expected `{type, id, payload}` messages, while `androidx.sqlite.driver.web.WebWorkerSQLiteDriver`
   sends `{id, data: {cmd, ...}}`. It also misused the SQLite API — it compiled the wasm module with
   `WebAssembly.compileStreaming(...)` and then called the high-level `sqlite3.oo1.OpfsDb` JS API on
   the compiled module, and it referenced its `databases` map before it was initialized.
2. **Its npm dependency was never installed.** `contract/worker/package.json` declared
   `@sqlite.org/sqlite-wasm`, but npm does not install the dependencies of a package that is only
   *linked* into the workspace (`npm("sqlite-wasm-worker", file("worker"))`); `build/js/node_modules`
   had no `@sqlite.org` at all, and `npm install` reported "up to date".
3. **Webpack could not resolve the worker's import.** The linked package resolves to its real path
   `<repo>/sqlite-wasm-worker/worker/worker.js`, which is outside any `node_modules`, so
   `import sqlite3InitModule from '@sqlite.org/sqlite-wasm'` failed even after step 2.
4. **OPFS required server headers the dev server does not send.** The upstream worker opens the
   database with `sqlite3.oo1.OpfsDb`, whose VFS needs `SharedArrayBuffer`/`Atomics`, i.e. a
   cross-origin-isolated page:

   ```
   Ignoring inability to install OPFS sqlite3_vfs: Cannot install OPFS: Missing
   SharedArrayBuffer and/or Atomics. The server must emit the COOP/COEP response headers
   ```
   → `Error: SQLiteException: sqlite3.oo1.OpfsDb is not a constructor`

## Fix

### 1. Create the worker where webpack can see it

`Platform.js.kt` (Kotlin/JS) — the `new URL(..., import.meta.url)` form is the pattern webpack
statically analyses to emit the worker as a separate chunk:

```kotlin
private fun createSQLiteWorker(): Worker =
    Worker(js("""new URL("sqlite-wasm-worker/worker.js", import.meta.url)"""))
```

`Platform.wasmJs.kt` (Kotlin/Wasm) — `org.w3c.dom.Worker` has no usable constructor there, so the
whole expression goes to JavaScript; the `new URL(..., import.meta.url)` has to stay literal:

```kotlin
private fun createSQLiteWorker(): Worker =
    js("""new Worker(new URL("sqlite-wasm-worker/worker.js", import.meta.url))""")
```

Both mirror the pattern used by AndroidX itself in `sqlite:sqlite-web-worker-test`.

### 2. Replace the worker with the real AndroidX implementation

`sqlite-wasm-worker/worker/worker.js` is now the reference implementation of the driver protocol,
vendored from
[`sqlite/sqlite-web-worker-test/web-worker/worker.js`](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:sqlite/sqlite-web-worker-test/web-worker/worker.js)
(the matching npm package `@androidx/sqlite-web-worker` is not published yet).
`worker/package.json` was bumped to the version AndroidX pairs it with, `^3.50.1-build1`.

### 3. Make npm actually install the worker's dependency

Declared explicitly in `sqlite-wasm-worker/build.gradle.kts` so it lands in the workspace root:

```kotlin
implementation(npm("sqlite-wasm-worker", file("worker")))
implementation(npm("@sqlite.org/sqlite-wasm", "3.50.1-build1"))
```

### 4. Teach webpack where the workspace `node_modules` is

Added `app/webApp/webpack.config.d/npm-workspace-resolution.js`:

```js
const path = require("path")
config.resolve.modules = [
  ...(config.resolve.modules || ["node_modules"]),
  path.resolve(__dirname, "../../node_modules"),
]
```

`__dirname` is the npm package directory (`build/js/packages/boot-app-kmp-app-webApp`, or
`build/wasm/...` for the Wasm target), so `../../node_modules` is the workspace root. The entry is
additive: the default `node_modules` walk-up still runs first.

### 5. Use the OPFS SAH-pool VFS instead of the legacy OPFS VFS

The vendored worker now installs `opfs-sahpool` and opens databases through it, falling back to
`:memory:` when OPFS is unavailable:

```js
const pool = await instance.installOpfsSAHPoolVfs({ name: 'opfs-sahpool' })
opfsDatabaseFactory = pool.OpfsSAHPoolDb
```

The SAH-pool VFS provides the same OPFS persistence using only sync access handles. It needs **no**
COOP/COEP headers, which keeps the app deployable on plain static hosting and, importantly, keeps
the project's own cross-origin WebView demo iframe (`https://yuanjingtech.github.io/privancy/`)
working — `COEP: require-corp` would have blocked it.

`app/webApp/webpack.config.d/node-core-modules.js` (added earlier for the build) is what lets the
worker chunk and the main bundle resolve `fs`/`path`/`crypto`/`os`, which only exist behind dead
Node-only branches in `sql.js`, `skiko.mjs` and `okio`.

## Verification

Verified by serving the app and loading it in headless Chrome, capturing `pageerror`, console
errors and OPFS state.

| Target | Result |
| --- | --- |
| `js` (`jsBrowserDevelopmentRun`, port 8080) | no page errors, UI renders, OPFS `.opfs-sahpool` created and kept across two page loads |
| `wasmJs` (dev bundle served statically) | no page errors, UI renders, `WASM WebView: iframe loaded`, database opens |

Also confirmed the worker chunk and its wasm binary are emitted for both targets:

```
asset sqlite-wasm-worker_worker_worker_js.js                     49.7 KiB
asset acda28ce0493138989bb.wasm                                 836 KiB   <- sqlite3.wasm
```

## Notes

- `sqlite3InitModule()` still logs "Ignoring inability to install OPFS sqlite3_vfs …". That is
  expected: it is sqlite-wasm failing to auto-register the *legacy* OPFS VFS, which the app does not
  use.
- The worker is the only place that opens the database, so a page reload picks up the persisted
  OPFS data; the fallback `:memory:` path is used only when OPFS is genuinely unavailable.
- `sqldelight/src/{jsMain,wasmJsMain}` has an unrelated, currently unused
  `DriverFactory.createDriver` (the `wasmJs` one is still `TODO()`); the app's Room3 database uses
  the `androidx.sqlite` web worker driver instead.
- `gradle.properties` sets `org.gradle.caching=true`; on this APFS volume (`noowners`) Gradle
  intermittently fails to read/set POSIX file modes ("Could not get file mode", "Could not set file
  mode 644"), which shows up as spurious task failures in long Wasm builds. Retrying the task
  succeeds.
