package com.yuanjingtech.boot.app.kmp.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.yuanjingtech.boot.app.kmp.shared.generated.resources.LXGWWenKaiMono_Light
import com.yuanjingtech.boot.app.kmp.shared.generated.resources.LXGWWenKaiMono_Medium
import com.yuanjingtech.boot.app.kmp.shared.generated.resources.LXGWWenKaiMono_Regular
import com.yuanjingtech.boot.app.kmp.shared.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun LXGWWenKaiTypography(typography: Typography): Typography {
    val fontFamily = FontFamily(
        Font(Res.font.LXGWWenKaiMono_Regular, weight = FontWeight.Companion.Normal),
        Font(Res.font.LXGWWenKaiMono_Medium, weight = FontWeight.Companion.Medium),
        Font(Res.font.LXGWWenKaiMono_Medium, weight = FontWeight.Companion.Bold),
        Font(Res.font.LXGWWenKaiMono_Light, weight = FontWeight.Companion.Light),
    )

    return with(typography) {
        copy(
            displayLarge = displayLarge.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Bold),
            displayMedium = displayMedium.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Bold),
            displaySmall = displaySmall.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Bold),
            headlineLarge = headlineLarge.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Bold),
            headlineMedium = headlineMedium.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Bold),
            headlineSmall = headlineSmall.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Bold),
            titleLarge = titleLarge.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Bold),
            titleMedium = titleMedium.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Bold),
            titleSmall = titleSmall.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Bold),
            labelLarge = labelLarge.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Normal),
            labelMedium = labelMedium.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Normal),
            labelSmall = labelSmall.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Normal),
            bodyLarge = bodyLarge.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Normal),
            bodyMedium = bodyMedium.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Normal),
            bodySmall = bodySmall.copy(fontFamily = fontFamily, fontWeight = FontWeight.Companion.Normal),
        )
    }
}