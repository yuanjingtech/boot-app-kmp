package com.yuanjingtech.boot.app.kmp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.Navigator
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
        var selectIndex by remember { mutableStateOf(0) }
        val items = listOf("Home", "Settings")
        Scaffold(
            bottomBar = {
                PrimaryTabRow(selectIndex) {
                    items.forEachIndexed { index, item ->
                        Text(
                            text = item,
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable { selectIndex = index }
                        )
                    }
                }
            }
        ) { innerPadding ->
            when (selectIndex) {
                0 -> Content(modifier = Modifier.fillMaxSize().padding(innerPadding))

                else -> BootSettingScreen(modifier = Modifier.fillMaxSize().padding(innerPadding))
            }
        }
    }
}