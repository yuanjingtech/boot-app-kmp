package com.yuanjingtech.boot.app.kmp.di

import com.yuanjingtech.boot.app.kmp.sqldelight.sqldelightModules
import org.koin.dsl.module

val bootModule = module { }
val bootModules = listOf(bootModule) + sqldelightModules