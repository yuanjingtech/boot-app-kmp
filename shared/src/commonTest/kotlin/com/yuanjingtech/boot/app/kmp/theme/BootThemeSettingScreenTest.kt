package com.yuanjingtech.boot.app.kmp.theme

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.yuanjingtech.boot.app.kmp.data.theme.BootThemeMode
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class BootThemeSettingScreenTest {

    @Test
    fun bootThemeSettingScreen_displaysTitle() = runComposeUiTest {
        setContent {
            BootThemeSettingScreen(
                selectedMode = BootThemeMode.FOLLOW_SYSTEM,
                onModeSelected = {},
            )
        }
        onNodeWithText("主题设置").assertIsDisplayed()
    }

    @Test
    fun bootThemeSettingScreen_displaysAllOptions() = runComposeUiTest {
        setContent {
            BootThemeSettingScreen(
                selectedMode = BootThemeMode.FOLLOW_SYSTEM,
                onModeSelected = {},
            )
        }
        onNodeWithText("跟随系统").assertIsDisplayed()
        onNodeWithText("浅色").assertIsDisplayed()
        onNodeWithText("深色").assertIsDisplayed()
    }

    @Test
    fun bootThemeSettingScreen_displaysOptionDescriptions() = runComposeUiTest {
        setContent {
            BootThemeSettingScreen(
                selectedMode = BootThemeMode.FOLLOW_SYSTEM,
                onModeSelected = {},
            )
        }
        onNodeWithText("根据系统设置自动切换主题").assertIsDisplayed()
        onNodeWithText("始终使用浅色主题").assertIsDisplayed()
        onNodeWithText("始终使用深色主题").assertIsDisplayed()
    }

    @Test
    fun bootThemeSettingScreen_callsOnModeSelected_whenOptionClicked() = runComposeUiTest {
        var selectedMode: BootThemeMode = BootThemeMode.FOLLOW_SYSTEM

        setContent {
            BootThemeSettingScreen(
                selectedMode = selectedMode,
                onModeSelected = { mode -> selectedMode = mode },
            )
        }

        onNodeWithText("浅色").performClick()
        kotlin.test.assertEquals(BootThemeMode.LIGHT, selectedMode)
    }
}
