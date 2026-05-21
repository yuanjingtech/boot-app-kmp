package com.yuanjingtech.boot.app.kmp

import androidx.compose.runtime.Composable
import com.yuanjingtech.boot.app.kmp.composeapp.generated.resources.Strings

/**
 * Boot UI string resources with i18n support.
 * Use these composable functions to get localized strings.
 */
object BootStrings {
    @Composable
    fun appName(): String = Strings.appName()

    @Composable
    fun tabItemMain(): String = Strings.tabItemMain()

    @Composable
    fun tabItemSetting(): String = Strings.tabItemSetting()
}
