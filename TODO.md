# Boot Module 路线图

基于 Spring Boot Starter 体系，boot-app-kmp 模块优先级路线图。

## P0 — 企业核心 (Phase 1)

### auth 模块 — 等价 spring-boot-starter-security

认证/授权模块，支持 JWT / Session 方案。

依赖: Ktor Client (network 已具备)
参考: Spring Security

- [ ] `auth/` 模块骨架
- [ ] JWT Token 生成与校验
- [ ] Session 管理
- [ ] Auth Interceptor (Ktor)

### actuator 模块 — 等价 spring-boot-starter-actuator

应用监控模块，提供健康检查、性能指标。

- [ ] `actuator/` 模块骨架
- [ ] Health Check 接口
- [ ] Metrics 指标暴露
- [ ] Ktor 端点集成

## P1 — 企业扩展 (Phase 2)

### cache 模块 — 等价 spring-boot-starter-data-redis + spring-boot-starter-cache

KMP 缓存抽象，优先使用 DataStore (跨平台)。

- [ ] `cache/` 模块骨架
- [ ] DataStore 实现
- [ ] Memory Cache 实现
- [ ] Cache Abstraction 接口

### store 模块 — 等价 spring-boot-starter-jdbc

数据源抽象层，统一数据库访问接口。

- [ ] `store/` 模块骨架
- [ ] Repository 模式
- [ ] SQLDelight 集成
- [ ] 事务管理

## P2 — 业务增强 (Phase 3)

### tenant 模块 — 等价多租户 SaaS

多租户隔离支持 (Shared DB / Separate Schema)。

- [ ] `tenant/` 模块骨架
- [ ] Tenant Context 传递
- [ ] 数据隔离策略
- [ ] Tenant Resolver

### mail 模块 — 等价 spring-boot-starter-mail

邮件发送支持。

- [ ] `mail/` 模块骨架
- [ ] SMTP 实现 (JVM)
- [ ] SendGrid 实现 (跨平台)

### websocket 模块 — 等价 spring-boot-starter-websocket

WebSocket 通信。

- [ ] `websocket/` 模块骨架
- [ ] Ktor WebSocket 实现

## P3 — 生态完善

### batch 模块 — 等价 spring-boot-starter-batch

批处理任务框架。

- [ ] `batch/` 模块骨架

### image 模块 — coil 独立模块化

图像加载独立封装。

- [ ] `image/` 模块骨架
- [ ] Coil KMP 封装

---

## UI 相关

- [ ] LiquidGlass 组件完善 — follow https://kyant.gitbook.io/backdrop
- [ ] composeApp module 使用 ui components

## Media 模块 (待调研)

- boot module : media
- 参考: https://juejin.cn/post/7631761079555145766
