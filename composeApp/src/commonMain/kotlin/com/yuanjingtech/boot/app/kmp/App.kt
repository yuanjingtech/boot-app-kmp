package com.yuanjingtech.boot.app.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yuanjingtech.boot.app.kmp.ui.components.BootNavigationBar
import com.yuanjingtech.boot.app.kmp.ui.components.BootScaffold
import com.yuanjingtech.boot.app.kmp.ui.components.BootTopAppBar
import com.yuanjingtech.boot.app.kmp.ui.setting.BootSettingScreen
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.includes
import org.koin.plugin.module.dsl.koinConfiguration

@Composable
@Preview
fun App() {
    BootApplication(config = KoinConfiguration {
        printLogger()
        includes(koinConfiguration<BootApp>())
    }) {
        var selectIndex by remember { mutableIntStateOf(0) }
        val items = listOf(
            Icons.Filled.Home to BootStrings.tabItemMain(),
            Icons.Filled.Settings to BootStrings.tabItemSetting(),
        )
        BootScaffold(
            topBar = {
                BootTopAppBar(
                    title = BootStrings.appName(),
                    modifier = Modifier,
                )
            },
            bottomBar = {
                BootNavigationBar(
                    selectedIndex = selectIndex,
                    onItemSelected = { selectIndex = it },
                    items = items,
                )
            },
            content = { padding ->
                when (selectIndex) {
                    0 -> Content(modifier = Modifier.fillMaxSize().padding(padding))
                    else -> BootSettingScreen(modifier = Modifier.fillMaxSize().padding(padding))
                }
            }
        )
    }
}
