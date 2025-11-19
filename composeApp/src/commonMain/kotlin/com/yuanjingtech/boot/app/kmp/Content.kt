package com.yuanjingtech.boot.app.kmp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.yuanjingtech.boot.app.kmp.network.NetworkService
import com.yuanjingtech.boot.app.kmp.network.ContentType
import com.yuanjingtech.boot.app.kmp.subapp.SubAppManager
import org.koin.compose.koinInject

@Composable
fun Content(
    modifier: Modifier = Modifier.Companion,
    subAppManager: SubAppManager = koinInject()
) {
    val networkService = koinInject<NetworkService>()
    LaunchedEffect("key") {
        try {
            val result = networkService.get({
                url("https://www.baidu.com")
            }).body<List<String>>()
        } catch (e: Exception) {
            println(e.message)
        }
    }
    val apps by subAppManager.apps.collectAsState(emptyList())
    Column(
        modifier.verticalScroll(rememberScrollState())
    ) {
        apps.forEach { app -> app.content().invoke(Modifier.fillMaxWidth()) }
    }
}