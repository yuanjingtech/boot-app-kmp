package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentLightPreviewWrapper

@Composable
fun FluentFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String? = null,
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = colors.accent
    val contentColor = colors.accentText

    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
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
                    androidx.compose.material3.Icon(icon, contentDescription = contentDescription, tint = contentColor)
                }
            }
        }
    }
}

@Composable
fun FluentSmallFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String? = null,
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = colors.controlFill

    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.padding(2.dp)) {
            androidx.compose.material3.Icon(icon, contentDescription = contentDescription, tint = colors.textPrimary)
        }
    }
}

@Composable
fun FluentLargeFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String? = null,
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = colors.accent
    val contentColor = colors.accentText

    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.material3.Icon(icon, contentDescription = contentDescription, tint = contentColor)
    }
}

@Composable
fun FluentExtendedFAB(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = colors.accent
    val contentColor = colors.accentText

    Row(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        androidx.compose.material3.Icon(icon, contentDescription = null, tint = contentColor)
        androidx.compose.material3.Text(
            text = text,
            color = contentColor,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentFABPreview() {
    FluentFAB(onClick = {})
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentSmallFABPreview() {
    FluentSmallFAB(onClick = {})
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentLargeFABPreview() {
    FluentLargeFAB(onClick = {})
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentExtendedFABPreview() {
    FluentExtendedFAB(onClick = {}, text = "Create")
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentLightPreviewWrapper::class)
private fun FluentFABLightPreview() {
    FluentExtendedFAB(onClick = {}, text = "New Item", icon = Icons.Default.Edit)
}