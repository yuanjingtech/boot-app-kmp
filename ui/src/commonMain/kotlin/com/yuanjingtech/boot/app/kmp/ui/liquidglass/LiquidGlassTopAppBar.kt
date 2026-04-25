package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.LgEffectConfig
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.rememberLgBarEffects

@Composable
fun LiquidGlassTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    backdrop: LiquidGlassBackdrop? = null,
    navigationIcon: ImageVector? = null,
    onNavigationClick: () -> Unit = {},
    config: LgEffectConfig = rememberLgBarEffects(),
    actions: @Composable RowScope.() -> Unit = {},
) {
    val shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .liquidGlassBackdropOrSurface(
                backdrop = backdrop,
                cornerRadius = 0.dp,
                borderAlpha = 0.15f,
                config = config.copy(blurRadius = 8.dp, lensRefractionHeight = 0.dp),
            )
            .height(56.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (navigationIcon != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = "Navigation",
                        tint = Color.White,
                    )
                }
            }
            Box(modifier = Modifier.weight(1f).padding(start = if (navigationIcon != null) 0.dp else 16.dp)) {
                Text(
                    text = title,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterStart),
                )
            }
            Row(
                modifier = Modifier.background(Color.Transparent),
                content = actions,
            )
        }
    }
}
