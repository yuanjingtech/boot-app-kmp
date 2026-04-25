package com.yuanjingtech.boot.app.kmp.ui.material3

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*

// ─── Material3Surface ────────────────────────────────────────────────────────
@Composable
fun Material3Surface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.Surface(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shadowElevation = 1.dp,
        content = content
    )
}

@Preview
@Composable
private fun Material3SurfacePreview() {
    MaterialTheme {
        Material3Surface(modifier = Modifier.fillMaxWidth()) { Text("Surface Content") }
    }
}
