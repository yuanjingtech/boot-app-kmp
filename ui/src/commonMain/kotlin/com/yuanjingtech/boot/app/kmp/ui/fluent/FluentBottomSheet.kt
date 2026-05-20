package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentLightPreviewWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FluentModalBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = LocalFluentColors.current
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.surface)
                    .padding(16.dp),
            ) {
                Column {
                    content()
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        containerColor = colors.background,
        contentColor = colors.textPrimary,
        modifier = modifier,
    ) {
        Box(modifier = Modifier.background(colors.background)) {
            Text("")
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentModalBottomSheetPreview() {
    FluentModalBottomSheet(
        onDismiss = { },
    ) {
        Text("Bottom Sheet Content")
    }
}