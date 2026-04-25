package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.LgEffectConfig
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.rememberLgBarEffects

@Composable
fun LiquidGlassScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = { _ -> },
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            topBar()
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            content(PaddingValues(0.dp))
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            bottomBar()
        }
    }
}

@Composable
fun LiquidGlassScaffoldTopBar(
    modifier: Modifier = Modifier,
    backdrop: LiquidGlassBackdrop? = null,
    config: LgEffectConfig = rememberLgBarEffects(),
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .liquidGlassBackdropOrSurface(
                backdrop = backdrop,
                cornerRadius = 0.dp,
                borderAlpha = 0.15f,
                config = config.copy(blurRadius = 8.dp),
            )
    ) {
        content()
    }
}

@Composable
fun LiquidGlassScaffoldBottomBar(
    modifier: Modifier = Modifier,
    backdrop: LiquidGlassBackdrop? = null,
    config: LgEffectConfig = rememberLgBarEffects(),
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .liquidGlassBackdropOrSurface(
                backdrop = backdrop,
                cornerRadius = 28.dp,
                borderAlpha = 0.25f,
                config = config.copy(blurRadius = 6.dp),
            )
            .navigationBarsPadding()
    ) {
        content()
    }
}

@Preview
@Composable
private fun LiquidGlassScaffoldPreview() {
    MaterialTheme {
        LiquidGlassScaffold(
            topBar = {
                LiquidGlassScaffoldTopBar {
                    Text(
                        text = "Title",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            },
            bottomBar = {
                LiquidGlassScaffoldBottomBar {
                    Text(
                        text = "Bottom Bar",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            },
            content = { _ ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Scaffold Content",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        )
    }
}
