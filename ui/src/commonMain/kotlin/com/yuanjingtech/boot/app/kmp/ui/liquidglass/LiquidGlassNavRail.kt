package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.LgEffectConfig
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.rememberLgBarEffects

private val DefaultNavRailItems: List<Pair<ImageVector, String>> = listOf(
    Icons.Filled.Home to "Home",
    Icons.Filled.Search to "Search",
    Icons.Filled.Person to "Profile",
    Icons.Filled.Settings to "Settings",
)

@Composable
fun LiquidGlassNavRail(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    backdrop: LiquidGlassBackdrop? = null,
    items: List<Pair<ImageVector, String>> = DefaultNavRailItems,
    config: LgEffectConfig = rememberLgBarEffects(),
) {
    Column(
        modifier = modifier
            .liquidGlassBackdropOrSurface(
                backdrop = backdrop,
                cornerRadius = 16.dp,
                borderAlpha = 0.2f,
                config = config.copy(blurRadius = 6.dp),
            )
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items.forEachIndexed { index, (icon, label) ->
            val selected = selectedIndex == index
            LiquidGlassNavRailItem(
                icon = icon,
                label = label,
                selected = selected,
                onClick = { onItemSelected(index) },
            )
        }
    }
}

@Composable
private fun LiquidGlassNavRailItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val alpha = if (selected) 1f else 0.55f

    Column(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White.copy(alpha = alpha),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White.copy(alpha = alpha),
        )
    }
}

@Preview
@Composable
private fun LiquidGlassNavRailPreview() {
    MaterialTheme {
        Column {
            var selected by mutableIntStateOf(0)
            LiquidGlassNavRail(
                selectedIndex = selected,
                onItemSelected = { selected = it },
            )
        }
    }
}
