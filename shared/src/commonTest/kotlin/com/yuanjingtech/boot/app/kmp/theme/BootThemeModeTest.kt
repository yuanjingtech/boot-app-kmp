package com.yuanjingtech.boot.app.kmp.theme

import com.yuanjingtech.boot.app.kmp.data.theme.BootThemeMode
import kotlin.test.Test
import kotlin.test.assertEquals

class BootThemeModeTest {

    @Test
    fun themeMode_hasThreeModes() {
        assertEquals(3, BootThemeMode.entries.size)
    }

    @Test
    fun themeMode_followSystem_isFirst() {
        assertEquals(BootThemeMode.FOLLOW_SYSTEM, BootThemeMode.entries.first())
    }

    @Test
    fun themeMode_dark_isLast() {
        assertEquals(BootThemeMode.DARK, BootThemeMode.entries.last())
    }

    @Test
    fun themeMode_hasCorrectNames() {
        assertEquals("FOLLOW_SYSTEM", BootThemeMode.FOLLOW_SYSTEM.name)
        assertEquals("LIGHT", BootThemeMode.LIGHT.name)
        assertEquals("DARK", BootThemeMode.DARK.name)
    }

    @Test
    fun themeMode_canParseFromString() {
        assertEquals(BootThemeMode.FOLLOW_SYSTEM, BootThemeMode.valueOf("FOLLOW_SYSTEM"))
        assertEquals(BootThemeMode.LIGHT, BootThemeMode.valueOf("LIGHT"))
        assertEquals(BootThemeMode.DARK, BootThemeMode.valueOf("DARK"))
    }
}
