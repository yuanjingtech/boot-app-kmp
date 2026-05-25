package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class LiquidGlassTextTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun liquidGlassText_rendersCorrectly() {
        composeTestRule.setContent {
            LiquidGlassText(text = "Test")
        }
        composeTestRule.onNodeWithText("Test").assertExists()
    }

    @Test
    fun liquidGlassText_withCustomColor() {
        composeTestRule.setContent {
            LiquidGlassText(
                text = "Colored",
                color = Color.Blue
            )
        }
        composeTestRule.onNodeWithText("Colored").assertExists()
    }

    @Test
    fun liquidGlassText_withOnDarkBackground() {
        composeTestRule.setContent {
            LiquidGlassText(
                text = "Dark Theme",
                onDarkBackground = Color.White
            )
        }
        composeTestRule.onNodeWithText("Dark Theme").assertExists()
    }

    @Test
    fun liquidGlassText_withOnLightBackground() {
        composeTestRule.setContent {
            LiquidGlassText(
                text = "Light Theme",
                onLightBackground = Color.Black
            )
        }
        composeTestRule.onNodeWithText("Light Theme").assertExists()
    }
}