package com.yuanjingtech.boot.app.kmp.webview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface IWebViewScreen {
    fun content(): @Composable ((Modifier, WebViewState) -> Unit)
}