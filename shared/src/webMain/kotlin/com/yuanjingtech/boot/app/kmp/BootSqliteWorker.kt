package com.yuanjingtech.boot.app.kmp

import org.w3c.dom.Worker

/**
 * Injection point for the SQLite web worker that backs [bootPlatformModule] on the
 * `js` and `wasmJs` targets.
 *
 * ## Why this library cannot ship the worker itself
 *
 * `androidx.sqlite.driver.web.WebWorkerSQLiteDriver` needs a [Worker] whose script the
 * **consuming** bundler resolves. The only pattern webpack recognises is
 * `new Worker(new URL("…", import.meta.url))`, and that literal ends up inside the
 * published klib. Webpack resolves it while parsing this library's JavaScript, whether
 * or not the application ever touches the database — so a hard-coded default turns every
 * downstream web build into
 *
 * ```
 * Module not found: Error: Can't resolve 'sqlite-wasm-worker/worker.js'
 * ```
 *
 * …even for applications that never call [bootPlatformModule]. Two facts make the
 * hard-coded default unfixable from inside this module:
 *
 * 1. `sqlite-wasm-worker` is a **local file package** in the boot repository; it has
 *    never been published, so no consumer can resolve the specifier.
 * 2. npm dependencies of a Kotlin library **do not travel with its Maven publication**:
 *    the generated `.pom` and `.module` files contain no npm metadata at all. Verified
 *    on `com.yuanjingtech.boot.app.kmp:shared-wasm-js:0.0.2-alpha.40`, where
 *    `grep -c 'npm|sqlite-wasm-worker'` on both files returns `0`, while the klib itself
 *    contains the worker URL literal.
 *
 * ## What an application must do
 *
 * The application owns the worker and installs a factory once, before the Koin graph
 * resolves the database:
 *
 * ```kotlin
 * // webMain source set (shared by the js and wasmJs targets):
 * internal expect fun newSqliteWorker(): Worker
 *
 * // Once, before anything resolves the database:
 * BootSqliteWorker.install { newSqliteWorker() }
 * ```
 *
 * The factory must be an `expect`/`actual` pair rather than a lambda that calls
 * `js(...)` inline: Kotlin/Wasm only accepts `js(code)` as a single expression in a
 * top-level function body or a property initializer, so `install { js("...") }`
 * fails to compile with
 * `Calls to 'js(code)' must be a single expression inside a top-level function body
 * or a property initializer in Kotlin/Wasm`. See `:app:webApp/src/{jsMain,wasmJsMain}`
 * for both actuals — the shapes differ (`Worker(js("""new URL(...)"""))` on JS,
 * `js("""new Worker(new URL(...))"""))` on Wasm).
 *
 * It also declares the matching npm dependencies in its own build script, so the
 * specifier resolves from the application's `node_modules`. `:app:webApp` in this
 * repository is a complete, working example.
 *
 * Applications that never resolve the database do not have to install anything — with
 * the literal gone from the klib, their web bundle builds without the npm package.
 */
public object BootSqliteWorker {
    private var factory: (() -> Worker)? = null

    /**
     * Installs the factory used to create the SQLite web worker.
     *
     * Call it once during start-up. A later call replaces the previous factory instead of
     * creating a second worker.
     */
    public fun install(factory: () -> Worker) {
        this.factory = factory
    }

    internal fun create(): Worker {
        val factory = checkNotNull(this.factory) {
            "No SQLite web worker is installed. Call BootSqliteWorker.install { ... } before " +
                "resolving the database, and declare the npm dependency that provides the worker " +
                "script. See :app:webApp in the boot-app-kmp repository for a complete example."
        }
        return factory()
    }
}