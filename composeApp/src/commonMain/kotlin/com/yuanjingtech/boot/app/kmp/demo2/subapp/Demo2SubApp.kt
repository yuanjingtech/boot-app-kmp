package com.yuanjingtech.boot.app.kmp.demo2.subapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuanjingtech.boot.app.kmp.demo.DemoScreen
import com.yuanjingtech.boot.app.kmp.subapp.SubApp

class Demo2SubApp : SubApp {
    override val id: String get() = "demo2"
    override val name: String get() = "demo2"
    override val description: String get() = "description"

    override fun content(): @Composable() ((modifier: Modifier) -> Unit) {
        return { modifier ->
            DemoScreen(modifier)
        }
    }
}

