package com.yuanjingtech.boot.app.kmp

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication

@KoinApplication
@Configuration
@ComponentScan("com.yuanjingtech.boot.app.kmp.**")
object BootApp
