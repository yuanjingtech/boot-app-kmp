package com.yuanjingtech.boot.app.kmp.webview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * **前进 / 后退工具栏。**
 *
 * ## 它对应 RN 端的哪一段
 *
 * RN `SubAppWebView.tsx:31-34`：
 * ```tsx
 * <View style={styles.toolbar}>
 *     <Button title={"back"} onPress={() => webViewElement.current.goBack()}/>
 *     <Button title={"forward"} onPress={() => webViewElement.current.goForward()}/>
 * </View>
 * ```
 * 样式是 `flexDirection: "row"` + `justifyContent: "flex-start"`
 * ⇒ ⇒ **一行、靠左** ⇒ 这里用 [Row] + 默认左对齐。
 *
 * ## ⚠️ 文案为什么是中文「后退」「前进」
 *
 * RN 那两个 `title={"back"} / {"forward"}` 是**没翻译的占位符**，
 * 而且 `react-native-material-ui` 的 `Button` 有 `defaultProps.upperCase = true`
 * ⇒ ⇒ RN 实际渲染出来的是 **"BACK" / "FORWARD"**。
 *
 * ⇒ 按「补全而非等价还原」的口径 ⇒ ⇒ 这里用**中文**。
 * ⚠️ 本模块**没有 composeResources** ⇒ ⇒ 文案只能硬编码。
 *
 * ## ⚠️ `enabled` 为什么不写死
 *
 * RN 那两个按钮是**假按钮** —— 它们的 enabled 状态跟真实的 `canGoBack` / `canGoForward` 无关
 * ⇒ ⇒ 用户在没有历史时点「后退」⇒ 点了没反应。
 *
 * ⇒ ⇒ 这里**不做假按钮**：`enabled` 严格跟 [WebViewState.canGoBack] / [WebViewState.canGoForward] 走。
 *
 * ⚠️ **真值来源只能是 state holder**：那是从真实 WebView 转发过来的**滞后副本**
 * （库的 client 在 `onPageStarted` / `onPageFinished` 才写回真实值）
 * ⇒ ⇒ 口径是「**最终一致**」而不是「逐帧一致」：
 * **enabled 不会比真值更早为 true**，但真值从 true 变 false 时允许滞后一拍。
 *
 * ## ⚠️ 为什么这个 composable 放在 `webview` 模块（而不是 KMP 侧）
 *
 * boot 的 `shared` 对外 `api(projects.webview)`
 * ⇒ ⇒ `app/composeApp` 和 `features/subapp` **两边都拿得到**
 * ⇒ ⇒ 不用往 KMP 的 `shared` 下沉，消费方直接用。
 */
@Composable
fun WebViewNavigationBar(
    state: WebViewState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
    ) {
        TextButton(
            onClick = { state.controller?.goBack() },
            enabled = state.canGoBack,
        ) {
            Text("后退")
        }

        TextButton(
            onClick = { state.controller?.goForward() },
            enabled = state.canGoForward,
        ) {
            Text("前进")
        }
    }
}
