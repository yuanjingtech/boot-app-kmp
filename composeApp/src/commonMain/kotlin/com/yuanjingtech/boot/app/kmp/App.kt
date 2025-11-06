package com.yuanjingtech.boot.app.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.yuanjingtech.boot.app.kmp.demo.demoModule
import com.yuanjingtech.boot.app.kmp.demo2.demo2Module
import com.yuanjingtech.boot.app.kmp.di.bootModule
import com.yuanjingtech.boot.app.kmp.network.NetworkService
import com.yuanjingtech.boot.app.kmp.network.body
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.dsl.KoinConfiguration


@Composable
@Preview
fun App() {
    val networkService = koinInject<NetworkService>()
    LaunchedEffect("key") {
        val result = networkService.get({
            url("http://www.baidu.com")
        }).body<List<String>>()
    }
    BootApp(config = KoinConfiguration {
        printLogger()
        modules(bootModule, demoModule, demo2Module)
    }) {
        Content(Modifier.fillMaxSize())
    }
}