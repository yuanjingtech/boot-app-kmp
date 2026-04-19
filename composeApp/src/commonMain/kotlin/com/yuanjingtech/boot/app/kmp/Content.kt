package com.yuanjingtech.boot.app.kmp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuanjingtech.boot.app.kmp.network.TestNetworkScreen
import com.yuanjingtech.boot.app.kmp.subapp.TestSubAppScreen
import com.yuanjingtech.boot.app.kmp.webview.WebViewDemoScreen

@Composable
fun Content(
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        WebViewDemoScreen(Modifier.fillMaxSize())
        TestSubAppScreen()
        TestNetworkScreen()
    }
}

