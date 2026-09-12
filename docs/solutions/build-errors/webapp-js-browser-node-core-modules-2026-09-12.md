---
title: ":app:webApp jsBrowserDevelopmentWebpack fails on unresolved Node.js core modules"
date: "2026-09-12"
category: docs/solutions/build-errors/
module: boot-app-kmp/app/webApp
problem_type: build_error
component: web-target
symptoms:
  - "Execution failed for task ':app:webApp:jsBrowserDevelopmentWebpack'"
  - "Module not found: Error: Can't resolve 'path' in '.../build/js/node_modules/sql.js/dist'"
  - "Module not found: Error: Can't resolve 'fs' / 'crypto' in '.../build/js/node_modules/sql.js/dist'"
  - "Module not found: Error: Can't resolve 'os' / 'path' in '.../build/js/packages/boot-app-kmp-app-webApp/kotlin'"
  - "webpack 5.108.1 compiled with 2 errors and 4 warnings"
  - "'jsBrowserDevelopmentRun' therefore never starts the dev server"
  - "jsBrowserProductionWebpack only warns and still succeeds, which hides the problem"
root_cause: webpack5_node_polyfill_removal
resolution_type: code_fix
severity: high
related_components:
  - app/webApp/webpack.config.d/node-core-modules.js
  - app/webApp/webpack.config.d/watch.js
  - sqldelight/build.gradle.kts
tags:
  - kmp
  - kotlin-js
  - wasm
  - webpack
  - compose-multiplatform
  - sqldelight
  - sql-js
  - skiko
  - okio
  - webapp
---

# `:app:webApp` `jsBrowserDevelopmentWebpack` fails on unresolved Node.js core modules

## Problem

Running the web app through the Kotlin/JS dev server fails while bundling:

```
> Task :app:webApp:jsBrowserDevelopmentWebpack FAILED
Module not found: Error: Can't resolve 'path' in '.../build/js/node_modules/sql.js/dist'
Module not found: Error: Can't resolve 'fs'   in '.../build/js/node_modules/sql.js/dist'
Module not found: Error: Can't resolve 'crypto' in '.../build/js/node_modules/sql.js/dist'
Module not found: Error: Can't resolve 'os'   in '.../build/js/packages/boot-app-kmp-app-webApp/kotlin'
Module not found: Error: Can't resolve 'path' in '.../build/js/packages/boot-app-kmp-app-webApp/kotlin'
webpack 5.108.1 compiled with 2 errors and 4 warnings
```

Because `jsBrowserDevelopmentRun` depends on `jsBrowserDevelopmentWebpack`, the web dev
server never starts. `:app:webApp:wasmJsBrowserDevelopmentWebpack` is unaffected.

## Symptoms

- `./gradlew :app:webApp:jsBrowserDevelopmentRun` fails before the server binds a port.
- `./gradlew :app:webApp:build` **succeeds**, because in `mode: production` webpack only
  reports these same requests as warnings. That asymmetry is what makes the failure
  look intermittent.
- Only the `js` target is affected; `wasmJs` bundles cleanly.

## Root Cause

Webpack 5 removed the automatic Node.js core-module polyfills. Any statically visible
`require("fs" | "path" | "crypto" | "os")` in a browser bundle is now a hard error. The
Kotlin/JS graph of `:app:webApp` still contains three such call sites, and all of them
sit behind a dead Node-only branch:

| Source | Node-only requests | Why it is in the bundle |
| --- | --- | --- |
| `sql.js` | `fs`, `path`, `crypto` | pulled in by `@cashapp/sqldelight-sqljs-worker` (`sqldelight` `jsMain`) |
| `skiko.mjs` | `fs`, `path`, `crypto`, `url` | Compose Multiplatform web runtime loader; guarded by Emscripten's `if (false) { ... }` check |
| `okio` | `os`, `path` | transitive Ktor dependency; Node file-system support |

None of those branches are reachable in a browser, so the requests should resolve to
empty modules rather than fail the bundle.

## Fix

Added `app/webApp/webpack.config.d/node-core-modules.js`:

```js
config.resolve = config.resolve || {}
config.resolve.fallback = config.resolve.fallback || {}
config.resolve.fallback["fs"] = false
config.resolve.fallback["path"] = false
config.resolve.fallback["crypto"] = false
config.resolve.fallback["os"] = false
```

`webpack.config.d/` is read by the Kotlin Gradle plugin for every webpack task of the
module, so the file covers both `js` and `wasmJs` (harmless for `wasmJs`, which already
resolved cleanly).

## Verification

Run each target separately — combining production and development webpack tasks in one
invocation trips an unrelated Gradle implicit-dependency check between
`wasmJsDevelopmentExecutableCompileSync` and `wasmJsBrowserProductionWebpack`.

```
./gradlew :app:webApp:jsBrowserDevelopmentWebpack      # BUILD SUCCESSFUL, 0 errors
./gradlew :app:webApp:wasmJsBrowserDevelopmentWebpack  # BUILD SUCCESSFUL
./gradlew :app:webApp:build                            # BUILD SUCCESSFUL
./gradlew :app:webApp:jsBrowserDevelopmentRun          # dev server serves on :8080
```

The bundle now lists the aliases as ignored optional modules:

```
optional modules 45 bytes [optional]
  os (ignored) 15 bytes [optional] [built] [code generated]
  path (ignored) 15 bytes [optional] [built] [code generated]
  crypto (ignored) 15 bytes [optional] [built] [code generated]
```

## Related: stale `kotlin-js-store` lock files

A separate failure can block every JS/Wasm build before webpack even runs:

```
Execution failed for task ':kotlinStorePackageLock'.
> Lock file was changed. Run the `kotlinUpgradePackageLock` task to actualize lock file
Execution failed for task ':kotlinWasmStorePackageLock'.
> Lock file was changed. Run the `kotlinWasmUpgradePackageLock` task to actualize lock file
```

`kotlin-js-store/` is git-ignored (see the `.gitignore` comment), so after a dependency
bump each working copy keeps a stale lock file that no longer matches the freshly
generated one. Actualize it, or delete the directory to let the plugin recreate it:

```
./gradlew kotlinUpgradePackageLock kotlinWasmUpgradePackageLock
```

## Follow-up

- The runtime failures that this build fix exposed are documented separately in
  `docs/solutions/runtime-errors/webapp-web-sqlite-worker-and-opfs-2026-09-12.md`.
- `sqldelight/src/wasmJsMain/.../DriverFactory.wasmJs.kt` still throws `TODO()`, and its
  `createDriver` is not called from anywhere. That is a **separate, currently unused**
  database stack: the app's Room3 `BootDatabase` goes through the `androidx.sqlite`
  web worker driver in `shared`, which now works on both web targets. Note that
  `@cashapp/sqldelight-sqljs-worker/sqljs.worker.js` loads `sql-wasm.wasm` from `/` and
  `sqlite-wasm-worker/worker/worker.js` copies nothing outside the worker chunk, so wire
  up `copy-webpack-plugin` (already declared as `devNpm("copy-webpack-plugin", "9.1.0")`
  in `sqldelight/build.gradle.kts`) before using that stack on the web.
- `:app:composeApp` still declares `binaries.executable()` for `js` and `wasmJs` after the
  web entry point moved to `:app:webApp`. It builds, but emits a dead `composeApp.js`
  bundle; the `binaries.executable()` calls can be dropped once the targets only need to
  act as libraries for `:app:webApp`. If that module is ever bundled again, it needs
  `webpack.config.d/npm-workspace-resolution.js` too, because it also pulls
  `shared`'s worker reference into its module graph.
- On this repository's external APFS volume (`noowners`), a long-running Gradle daemon can
  accumulate a stale VFS: builds then fail with bogus
  `NoSuchFileException` / `Could not set file mode 644` errors on files that plainly exist.
  `./gradlew --stop` clears it.
