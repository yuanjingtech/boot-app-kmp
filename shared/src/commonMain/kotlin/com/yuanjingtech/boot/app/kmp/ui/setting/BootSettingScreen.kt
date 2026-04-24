package com.yuanjingtech.boot.app.kmp.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yuanjingtech.boot.app.kmp.theme.BootThemeSettingScreen
import com.yuanjingtech.boot.app.kmp.theme.BootThemeSettingScreenWithStore

@Preview
@Composable
fun BootSettingScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    Column(
        modifier = modifier,
    ) {
        BootThemeSettingScreenWithStore(modifier = Modifier.fillMaxSize())
    }
}