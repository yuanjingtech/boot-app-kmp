package com.yuanjingtech.boot.app.kmp.ui.material3

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class Material3TextTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun material3Text_rendersCorrectly() {
        composeTestRule.setContent {
            Material3Text(text = "Material Design")
        }
        composeTestRule.onNodeWithText("Material Design").assertExists()
    }

    @Test
    fun material3Text_withCustomStyle() {
        composeTestRule.setContent {
            Material3Text(
                text = "Styled",
                style = Material3TextStyle.TitleLarge
            )
        }
        composeTestRule.onNodeWithText("Styled").assertExists()
    }
}