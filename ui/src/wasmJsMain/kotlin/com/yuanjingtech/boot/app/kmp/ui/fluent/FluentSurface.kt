package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FluentSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp,
    content: @Composable () -> Unit,
) {
    val colors = LocalFluentColors.current
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .background(colors.surface, shape),
        contentAlignment = Alignment.TopStart,
    ) {
        content()
    }
}
