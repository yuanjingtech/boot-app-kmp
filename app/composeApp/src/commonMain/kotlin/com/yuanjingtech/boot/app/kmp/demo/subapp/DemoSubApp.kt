package com.yuanjingtech.boot.app.kmp.demo.subapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuanjingtech.boot.app.kmp.demo.DemoScreen
import com.yuanjingtech.boot.app.kmp.subapp.SubApp

class DemoSubApp : SubApp {
    override val id: String get() = "demo"
    override val name: String get() = "demo"
    override val description: String get() = "description"

    override fun content(): @Composable() ((modifier: Modifier) -> Unit) {
        return { modifier ->
            DemoScreen(modifier)
        }
    }
}

