package com.yuanjingtech.boot.app.kmp.theme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class BootThemeSettingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bootThemeSettingScreen_displaysTitle() {
        composeTestRule.setContent {
            BootThemeSettingScreen(
                selectedMode = ThemeMode.FOLLOW_SYSTEM,
                onModeSelected = {}
            )
        }
        composeTestRule.onNodeWithText("主题设置").assertIsDisplayed()
    }

    @Test
    fun bootThemeSettingScreen_displaysAllOptions() {
        composeTestRule.setContent {
            BootThemeSettingScreen(
                selectedMode = ThemeMode.FOLLOW_SYSTEM,
                onModeSelected = {}
            )
        }
        composeTestRule.onNodeWithText("跟随系统").assertIsDisplayed()
        composeTestRule.onNodeWithText("浅色").assertIsDisplayed()
        composeTestRule.onNodeWithText("深色").assertIsDisplayed()
    }

    @Test
    fun bootThemeSettingScreen_displaysOptionDescriptions() {
        composeTestRule.setContent {
            BootThemeSettingScreen(
                selectedMode = ThemeMode.FOLLOW_SYSTEM,
                onModeSelected = {}
            )
        }
        composeTestRule.onNodeWithText("根据系统设置自动切换主题").assertIsDisplayed()
        composeTestRule.onNodeWithText("始终使用浅色主题").assertIsDisplayed()
        composeTestRule.onNodeWithText("始终使用深色主题").assertIsDisplayed()
    }

    @Test
    fun bootThemeSettingScreen_callsOnModeSelected_whenOptionClicked() {
        var selectedMode: ThemeMode = ThemeMode.FOLLOW_SYSTEM

        composeTestRule.setContent {
            BootThemeSettingScreen(
                selectedMode = selectedMode,
                onModeSelected = { mode -> selectedMode = mode }
            )
        }

        composeTestRule.onNodeWithText("浅色").performClick()
        assert(selectedMode == ThemeMode.LIGHT)
    }
}
