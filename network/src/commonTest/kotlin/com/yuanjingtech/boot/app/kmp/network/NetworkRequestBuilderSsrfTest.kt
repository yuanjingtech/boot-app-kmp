package com.yuanjingtech.boot.app.kmp.network

import org.junit.Assert.*
import org.junit.Test

/**
 * SSRF 校验的 debug / release 行为锁定。
 *
 * - **release**（`isDebug = false`，默认值）⇒ 私网 / localhost **必须被拦截**；
 * - **debug**（`isDebug = true`）⇒ 允许访问本机后端，但**协议校验仍然生效**。
 *
 * 默认值是 `false`，所以既有调用方（直接 `NetworkRequestBuilder()`）的行为
 * 与改动前完全一致。
 */
class NetworkRequestBuilderSsrfTest {

    // ---------- release 行为（默认值，必须全量拦截） ----------

    @Test(expected = IllegalArgumentException::class)
    fun release_rejectsLocalhost() {
        NetworkRequestBuilder().url("http://localhost:8080/api")
    }

    @Test(expected = IllegalArgumentException::class)
    fun release_rejectsLoopbackIp() {
        NetworkRequestBuilder().url("http://127.0.0.1:8080/api")
    }

    @Test(expected = IllegalArgumentException::class)
    fun release_rejectsPrivateIp_10() {
        NetworkRequestBuilder().url("http://10.0.0.1/api")
    }

    @Test(expected = IllegalArgumentException::class)
    fun release_rejectsPrivateIp_172() {
        NetworkRequestBuilder().url("http://172.16.0.1/api")
    }

    @Test(expected = IllegalArgumentException::class)
    fun release_rejectsPrivateIp_192() {
        NetworkRequestBuilder().url("http://192.168.1.1/api")
    }

    @Test
    fun release_allowsPublicHttps() {
        val builder = NetworkRequestBuilder()
        builder.url("https://yuanjing.binbinsoft.com/api")
        assertTrue(builder.build().url.toString().startsWith("https://"))
    }

    // ---------- debug 行为（放行私网 / localhost） ----------

    @Test
    fun debug_allowsLocalhost() {
        val builder = NetworkRequestBuilder(isDebug = true)
        builder.url("http://localhost:8080/api")
        assertTrue(builder.build().url.toString().contains("localhost"))
    }

    @Test
    fun debug_allowsLoopbackIp() {
        val builder = NetworkRequestBuilder(isDebug = true)
        builder.url("http://127.0.0.1:8080/api")
        assertTrue(builder.build().url.toString().contains("127.0.0.1"))
    }

    @Test
    fun debug_allowsPrivateIp() {
        val builder = NetworkRequestBuilder(isDebug = true)
        builder.url("http://192.168.1.10:8080/api")
        assertTrue(builder.build().url.toString().contains("192.168.1.10"))
    }

    @Test
    fun debug_stillRejectsNonHttpScheme() {
        // 放行的只是内网地址，协议限制不能被一起放开
        val thrown = try {
            NetworkRequestBuilder(isDebug = true).url("ftp://localhost/file")
            null
        } catch (e: IllegalArgumentException) {
            e
        }
        assertNotNull("debug 模式仍应拒绝非 http/https 协议", thrown)
        assertTrue(thrown!!.message!!.contains("HTTP/HTTPS"))
    }

    // ---------- 配置对象 ----------

    @Test
    fun defaultBuildConfig_isDebugFalse() {
        // 安全类开关必须默认拒绝：忘覆盖的构建应当是 release 行为，而不是放行内网
        assertFalse(DefaultNetworkBuildConfig().isDebug)
    }

    @Test
    fun builderDefault_isReleaseLike() {
        // 默认构造必须等价于 release —— 这是"既有调用方行为不变"的保证
        val thrown = try {
            NetworkRequestBuilder().url("http://localhost:8080/api")
            null
        } catch (e: IllegalArgumentException) {
            e
        }
        assertNotNull(thrown)
        assertTrue(thrown!!.message!!.contains("blocked for security"))
    }
}
