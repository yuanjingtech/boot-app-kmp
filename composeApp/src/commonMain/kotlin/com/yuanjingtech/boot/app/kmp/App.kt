package com.yuanjingtech.boot.app.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuanjingtech.boot.app.kmp.demo.demoModule
import com.yuanjingtech.boot.app.kmp.demo2.demo2Module
import com.yuanjingtech.boot.app.kmp.di.bootModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.dsl.KoinConfiguration


@Composable
@Preview
fun App() {
    BootApp(config = KoinConfiguration {
        printLogger()
        modules(bootModule, demoModule, demo2Module)
    }) {
        Content(Modifier.fillMaxSize())
    }
}