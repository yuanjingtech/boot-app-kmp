package com.yuanjingtech.boot.app.kmp.network

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject

@Composable
fun TestNetworkScreen(
    modifier: Modifier = Modifier.Companion.fillMaxSize(),
) {
    val networkService = koinInject<NetworkService>()
    LaunchedEffect("key") {
        try {
            val result = networkService.get({
                url("https://www.baidu.com")
            }).body<List<String>>()
        } catch (e: Exception) {
            // TODO: Replace with proper error logging system (e.g., os.log on iOS, Log on Android)
            // Avoid exposing internal error details in production logs
        }
    }
    Text("TestNetworkScreen")
}