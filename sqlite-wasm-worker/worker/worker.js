/**
 * Copyright 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Reference implementation of the `androidx.sqlite.driver.web.WebWorkerSQLiteDriver`
 * message protocol (that class documents the full protocol).
 *
 * Vendored from the AndroidX repository, module `sqlite:sqlite-web-worker-test`:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:sqlite/sqlite-web-worker-test/web-worker/worker.js
 *
 * It lives here because the matching npm package (`@androidx/sqlite-web-worker`) has
 * not been published. Keep it in sync with `androidx.sqlite:sqlite-web` when that
 * dependency is upgraded.
 *
 * Local adaptation: upstream opens databases with `sqlite3.oo1.OpfsDb`, which uses the
 * original OPFS VFS. That VFS relies on `SharedArrayBuffer`/`Atomics`, so the hosting
 * page must be cross-origin isolated with COOP/COEP response headers — otherwise it
 * fails with "sqlite3.oo1.OpfsDb is not a constructor". The SQLite "SAH pool" VFS
 * provides the same OPFS persistence using only sync access handles, needs no special
 * headers, and additionally keeps working inside cross-origin iframes. This worker
 * therefore prefers the SAH pool VFS and only falls back to an in-memory database when
 * OPFS itself is unavailable.
 * See https://sqlite.org/wasm/doc/trunk/persistence.md
 */
import sqlite3InitModule from '@sqlite.org/sqlite-wasm';

let sqlite3 = null;

// `OpfsSAHPoolDatabase` constructor, or null when OPFS is unavailable.
let opfsDatabaseFactory = null;

// Maps to track active database connections and prepared statements by their unique IDs.
const databases = new Map(); // stores databaseId -> SQLiteDbObject
const statements = new Map(); // stores statementId -> SQLiteStatementObject

// Counters to generate unique IDs for new database connections and statements.
let nextDatabaseId = 0;
let nextStatementId = 0;

function openRequest(id, requestData) {
    try {
        const newDatabaseId = nextDatabaseId++;
        const newDatabase = opfsDatabaseFactory
            ? new opfsDatabaseFactory(requestData.fileName)
            : new sqlite3.oo1.DB(':memory:', 'ct');
        databases.set(newDatabaseId, newDatabase);
        postMessage({'id': id, data: {'databaseId': newDatabaseId}});
    } catch (error) {
        postMessage({'id': id, error: error.message});
    }
}

function prepareRequest(id, requestData) {
    try {
        const newStatementId = nextStatementId++;
        const resultData = {
            'statementId': newStatementId,
            'parameterCount': 0,
            'columnNames': []
        };
        const database = databases.get(requestData.databaseId);
        if (!database) {
            postMessage({'id': id, error: "Invalid database ID: " + requestData.databaseId});
            return;
        }
        const statement = database.prepare(requestData.sql);
        statements.set(newStatementId, statement);
        resultData.parameterCount = sqlite3.capi.sqlite3_bind_parameter_count(statement);
        for (let i = 0; i < statement.columnCount; i++) {
            resultData.columnNames.push(sqlite3.capi.sqlite3_column_name(statement, i));
        }
        postMessage({'id': id, data: resultData});
    } catch (error) {
        postMessage({'id': id, error: error.message});
    }
}

function stepRequest(id, requestData) {
    const statement = statements.get(requestData.statementId);
    if (!statement) {
        postMessage({'id': id, error: "Invalid statement ID: " + requestData.statementId});
        return;
    }
    try {
        const resultData = {
            'rows': [],
            'columnTypes': []
        };
        statement.reset();
        statement.clearBindings();
        for (let i = 0; i < requestData.bindings.length; i++) {
            statement.bind(i + 1, requestData.bindings[i]);
        }
        while (statement.step()) {
            if (!resultData.columnTypes.length) {
                for (let i = 0; i < statement.columnCount; i++) {
                    resultData.columnTypes.push(sqlite3.capi.sqlite3_column_type(statement, i));
                }
            }
            resultData.rows.push(statement.get([]));
        }
        postMessage({'id': id, data: resultData});
    } catch (error) {
        postMessage({'id': id, error: error.message});
    }
}

function closeRequest(id, requestData) {
    if (requestData.statementId !== undefined && requestData.statementId != null) {
        const statement = statements.get(requestData.statementId);
        if (!statement) {
            postMessage({'id': id, error: "Invalid statement ID: " + requestData.statementId});
            return;
        }
        try {
            statement.finalize();
            statements.delete(requestData.statementId);
        } catch (error) {
            postMessage({'id': id, error: error.message});
        }
    }

    if (requestData.databaseId !== undefined && requestData.databaseId != null) {
        const database = databases.get(requestData.databaseId);
        if (!database) {
            postMessage({'id': id, error: "Invalid database ID: " + requestData.databaseId});
            return;
        }
        try {
            database.close();
            databases.delete(requestData.databaseId);
        } catch (error) {
            postMessage({'id': id, error: error.message});
        }
    }
}

// A map that links command names (strings) to their respective handler functions.
const commandMap = {
    'open': openRequest,
    'prepare': prepareRequest,
    'step': stepRequest,
    'close': closeRequest,
};

function handleMessage(e) {
    const requestMsg = e.data;
    if (!Object.hasOwn(requestMsg, 'data') && requestMsg.data == null) {
        postMessage(
            {'id': requestMsg.id, 'error': "Invalid request, missing 'data'."}
        );
        return;
    }
    if (!Object.hasOwn(requestMsg.data, 'cmd') && requestMsg.data.cmd == null) {
        postMessage(
            {'id': requestMsg.id, 'error': "Invalid request, missing 'cmd'."}
        );
        return;
    }
    const command = requestMsg.data.cmd;
    const requestHandler = commandMap[command];
    if (requestHandler) {
        requestHandler(requestMsg.id, requestMsg.data);
    } else {
        postMessage(
            {'id': requestMsg.id, 'error': "Invalid request, unknown command: '" + command + "'."}
        );
    }
}

const messageQueue = [];
onmessage = (e) => {
    if (!sqlite3) {
        messageQueue.push(e);
    } else {
        handleMessage(e);
    }
};

sqlite3InitModule().then(async (instance) => {
    sqlite3 = instance;
    try {
        const pool = await instance.installOpfsSAHPoolVfs({ name: 'opfs-sahpool' });
        opfsDatabaseFactory = pool.OpfsSAHPoolDb;
    } catch (error) {
        console.warn(
            '[sqlite-wasm-worker] OPFS is unavailable, falling back to an in-memory ' +
            'database; data will not survive a page reload.',
            error
        );
    }
    while (messageQueue.length > 0) {
        handleMessage(messageQueue.shift());
    }
});
