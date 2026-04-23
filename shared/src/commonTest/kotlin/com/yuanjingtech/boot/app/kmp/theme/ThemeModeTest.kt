package com.yuanjingtech.boot.app.kmp.theme

import kotlin.test.Test
import kotlin.test.assertEquals

class ThemeModeTest {

    @Test
    fun themeMode_hasThreeModes() {
        assertEquals(3, ThemeMode.entries.size)
    }

    @Test
    fun themeMode_followSystem_isFirst() {
        assertEquals(ThemeMode.FOLLOW_SYSTEM, ThemeMode.entries.first())
    }

    @Test
    fun themeMode_dark_isLast() {
        assertEquals(ThemeMode.DARK, ThemeMode.entries.last())
    }

    @Test
    fun themeMode_hasCorrectNames() {
        assertEquals("FOLLOW_SYSTEM", ThemeMode.FOLLOW_SYSTEM.name)
        assertEquals("LIGHT", ThemeMode.LIGHT.name)
        assertEquals("DARK", ThemeMode.DARK.name)
    }

    @Test
    fun themeMode_canParseFromString() {
        assertEquals(ThemeMode.FOLLOW_SYSTEM, ThemeMode.valueOf("FOLLOW_SYSTEM"))
        assertEquals(ThemeMode.LIGHT, ThemeMode.valueOf("LIGHT"))
        assertEquals(ThemeMode.DARK, ThemeMode.valueOf("DARK"))
    }
}
