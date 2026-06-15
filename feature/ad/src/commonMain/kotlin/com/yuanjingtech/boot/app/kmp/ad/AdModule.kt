package com.yuanjingtech.boot.app.kmp.ad

import org.koin.dsl.module

/**
 * 广告模块 Koin 聚合入口
 *
 * 使用方式:
 * ```
 * startKoin {
 *     modules(adModule)
 * }
 * ```
 *
 * 在 release 构建中应覆盖 [AdBuildConfig]:
 * ```
 * modules(module {
 *     single<AdBuildConfig> { object : AdBuildConfig { override val isDebug = false } }
 * })
 * ```
 */
val adModule = module {
    single<AdBuildConfig> { DefaultAdBuildConfig() }
    single<AdUnitConfigProvider> { DefaultAdUnitConfigProvider(get()) }
    includes(adPlatformModule)
}

/**
 * 根据构建模式返回对应的 [AdUnitConfig]。
 *
 * - `isDebug == true`:返回 [AdUnitConfig.TEST](Google 官方测试 ID)
 * - `isDebug == false`:返回 release 时由业务模块注入的 [productionConfig]
 */
interface AdUnitConfigProvider {
    fun current(productionConfig: AdUnitConfig): AdUnitConfig
}

class DefaultAdUnitConfigProvider(
    private val buildConfig: AdBuildConfig,
) : AdUnitConfigProvider {
    override fun current(productionConfig: AdUnitConfig): AdUnitConfig {
        return if (buildConfig.isDebug) AdUnitConfig.TEST else productionConfig
    }
}
