package com.yuanjingtech.boot.app.kmp.data.theme

import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeDao
import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeSettings
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class BootThemeStore(private val themeDao: ThemeDao) {

    val themeModeFlow: Flow<BootThemeMode> = themeDao.getThemeSettings().map { settings ->
        try {
            settings?.let { BootThemeMode.valueOf(it.themeMode) } ?: BootThemeMode.FOLLOW_SYSTEM
        } catch (e: IllegalArgumentException) {
            BootThemeMode.FOLLOW_SYSTEM
        }
    }

    val uiStyleFlow: Flow<BootUiStyle> = themeDao.getThemeSettings().map { settings ->
        try {
            settings?.uiStyle?.let { BootUiStyle.valueOf(it) } ?: BootUiStyle.LIQUID_GLASS
        } catch (e: IllegalArgumentException) {
            BootUiStyle.LIQUID_GLASS
        }
    }

    suspend fun setThemeMode(mode: BootThemeMode) {
        val current = themeDao.getThemeSettings().first()
        themeDao.insertOrUpdate(
            ThemeSettings(
                id = 1,
                themeMode = mode.name,
                uiStyle = current?.uiStyle ?: "LIQUID_GLASS",
            )
        )
    }

    suspend fun setUiStyle(style: BootUiStyle) {
        val current = themeDao.getThemeSettings().first()
        themeDao.insertOrUpdate(
            ThemeSettings(
                id = 1,
                themeMode = current?.themeMode ?: "FOLLOW_SYSTEM",
                uiStyle = style.name,
            )
        )
    }
}