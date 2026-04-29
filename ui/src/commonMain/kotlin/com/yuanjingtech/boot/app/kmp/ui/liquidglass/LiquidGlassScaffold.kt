package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.AsyncImageView
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.LgEffectConfig
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.rememberLgBarEffects

@Composable
fun LiquidGlassScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = { _ -> },
) {
    val density = LocalDensity.current
    var topBarSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }
    var bottomBarSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }
    val backdrop = rememberLiquidGlassBackdrop()
    Scaffold() { paddingValues ->
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .liquidGlassBackdrop(backdrop)
            ) {
                content(
                    paddingValues.plus(
                        PaddingValues(
                            0.dp,
                            with(density) { topBarSize.height.toDp() },
                            0.dp,
                            with(density) { bottomBarSize.height.toDp() },
                        )
                    )
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.TopCenter),
            ) {
                Row(
                    Modifier.fillMaxWidth()
                        .onSizeChanged({ topBarSize = it })
                        .liquidGlassBackdropCanvas({
                            CircleShape
                        })
                ) {
                    topBar()
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .navigationBarsPadding()
                    .align(BottomCenter),
                contentAlignment = Center,
            ) {
                Row(
                    Modifier.fillMaxWidth()
                        .onSizeChanged({ bottomBarSize = it })
                        .liquidGlassBackdropCanvas({
                            CircleShape
                        })
                ) {
                    bottomBar()
                }
            }
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

@Preview(
    showSystemUi = true,
    showBackground = true,
)
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
                    Row {
                        Text(
                            text = "Bottom Bar",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Bottom Bar",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            },
            content = { paddingValues ->
                Box(modifier = Modifier.fillMaxSize().background(Color.Gray).padding(0.dp)) {
                    AsyncImageView(
                        modifier = Modifier.fillMaxSize(),
                        model = "https://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280"
                    )
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
