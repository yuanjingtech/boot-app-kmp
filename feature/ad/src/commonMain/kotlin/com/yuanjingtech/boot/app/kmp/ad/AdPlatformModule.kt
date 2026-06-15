package com.yuanjingtech.boot.app.kmp.ad

import org.koin.core.module.Module

/**
 * 平台相关 Koin 模块 — 各平台 actual 实现:androidMain / iosMain / jvmMain / jsMain / wasmJsMain。
 */
expect val adPlatformModule: Module
