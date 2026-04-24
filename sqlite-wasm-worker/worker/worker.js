// https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:sqlite/sqlite-web-worker-test/web-worker/worker.js
// 实现 WebWorkerSQLiteDriver 协议 — 负责数据库初始化和 SQL 执行

const POST_MESSAGE_TYPE_DB_OPEN = 0;
const POST_MESSAGE_TYPE_DB_OPEN_RESULT = 1;
const POST_MESSAGE_TYPE_DB_EXECUTE = 2;
const POST_MESSAGE_TYPE_DB_EXECUTE_RESULT = 3;

let sqlite3 = null;

async function loadSqlite() {
  if (sqlite3) return sqlite3;
  const wasmUrl = new URL("@sqlite.org/sqlite-wasm/sqlite-wasm.wasm", import.meta.url);
  sqlite3 = await WebAssembly.compileStreaming(fetch(wasmUrl));
  return sqlite3;
}

self.onmessage = async function (event) {
  const { type, id, payload } = event.data;

  try {
    switch (type) {
      case POST_MESSAGE_TYPE_DB_OPEN:
        await loadSqlite();
        const vfs = await sqlite3.createVfs('opfs');
        const db = new sqlite3.oo1.OpfsDb(vfs, payload.path, 'c');
        db.open();
        const dbId = Math.floor(Math.random() * 0xFFFFFFFF);
        databases.set(dbId, db);
        self.postMessage({ type: POST_MESSAGE_TYPE_DB_OPEN_RESULT, id, payload: { dbId } });
        break;

      case POST_MESSAGE_TYPE_DB_EXECUTE: {
        const db = databases.get(payload.dbId);
        db.exec(payload.sql);
        const changes = db.getRowsModified();
        self.postMessage({ type: POST_MESSAGE_TYPE_DB_EXECUTE_RESULT, id, payload: { changes } });
        break;
      }
    }
  } catch (err) {
    self.postMessage({ type: type + 1, id, error: err.message });
  }
};

const databases = new Map();