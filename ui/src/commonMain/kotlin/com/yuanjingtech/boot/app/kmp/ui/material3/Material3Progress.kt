package com.yuanjingtech.boot.app.kmp.ui.material3

import androidx.compose.foundation.layout.*
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

// ─── Material3Progress ───────────────────────────────────────────────────────
@Composable
fun Material3CircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color = MaterialTheme.colorScheme.primary
) {
    CircularProgressIndicator(
        modifier = modifier,
        progress = { progress ?: 0f },
        color = color,
        strokeWidth = 4.dp
    )
}

@Composable
fun Material3LinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null
) {
    LinearProgressIndicator(
        modifier = modifier,
        progress = { progress ?: 0f }
    )
}

@Preview
@Composable
private fun Material3ProgressPreview() {
    MaterialTheme {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Material3CircularProgressIndicator()
            Material3CircularProgressIndicator(progress = 0.6f)
            Material3LinearProgressIndicator()
            Material3LinearProgressIndicator(progress = 0.4f)
        }
    }
}
