package com.yuanjingtech.boot.app.kmp.ad

/**
 * 构建配置接口 — 用于运行时告知是否处于 debug 模式。
 *
 * 默认实现见 [DefaultAdBuildConfig](`isDebug = true`),
 * 生产项目应通过 Koin 覆盖为读取自身构建配置的 actual。
 *
 * Android:在 `androidApp` 中提供 `single<AdBuildConfig> { object : AdBuildConfig { override val isDebug = BuildConfig.DEBUG } }`
 * iOS:在 `iosApp` 中提供 `single<AdBuildConfig> { object : AdBuildConfig { override val isDebug = isDebugConfiguration } }`
 */
interface AdBuildConfig {
    val isDebug: Boolean
}

/**
 * 默认实现 — 假定为开发态(返回 `true`),允许使用 [AdUnitConfig.TEST]。
 * 在 release 构建中应通过 Koin 覆盖为返回 `false`。
 */
class DefaultAdBuildConfig : AdBuildConfig {
    override val isDebug: Boolean = true
}
