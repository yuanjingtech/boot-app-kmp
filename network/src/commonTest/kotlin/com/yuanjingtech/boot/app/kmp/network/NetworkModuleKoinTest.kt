package com.yuanjingtech.boot.app.kmp.network

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * networkModule 的 Koin 装配自检。
 *
 * [NetworkServiceKtorImpl] 的构造参数多了 [NetworkBuildConfig] ——
 * 如果 `networkModule` 里漏了这个绑定，运行期才会炸（编译期不报错）。
 * 这里真的把图实例化一遍来兜底。
 */
class NetworkModuleKoinTest {

    @Test
    fun networkModuleResolvesService() {
        stopKoin()
        try {
            val koin = startKoin {
                modules(networkModule)
            }.koin

            // 能拿到就说明 NetworkBuildConfig 的绑定到位了
            val service = koin.get<NetworkService>()
            assertTrue(service is NetworkServiceKtorImpl)

            val config = koin.get<NetworkBuildConfig>()
            assertFalse("默认实现应为 release 态（SSRF 全量拦截）", config.isDebug)
        } finally {
            stopKoin()
        }
    }

    @Test
    fun debugOverrideTakesEffect() {
        // 业务仓（KMP）在 debug 构建里覆盖成 true —— 必须真的生效，
        // 否则 T1.3 的放行开关形同虚设。
        stopKoin()
        try {
            val koin = startKoin {
                modules(
                    networkModule,
                    module {
                        single<NetworkBuildConfig> { object : NetworkBuildConfig {
                            override val isDebug = true
                        } }
                    },
                )
            }.koin

            assertTrue("覆盖后应解析到 debug 配置", koin.get<NetworkBuildConfig>().isDebug)
        } finally {
            stopKoin()
        }
    }
}
