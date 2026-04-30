package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
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