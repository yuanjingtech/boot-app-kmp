package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.LgEffectConfig
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.rememberLgBarEffects

@Composable
fun LiquidGlassBottomAppBar(
    modifier: Modifier = Modifier,
    backdrop: LiquidGlassBackdrop? = null,
    config: LgEffectConfig = rememberLgBarEffects(),
    content: @Composable RowScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .liquidGlassBackdropOrSurface(
                backdrop = backdrop,
                cornerRadius = 28.dp,
                borderAlpha = 0.25f,
                config = config.copy(blurRadius = 6.dp),
            )
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            content = content,
        )
    }
}

@Preview
@Composable
private fun LiquidGlassBottomAppBarPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(0.dp)) {
            LiquidGlassBottomAppBar {
                Text("Action 1")
                Text("Action 2")
            }
        }
    }
}
