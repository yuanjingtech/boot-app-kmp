package com.yuanjingtech.boot.app.kmp.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.data.theme.BootThemeMode
import com.yuanjingtech.boot.app.kmp.data.theme.BootThemeStore
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private data class ThemeOption(
    val mode: BootThemeMode,
    val title: String,
    val description: String,
)

private val themeOptions = listOf(
    ThemeOption(BootThemeMode.FOLLOW_SYSTEM, "跟随系统", "根据系统设置自动切换主题"),
    ThemeOption(BootThemeMode.LIGHT, "浅色", "始终使用浅色主题"),
    ThemeOption(BootThemeMode.DARK, "深色", "始终使用深色主题"),
)

@Composable
fun BootThemeSettingScreen(
    modifier: Modifier = Modifier,
    selectedMode: BootThemeMode,
    onModeSelected: (BootThemeMode) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "主题设置",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        themeOptions.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedMode == option.mode,
                        onClick = { onModeSelected(option.mode) },
                        role = Role.RadioButton,
                    )
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = selectedMode == option.mode,
                    onClick = null,
                )
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        text = option.title,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = option.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
fun BootThemeSettingScreenWithStore(
    modifier: Modifier = Modifier,
) {
    val store: BootThemeStore = koinInject()
    val scope = rememberCoroutineScope()
    val selectedMode by store.themeModeFlow.collectAsState(initial = BootThemeMode.FOLLOW_SYSTEM)

    BootThemeSettingScreen(
        modifier = modifier,
        selectedMode = selectedMode,
        onModeSelected = { mode ->
            scope.launch { store.setThemeMode(mode) }
        },
    )
}

@Preview
@Composable
private fun BootThemeSettingScreenPreview() {
    MaterialTheme {
        BootThemeSettingScreen(
            selectedMode = BootThemeMode.FOLLOW_SYSTEM,
            onModeSelected = {},
        )
    }
}