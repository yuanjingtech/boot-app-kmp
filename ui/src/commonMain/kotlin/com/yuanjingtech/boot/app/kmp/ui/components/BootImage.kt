package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.yuanjingtech.boot.app.kmp.ui.AsyncImageView

@Composable
fun BootImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String? = null,
    placeholder: Painter? = null,
    error: Painter? = null,
    fallback: Painter? = error,
) {
    AsyncImageView(
        modifier = modifier,
        model = model,
        contentDescription = contentDescription,
        placeholder = placeholder,
        error = error,
        fallback = fallback,
    )
}