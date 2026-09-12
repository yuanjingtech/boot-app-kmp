/*
 * The SQLite web worker lives in the local npm package `sqlite-wasm-worker`
 * (`sqlite-wasm-worker/worker`), which npm links into the workspace root as a
 * symlink. Webpack resolves the real path of that symlink
 * (`<repo>/sqlite-wasm-worker/worker/worker.js`) and then tries to resolve the
 * worker's own `import ... from '@sqlite.org/sqlite-wasm'` by walking up from
 * there — a directory that is outside any `node_modules`, so the import fails
 * even though npm installed the package at the workspace root:
 *
 *   Module not found: Error: Can't resolve '@sqlite.org/sqlite-wasm'
 *     in '<repo>/sqlite-wasm-worker/worker'
 *
 * Add the workspace `node_modules` (the directory this webpack build runs from,
 * i.e. `build/js/node_modules` for the JS target and `build/wasm/node_modules`
 * for the Wasm target) as an extra absolute lookup directory. This is additive:
 * the default `node_modules` walk-up still runs first.
 */
const path = require("path")

const workspaceNodeModules = path.resolve(__dirname, "../../node_modules")

config.resolve = config.resolve || {}
config.resolve.modules = [...(config.resolve.modules || ["node_modules"]), workspaceNodeModules]
