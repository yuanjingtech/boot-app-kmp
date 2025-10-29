package com.yuanjingtech.boot.app.kmp.sqldelight

import org.koin.core.module.Module

expect val platformModule: Module

val sqldelightModules: List<Module> = listOf(platformModule)