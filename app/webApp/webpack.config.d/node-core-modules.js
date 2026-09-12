/*
 * Webpack 5 dropped the automatic Node.js core-module polyfills, so any
 * `require("fs" | "path" | "crypto" | "os")` left in a browser bundle is a
 * hard `Module not found` error. The Kotlin/JS web bundle of `:app:webApp`
 * still contains a few of them, all behind a dead Node-only branch:
 *
 *   - `skiko.mjs` (Compose Multiplatform web runtime loader) guards them with
 *     Emscripten's `if (false) { ... require("fs") ... }` environment check.
 *   - `sql.js`, pulled in by `@cashapp/sqldelight-sqljs-worker`, only touches
 *     them when it detects a Node host.
 *   - `okio` (through Ktor) does the same for its Node file-system support.
 *
 * None of those branches are reachable in a browser, so map the requests to
 * empty modules instead of failing the dev-server bundle
 * (`jsBrowserDevelopmentWebpack`, and therefore `jsBrowserDevelopmentRun`).
 *
 * Keep this list in sync with the resolution errors reported by webpack.
 */
config.resolve = config.resolve || {}
config.resolve.fallback = config.resolve.fallback || {}
config.resolve.fallback["fs"] = false
config.resolve.fallback["path"] = false
config.resolve.fallback["crypto"] = false
config.resolve.fallback["os"] = false
