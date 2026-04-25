package com.yuanjingtech.boot.app.kmp.ui.components

import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle

/**
 * Shared preview parameter provider for [BootUiStyle] values.
 * Use with @PreviewParameter to iterate over all UI styles.
 */
class BootUiStyleProvider : androidx.compose.ui.tooling.preview.PreviewParameterProvider<BootUiStyle> {
    override val values: Sequence<BootUiStyle> = sequenceOf(
        BootUiStyle.LIQUID_GLASS,
        BootUiStyle.MATERIAL3
    )
}
