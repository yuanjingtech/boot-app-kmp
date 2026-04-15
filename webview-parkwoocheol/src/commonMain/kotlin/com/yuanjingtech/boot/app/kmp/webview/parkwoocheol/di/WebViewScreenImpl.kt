package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuanjingtech.boot.app.kmp.webview.IWebViewScreen
import com.yuanjingtech.boot.app.kmp.webview.parkwoocheol.WebViewScreen
import com.yuanjingtech.boot.app.kmp.webview.WebViewState

class WebViewScreenImpl : IWebViewScreen {
    override fun content(): @Composable (Modifier, WebViewState) -> Unit =
        { modifier, stateHolder ->
            WebViewScreen(modifier, stateHolder)
        }
}