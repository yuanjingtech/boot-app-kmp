package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LiquidGlassCircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color = Color.White,
    strokeWidth: Dp = 4.dp,
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .liquidGlassSurface(cornerRadius = 24.dp, borderAlpha = 0.2f),
        contentAlignment = Alignment.Center,
    ) {
        if (progress != null) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val diameter = size.minDimension
                val radius = (diameter - strokeWidth.toPx()) / 2
                val center = Offset(size.width / 2, size.height / 2)
                drawCircle(
                    color = color.copy(alpha = 0.2f),
                    radius = radius,
                    center = center,
                    style = Stroke(width = strokeWidth.toPx()),
                )
                drawArc(
                    color = color.copy(alpha = 0.8f),
                    startAngle = -90f,
                    sweepAngle = progress * 360f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
                )
            }
        } else {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val diameter = size.minDimension
                val radius = (diameter - strokeWidth.toPx()) / 2
                val center = Offset(size.width / 2, size.height / 2)
                drawCircle(
                    color = color.copy(alpha = 0.2f),
                    radius = radius,
                    center = center,
                    style = Stroke(width = strokeWidth.toPx()),
                )
            }
        }
    }
}

@Composable
fun LiquidGlassLinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color = Color.White,
    strokeWidth: Dp = 4.dp,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(strokeWidth)
            .clip(CircleShape)
            .liquidGlassSurface(cornerRadius = strokeWidth / 2, borderAlpha = 0.2f)
            .padding(horizontal = 2.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        if (progress != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(strokeWidth - 4.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.8f)),
            )
        }
    }
}

@Preview
@Composable
private fun LiquidGlassProgressPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            LiquidGlassCircularProgressIndicator()
            LiquidGlassCircularProgressIndicator(progress = 0.6f)
            LiquidGlassLinearProgressIndicator()
            LiquidGlassLinearProgressIndicator(progress = 0.4f)
        }
    }
}
