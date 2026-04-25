package com.yuanjingtech.boot.app.kmp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.components.BootNavigationBar
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
            Icons.Filled.Home to "Home",
            Icons.Filled.Settings to "Settings",
        )
        Column(modifier = Modifier.fillMaxSize()) {
            BootTopAppBar(
                title = "Boot App",
                modifier = Modifier,
            )
            when (selectIndex) {
                0 -> Content(modifier = Modifier.fillMaxSize())
                else -> BootSettingScreen(modifier = Modifier.fillMaxSize())
            }
            BootNavigationBar(
                selectedIndex = selectIndex,
                onItemSelected = { selectIndex = it },
                items = items,
            )
        }
    }
}
