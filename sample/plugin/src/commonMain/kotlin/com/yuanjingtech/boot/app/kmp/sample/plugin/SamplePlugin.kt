package com.yuanjingtech.boot.app.kmp.sample.plugin

@ServiceProvider
object SamplePlugin : Plugin {
    override val module get() = samplePluginModule
}