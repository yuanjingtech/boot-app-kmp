# 插件

插件(Plugin)是特殊的模块(Module), 是可以被动态加载的模块, 其主要作用是为其他模块提供服务(Service), 以实现模块之间的解耦和扩展性. 插件通常包含一个或多个服务接口的实现类, 以及一个用于注册这些服务的模块类. 插件可以被其他模块通过服务加载器(ServiceLoader)来加载和使用, 从而实现功能的扩展和增强. 插件的使用可以大大提高系统的灵活性和可维护性, 使得系统能够更好地适应不断变化的需求和环境.

## sweet-spi

https://klibs.io/project/whyoleg/sweet-spi

##  

```kotlin
internal val demoModule = module {
    includes(bootModule)
    single { DemoSubApp() } bind SubApp::class
}

@ServiceProvider
object DemoModule : BootModule {
    @Suppress("unused")
    override val module get() = demoModule
}
```

## 参考

模块化的粒度:
服务, 一组方法的集合,
模块, 一组相关的服务的集合, 
组件, 一组相关的模块的集合, 可以独立部署和运行, 以实现功能的封装和复用.
插件、组件包、bundle, 一组相关的组件的集合, 通常是一个库, 也可以是一个应用, 甚至是一个系统, 可以动态加载和卸载, 以实现功能的扩展和增强.