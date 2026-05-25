package com.yuanjingtech.boot.app.kmp.data.theme

import androidx.compose.ui.graphics.Color
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import org.junit.Assert.*
import org.junit.Test

class BootThemeModeTest {

    @Test
    fun bootThemeMode_hasExpectedValues() {
        val values = BootThemeMode.entries.toTypedArray()
        assertTrue(values.contains(BootThemeMode.LIGHT))
        assertTrue(values.contains(BootThemeMode.DARK))
        assertTrue(values.contains(BootThemeMode.FOLLOW_SYSTEM))
    }
}

class BootUiStyleTest {

    @Test
    fun bootUiStyle_hasExpectedValues() {
        val values = BootUiStyle.entries.toTypedArray()
        assertTrue(values.contains(BootUiStyle.LIQUID_GLASS))
        assertTrue(values.contains(BootUiStyle.MATERIAL3))
        assertTrue(values.contains(BootUiStyle.FLUENT))
    }
}