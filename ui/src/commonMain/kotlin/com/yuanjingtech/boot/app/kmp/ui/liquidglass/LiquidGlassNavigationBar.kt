package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.LgEffectConfig
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.rememberLgBarEffects

private val DefaultNavItems: List<Pair<ImageVector, String>> = listOf(
    Icons.Filled.Home to "Home",
    Icons.Filled.Search to "Search",
    Icons.Filled.Person to "Profile",
    Icons.Filled.Settings to "Settings",
)

@Composable
fun LiquidGlassNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    backdrop: LiquidGlassBackdrop? = null,
    items: List<Pair<ImageVector, String>> = DefaultNavItems,
    config: LgEffectConfig = rememberLgBarEffects(),
    height: Dp = 80.dp,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .liquidGlassBackdropOrSurface(
                backdrop = backdrop,
                cornerRadius = 28.dp,
                borderAlpha = 0.3f,
                config = config,
            )
            .navigationBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, (icon, label) ->
            val selected = selectedIndex == index
            LiquidGlassNavItem(
                icon = icon,
                label = label,
                selected = selected,
                onClick = { onItemSelected(index) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun RowScope.LiquidGlassNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val alpha = if (selected) 1f else 0.6f

    Column(
        modifier = modifier
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White.copy(alpha = alpha),
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = alpha),
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
