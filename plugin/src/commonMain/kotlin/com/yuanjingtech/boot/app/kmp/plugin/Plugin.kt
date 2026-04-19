package com.yuanjingtech.boot.app.kmp.plugin

import dev.whyoleg.sweetspi.Service
import org.koin.core.module.Module

@Service
interface Plugin {
    @Suppress("unused")
    val module: Module

}