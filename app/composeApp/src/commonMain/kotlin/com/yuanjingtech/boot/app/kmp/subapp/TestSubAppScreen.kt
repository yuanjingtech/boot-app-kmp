package com.yuanjingtech.boot.app.kmp.subapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject

@Composable
fun TestSubAppScreen(
    modifier: Modifier = Modifier.Companion.fillMaxSize(),
    subAppManager: SubAppManager = koinInject()
) {
    val apps by subAppManager.apps.collectAsState(emptyList())
    Column(
        modifier.verticalScroll(rememberScrollState())
    ) {
        apps.forEach { app -> app.content().invoke(Modifier.Companion.fillMaxWidth()) }
    }
}