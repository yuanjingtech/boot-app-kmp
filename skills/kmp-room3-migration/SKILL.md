---
name: kmp-room3-migration
description: "Migrates a KMP project to Room 3.x with @ConstructedBy and RoomDatabaseConstructor expect/actual pattern."
license: MIT
metadata:
  author: "lotosbin"
  version: "1.0.0"
user-invocable: true
disable-model-invocation: false
---

# KMP Room 3 Migration Skill

## Problem (from SO 78858784)

After upgrading to Room `2.7.0-alpha06`+, KMP database instantiation requires implementing `RoomDatabaseConstructor`.

The error when KSP auto-generates `actual object` but you also write one manually:

```
Error 1: [generated] 'actual object MyDatabaseCtor' has no corresponding expected declaration
Error 2: Redeclaration: actual object MyDatabaseCtor
Error 3: [androidMain] 'actual object MyDatabaseCtor' has no corresponding expected declaration
```

**Root cause**: Room KSP generates BOTH `expect` and `actual` in commonMain metadata. If you write `expect object` in your source AND also write platform `actual object` implementations, the KSP-generated `actual` conflicts with your manual `actual`.

## Solution: Manual expect + Manual actuals (alpha03)

Room `3.0.0-alpha03` KSP does NOT auto-generate actual implementations. You must provide:

1. `expect object MyDatabaseCtor` in `commonMain` (suppress with `@Suppress("KotlinNoActualForExpect")`)
2. `actual object MyDatabaseCtor` in every platform source set
3. `@ConstructedBy(MyDatabaseCtor::class)` on the `@Database` class

## Files to Create

### commonMain: `MyDatabaseConstructor.kt`

```kotlin
package com.example.data

import androidx.room3.RoomDatabaseConstructor
import kotlin.Suppress

@Suppress("KotlinNoActualForExpect")
expect object MyDatabaseConstructor : RoomDatabaseConstructor<MyDatabase> {
    override fun initialize(): MyDatabase
}
```

### commonMain: `MyDatabase.kt`

```kotlin
package com.example.data

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(entities = [MyEntity::class], version = 1)
@ConstructedBy(MyDatabaseConstructor::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun myDao(): MyDao
}
```

### androidMain: `MyDatabaseConstructor.android.kt`

```kotlin
package com.example.data

import androidx.room3.RoomDatabaseConstructor

actual object MyDatabaseConstructor : RoomDatabaseConstructor<MyDatabase> {
    actual override fun initialize(): MyDatabase = createMyDatabase()
}
```

### jvmMain: `MyDatabaseConstructor.jvm.kt`

```kotlin
package com.example.data

import androidx.room3.RoomDatabaseConstructor

actual object MyDatabaseConstructor : RoomDatabaseConstructor<MyDatabase> {
    actual override fun initialize(): MyDatabase = createMyDatabase()
}
```

### iosMain: `MyDatabaseConstructor.ios.kt`

```kotlin
package com.example.data

import androidx.room3.RoomDatabaseConstructor

actual object MyDatabaseConstructor : RoomDatabaseConstructor<MyDatabase> {
    actual override fun initialize(): MyDatabase {
        throw UnsupportedOperationException(
            "Room3 BundledSQLiteDriver is not supported on iOS. " +
                "Use SQLDelight for iOS database operations."
        )
    }
}
```

### jsMain / wasmJsMain

Same pattern as iosMain — throw `UnsupportedOperationException`.

## build.gradle.kts

Add `kspCommonMainMetadata` so Room KSP runs on commonMain metadata and resolves `@ConstructedBy`:

```kotlin
dependencies {
    // Room KSP for commonMain metadata — generates expect/actual BootDatabaseConstructor
    kspCommonMainMetadata(libs.room3.compiler)
}
```

Also ensure `room3.runtime` is in commonMain and `sqlite-bundled` is in androidMain/jvmMain:

```kotlin
commonMain.dependencies {
    api(libs.room3.runtime)
}
androidMain.dependencies {
    implementation(libs.sqlite.bundled)
}
jvmMain.dependencies {
    implementation(libs.sqlite.bundled)
}
```

## Reference

- https://developer.android.com/kotlin/multiplatform/room#creating-database
- https://stackoverflow.com/questions/78858784/roomdatabaseconstructor-on-kotlinmultiplatform-has-no-corresponding-expected-dec
