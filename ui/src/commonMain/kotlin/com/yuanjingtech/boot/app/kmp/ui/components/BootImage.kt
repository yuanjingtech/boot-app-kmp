package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.yuanjingtech.boot.app.kmp.ui.AsyncImageView
import com.yuanjingtech.boot.app.kmp.ui.preview.LiquidGlassPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.Material3PreviewWrapper
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper

@Composable
fun BootImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String? = null,
    placeholder: Painter? = null,
    error: Painter? = null,
    fallback: Painter? = error,
    isLoading: Boolean = false,
) {
    AsyncImageView(
        modifier = modifier,
        model = model,
        contentDescription = contentDescription,
        placeholder = placeholder,
        error = error,
        fallback = fallback,
        isLoading = isLoading,
    )
}

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootImageLiquidGlassPreview() {
    val previewHandler = AsyncImagePreviewHandler { ColorImage(Color(0xFF808080).toArgb()) }
    Column(modifier = Modifier.padding(8.dp)) {
        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            BootImage(
                model = "https://picsum.photos/400/200",
                contentDescription = "Sample image",
                modifier = Modifier.height(200.dp),
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootImageMaterial3Preview() {
    val previewHandler = AsyncImagePreviewHandler { ColorImage(Color(0xFF808080).toArgb()) }
    Column(modifier = Modifier.padding(8.dp)) {
        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            BootImage(
                model = "https://picsum.photos/400/200",
                contentDescription = "Sample image",
                modifier = Modifier.height(200.dp),
            )
        }
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootImageLoadingPreview() {
    Box(modifier = Modifier) {
        BootImage(
            model = "https://example.com/image.jpg",
            contentDescription = "Sample image",
            isLoading = true,
            modifier = Modifier,
        )
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootImageLoadingMaterial3Preview() {
    Box(modifier = Modifier) {
        BootImage(
            model = "https://example.com/image.jpg",
            contentDescription = "Sample image",
            isLoading = true,
            modifier = Modifier,
        )
    }
}