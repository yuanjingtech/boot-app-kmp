package com.yuanjingtech.boot.app.kmp.data.theme

import com.yuanjingtech.boot.app.kmp.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeStore {
    var themeModeStateFlow: Flow<ThemeMode> = MutableStateFlow(ThemeMode.FOLLOW_SYSTEM).asStateFlow()
}