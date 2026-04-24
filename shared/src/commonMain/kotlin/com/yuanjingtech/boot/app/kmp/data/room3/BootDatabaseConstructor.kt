package com.yuanjingtech.boot.app.kmp.data.room3

import androidx.room3.RoomDatabaseConstructor
import kotlin.Suppress

@Suppress("KotlinNoActualForExpect")
expect object BootDatabaseConstructor : RoomDatabaseConstructor<BootDatabase> {
    override fun initialize(): BootDatabase
}