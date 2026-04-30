package com.yuanjingtech.boot.app.kmp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.yuanjingtech.boot.app.kmp.ui.components.shimmerEffect

@Composable
fun AsyncImageView(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String? = null,
    placeholder: Painter? = null,
    error: Painter? = null,
    fallback: Painter? = error,
    isLoading: Boolean = false,
) {
    if (isLoading) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect(visible = true, cornerRadius = 8.dp),
        )
    } else {
        AsyncImage(
            modifier = modifier,
            model = model,
            contentDescription = contentDescription,
            placeholder = placeholder,
            error = error,
            fallback = fallback,
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
fun AsyncImagePreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.Red.toArgb())
    }
    MaterialTheme {
        Column {
            CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
                AsyncImageView(
                    model = "https://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960"
                )
            }
        }
    }
}