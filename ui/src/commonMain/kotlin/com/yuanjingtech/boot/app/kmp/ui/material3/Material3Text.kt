package com.yuanjingtech.boot.app.kmp.ui.material3

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
fun Material3Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    fontSize: TextUnit = TextStyle.Default.fontSize,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    textDecoration: TextDecoration = TextDecoration.None,
    textAlign: TextAlign = TextAlign.Start,
    lineHeight: TextUnit = TextStyle.Default.lineHeight,
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
private fun Material3TextPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Material3Text(text = "Heading", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Material3Text(text = "Body text", fontSize = 16.sp)
            Material3Text(text = "Caption", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Material3Text(text = "Bold text", fontWeight = FontWeight.Bold)
            Material3Text(text = "Italic text", fontStyle = FontStyle.Italic)
            Material3Text(text = "Underlined text", textDecoration = TextDecoration.Underline)
            Material3Text(text = "Centered text", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Material3Text(text = "Max lines text that is very long and should be truncated when it exceeds the maximum number of lines", maxLines = 2)
        }
    }
}