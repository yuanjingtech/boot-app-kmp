package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.LgEffectConfig
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.rememberLgBottomSheetEffects

@Composable
fun LiquidGlassModalBottomSheet(
    modifier: Modifier = Modifier,
    backdrop: LiquidGlassBackdrop? = null,
    config: LgEffectConfig = rememberLgBottomSheetEffects(),
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .liquidGlassBackdropOrSurface(
                backdrop = backdrop,
                cornerRadius = 28.dp,
                borderAlpha = 0.3f,
                config = config,
            )
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Color.White.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(2.dp),
                    )
                    .padding(horizontal = 32.dp, vertical = 4.dp),
            )
        }
        Column(content = content)
    }
}

@Preview
@Composable
private fun LiquidGlassModalBottomSheetPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LiquidGlassModalBottomSheet {
                Text("Sheet Content Item 1")
                Text("Sheet Content Item 2")
                Text("Sheet Content Item 3")
            }
        }
    }
}
