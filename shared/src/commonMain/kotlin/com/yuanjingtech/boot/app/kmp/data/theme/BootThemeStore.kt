package com.yuanjingtech.boot.app.kmp.data.theme

import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeDao
import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BootThemeStore(private val themeDao: ThemeDao) {

    val themeModeFlow: Flow<BootThemeMode> = themeDao.getThemeSettings().map { settings ->
        try {
            settings?.let { BootThemeMode.valueOf(it.themeMode) } ?: BootThemeMode.FOLLOW_SYSTEM
        } catch (e: IllegalArgumentException) {
            BootThemeMode.FOLLOW_SYSTEM
        }
    }

    suspend fun setThemeMode(mode: BootThemeMode) {
        themeDao.insertOrUpdate(ThemeSettings(themeMode = mode.name))
    }
}