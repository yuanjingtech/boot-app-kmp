package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.HttpClient
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val networkModule = module {
    single<HttpClient> { createHttpClient() }
    single<NetworkServiceKtorImpl>() bind NetworkService::class
}