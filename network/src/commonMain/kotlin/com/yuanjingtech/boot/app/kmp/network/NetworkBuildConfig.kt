package com.yuanjingtech.boot.app.kmp.network

/**
 * 网络层的构建配置接口 —— 用于运行时告知是否处于 debug 模式。
 *
 * 用途：SSRF 防护（见 [NetworkRequestBuilder]）在 debug 模式下**允许**访问
 * `localhost` / `127.0.0.1` / 私网段，方便开发时连本机后端；
 * release 模式下行为完全不变，仍拦截这些 host。
 *
 * 覆盖方式：
 * ```
 * startKoin {
 *     modules(
 *         bootModule,
 *         module {
 *             single<NetworkBuildConfig> { object : NetworkBuildConfig {
 *                 override val isDebug = BuildConfig.DEBUG
 *             } }
 *         },
 *     )
 * }
 * ```
 *
 * - Android：在 `androidApp` 中用 `BuildConfig.DEBUG` 覆盖；
 * - iOS：由宿主传入自己的 debug 标志；
 * - Desktop / Web：用 Gradle 属性生成（见各业务仓的构建期配置做法）。
 *
 * **注意**：Koin 4 中同一类型重复定义需要允许覆盖（`allowOverride`），
 * 或在启动 Koin 时用 `single(override = true)` 重新声明。
 */
interface NetworkBuildConfig {
    /**
     * 是否为 debug 构建。
     *
     * `true` ⇒ 允许访问 `localhost` / `127.0.0.1` / 私网段；
     * `false` ⇒ 全量拦截（SSRF 防护全开）。
     */
    val isDebug: Boolean
}

/**
 * 默认实现 —— **假定为 release 态（`isDebug = false`）**。
 *
 * 这里刻意不跟随 [com.yuanjingtech.boot.app.kmp.ad.AdBuildConfig] 的开发态默认值：
 * 广告位默认值错了只是投错广告位，而 SSRF 防护默认值错了会让**生产构建**
 * 允许访问内网地址。安全类开关必须**默认拒绝**，由业务仓在 debug 构建里
 * 显式覆盖为 `true`。
 */
class DefaultNetworkBuildConfig : NetworkBuildConfig {
    override val isDebug: Boolean = false
}
