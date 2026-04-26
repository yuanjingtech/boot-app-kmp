package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LiquidGlassText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    textDecoration: TextDecoration = TextDecoration.None,
    textAlign: TextAlign = TextAlign.Start,
    lineHeight: TextUnit = 24.sp,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
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
}

@Preview
@Composable
private fun LiquidGlassTextPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            LiquidGlassText(text = "Heading", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            LiquidGlassText(text = "Body text", fontSize = 16.sp)
            LiquidGlassText(text = "Caption", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
            LiquidGlassText(text = "Bold text", fontWeight = FontWeight.Bold)
            LiquidGlassText(text = "Italic text", fontStyle = FontStyle.Italic)
            LiquidGlassText(text = "Underlined text", textDecoration = TextDecoration.Underline)
            LiquidGlassText(text = "Centered text", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            LiquidGlassText(text = "Max lines text that is very long and should be truncated when it exceeds the maximum number of lines", maxLines = 2)
        }
    }
}