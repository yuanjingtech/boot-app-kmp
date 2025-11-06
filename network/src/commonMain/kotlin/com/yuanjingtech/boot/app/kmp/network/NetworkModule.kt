package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.HttpClient
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> { createHttpClient() }
    single { NetworkServiceKtorImpl(get<HttpClient>()) } bind NetworkService::class
}