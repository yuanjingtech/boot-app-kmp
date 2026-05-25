package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import org.junit.Rule
import org.junit.Test

class BootTextTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bootText_rendersCorrectly() {
        composeTestRule.setContent {
            BootText(text = "Boot Component")
        }
        composeTestRule.onNodeWithText("Boot Component").assertExists()
    }

    @Test
    fun bootText_withLiquidGlassStyle() {
        composeTestRule.setContent {
            LocalUiStyle(value = BootUiStyle.LIQUID_GLASS) {
                BootText(text = "LiquidGlass Style")
            }
        }
        composeTestRule.onNodeWithText("LiquidGlass Style").assertExists()
    }

    @Test
    fun bootText_withMaterial3Style() {
        composeTestRule.setContent {
            LocalUiStyle(value = BootUiStyle.MATERIAL3) {
                BootText(text = "Material3 Style")
            }
        }
        composeTestRule.onNodeWithText("Material3 Style").assertExists()
    }
}