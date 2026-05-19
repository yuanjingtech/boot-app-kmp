package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FluentFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = { Icons.Default.Add },
    contentDescription: String? = null,
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = colors.accent
    val contentColor = colors.accentText
    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .background(backgroundColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.padding(4.dp)) {
                    icon()
                }
            }
        }
    }
}

@Composable
fun FluentSmallFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = { Icons.Default.Add },
    contentDescription: String? = null,
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = colors.controlFill
    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .background(backgroundColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.padding(2.dp)) {
            icon()
        }
    }
}