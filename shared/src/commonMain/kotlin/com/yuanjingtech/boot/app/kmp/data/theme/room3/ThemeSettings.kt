package com.yuanjingtech.boot.app.kmp.data.theme.room3

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "theme_settings")
data class ThemeSettings(
    @PrimaryKey
    val id: Int = 1,
    val themeMode: String = "FOLLOW_SYSTEM",
    val uiStyle: String = "LIQUID_GLASS",
)