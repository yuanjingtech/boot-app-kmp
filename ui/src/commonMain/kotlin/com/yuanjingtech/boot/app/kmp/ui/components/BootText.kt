package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.*
import com.yuanjingtech.boot.app.kmp.ui.preview.*

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassText
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3Text
import com.yuanjingtech.boot.app.kmp.ui.preview.LiquidGlassPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.Material3PreviewWrapper

@Composable
fun BootText(
    text: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    loadingWidthFraction: Float = 0.6f,
    color: Color? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    textDecoration: TextDecoration = TextDecoration.None,
    textAlign: TextAlign = TextAlign.Start,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassText(
            text = text,
            modifier = modifier,
            isLoading = isLoading,
            loadingWidthFraction = loadingWidthFraction,
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            maxLines = maxLines,
        )
        BootUiStyle.MATERIAL3 -> Material3Text(
            text = text,
            modifier = modifier,
            isLoading = isLoading,
            loadingWidthFraction = loadingWidthFraction,
            color = color ?: MaterialTheme.colorScheme.onSurface,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            maxLines = maxLines,
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootTextLiquidGlassPreview() {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        BootText(text = "Heading", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        BootText(text = "Body text", fontSize = 16.sp)
        BootText(text = "Caption", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
        BootText(text = "Bold text", fontWeight = FontWeight.Bold)
        BootText(text = "Italic text", fontStyle = FontStyle.Italic)
        BootText(text = "Underlined text", textDecoration = TextDecoration.Underline)
        BootText(text = "Centered text", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        BootText(text = "Max lines text", maxLines = 2)
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootTextMaterial3Preview() {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        BootText(text = "Heading", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        BootText(text = "Body text", fontSize = 16.sp)
        BootText(text = "Caption", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        BootText(text = "Bold text", fontWeight = FontWeight.Bold)
        BootText(text = "Italic text", fontStyle = FontStyle.Italic)
        BootText(text = "Underlined text", textDecoration = TextDecoration.Underline)
        BootText(text = "Centered text", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        BootText(text = "Max lines text that is very long and should be truncated when it exceeds the maximum number of lines", maxLines = 2)
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootTextLoadingPreview() {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        BootText(text = "Title", fontSize = 20.sp, fontWeight = FontWeight.Bold, isLoading = true)
        BootText(text = "Description", fontSize = 16.sp, isLoading = true)
        BootText(text = "Caption", fontSize = 12.sp, isLoading = true, loadingWidthFraction = 0.4f)
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootTextLoadingMaterial3Preview() {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        BootText(text = "Title", fontSize = 20.sp, fontWeight = FontWeight.Bold, isLoading = true)
        BootText(text = "Description", fontSize = 16.sp, isLoading = true)
        BootText(text = "Caption", fontSize = 12.sp, isLoading = true, loadingWidthFraction = 0.4f)
    }
}