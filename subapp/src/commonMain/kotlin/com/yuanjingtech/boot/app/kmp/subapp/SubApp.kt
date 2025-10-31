package com.yuanjingtech.boot.app.kmp.subapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface SubApp {
    val id: String
    val name: String
    val description: String
    fun content(): @Composable() ((modifier: Modifier) -> Unit)
}