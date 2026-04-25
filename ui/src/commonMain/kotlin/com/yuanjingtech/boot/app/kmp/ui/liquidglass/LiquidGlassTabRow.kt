package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.LgEffectConfig
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.rememberLgBarEffects

@Composable
fun LiquidGlassTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    backdrop: LiquidGlassBackdrop? = null,
    tabTitles: List<String> = emptyList(),
    config: LgEffectConfig = rememberLgBarEffects(),
    onTabSelected: (Int) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .liquidGlassBackdropOrSurface(
                backdrop = backdrop,
                cornerRadius = 16.dp,
                borderAlpha = 0.2f,
                config = config.copy(blurRadius = 4.dp, lensRefractionHeight = 0.dp),
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tabTitles.forEachIndexed { index, title ->
            val selected = selectedTabIndex == index
            val alpha = if (selected) 1f else 0.55f
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Transparent)
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = title,
                    color = Color.White.copy(alpha = alpha),
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}
