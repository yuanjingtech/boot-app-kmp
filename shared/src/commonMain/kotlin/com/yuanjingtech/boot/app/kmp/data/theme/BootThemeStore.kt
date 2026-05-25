package com.yuanjingtech.boot.app.kmp.data.theme

import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeDao
import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeSettings
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class BootThemeStore(private val themeDao: ThemeDao) {

    private val defaultSettings = ThemeSettings(
        id = 1,
        themeMode = BootThemeMode.FOLLOW_SYSTEM.name,
        uiStyle = BootUiStyle.LIQUID_GLASS.name
    )

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
        val current = themeDao.getThemeSettings().first() ?: defaultSettings
        themeDao.insertOrUpdate(
            current.copy(themeMode = mode.name)
        )
    }

    suspend fun setUiStyle(style: BootUiStyle) {
        val current = themeDao.getThemeSettings().first() ?: defaultSettings
        themeDao.insertOrUpdate(
            current.copy(uiStyle = style.name)
        )
    }
}