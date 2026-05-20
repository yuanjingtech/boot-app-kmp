package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentLightPreviewWrapper
import kotlin.math.roundToInt

@Composable
fun FluentSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
) {
    val colors = LocalFluentColors.current
    var width by remember { mutableFloatStateOf(0f) }

    val normalizedValue = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)
    val trackColor = if (enabled) colors.controlFill else colors.controlFillDisabled
    val activeTrackColor = if (enabled) colors.accent else colors.textDisabled
    val thumbColor = if (enabled) colors.accent else colors.textDisabled

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .onSizeChanged { width = it.width.toFloat() }
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                detectHorizontalDragGestures { change, _ ->
                    change.consume()
                    val newNormalized = (change.position.x / width).coerceIn(0f, 1f)
                    val rawValue = valueRange.start + (valueRange.endInclusive - valueRange.start) * newNormalized
                    val steppedValue = if (steps > 0) {
                        val stepSize = 1f / steps
                        (rawValue / stepSize).roundToInt() * stepSize
                    } else rawValue
                    onValueChange(steppedValue.coerceIn(valueRange.start, valueRange.endInclusive))
                }
            },
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.matchParentSize()) {
            val h = size.height
            val w = size.width
            val cy = h / 2
            val trackH = 4.dp.toPx()
            val thumbR = 8.dp.toPx()

            drawRoundRect(
                color = trackColor,
                topLeft = androidx.compose.ui.geometry.Offset(0f, cy - trackH / 2),
                size = androidx.compose.ui.geometry.Size(w, trackH),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(trackH / 2),
            )
            drawRoundRect(
                color = activeTrackColor,
                topLeft = androidx.compose.ui.geometry.Offset(0f, cy - trackH / 2),
                size = androidx.compose.ui.geometry.Size(w * normalizedValue, trackH),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(trackH / 2),
            )
            drawCircle(
                color = thumbColor,
                radius = thumbR,
                center = androidx.compose.ui.geometry.Offset(w * normalizedValue, cy),
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentSliderPreview() {
    var value by remember { mutableFloatStateOf(0.5f) }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Value: ${(value * 100).roundToInt()}%")
        FluentSlider(
            value = value,
            onValueChange = { value = it },
        )
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentLightPreviewWrapper::class)
private fun FluentSliderLightPreview() {
    var value by remember { mutableFloatStateOf(0.7f) }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Light: ${(value * 100).roundToInt()}%")
        FluentSlider(
            value = value,
            onValueChange = { value = it },
            valueRange = 0f..100f,
        )
    }
}