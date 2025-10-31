package com.yuanjingtech.boot.app.kmp.subapp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SubAppManager(initApps: List<SubApp>) {
    private val _apps = MutableStateFlow(initApps)
    val apps: StateFlow<List<SubApp>> = _apps.asStateFlow()
}