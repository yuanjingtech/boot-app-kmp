package com.yuanjingtech.boot.app.kmp.di

import com.yuanjingtech.boot.app.kmp.sqldelight.sqldelightModule
import org.koin.dsl.module

val bootModule = module {
    includes(sqldelightModule)
}