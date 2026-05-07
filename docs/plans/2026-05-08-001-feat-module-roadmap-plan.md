---
title: "feat: boot-app-kmp 模块路线图 — 企业常用 Starter 映射"
type: feat
status: active
date: 2026-05-08
---

# feat: boot-app-kmp 模块路线图 — 企业常用 Starter 映射

## Overview

基于企业级 Spring Boot Starter 使用频率调研，制定 boot-app-kmp 的模块开发优先级路线图。
核心发现: boot-app-kmp 已通过 `pluginModule` + SweetSPI 实现了与 Spring Boot Starter 等价的自动配置机制。

---

## Context & Research

### Spring Boot Starter 企业使用频率 (Top 12)

| 排序 | Starter | 用途 |
|------|---------|------|
| 1 | spring-boot-starter-web | RESTful API |
| 2 | spring-boot-starter-data-jpa | ORM |
| 3 | spring-boot-starter-security | 安全认证 |
| 4 | spring-boot-starter-actuator | 应用监控 |
| 5 | spring-boot-starter-data-redis | KV 缓存 |
| 6 | spring-boot-starter-validation | 参数校验 |
| 7 | spring-boot-starter-aop | AOP |
| 8 | spring-boot-starter-mail | 邮件 |
| 9 | spring-boot-starter-cache | 缓存抽象 |
| 10 | spring-boot-starter-websocket | WebSocket |
| 11 | spring-boot-starter-batch | 批处理 |
| 12 | spring-boot-starter-oauth2-client | OAuth2 |

### boot-app-kmp 自动配置机制 (已实现)

等价关系:

| Spring Boot | boot-app-kmp |
|-------------|--------------|
| `spring-boot-starter-xxx` | `boot-xxx` 模块 |
| `@Configuration` + `AutoConfiguration` | `BootModule` + Koin |
| `spring.factories` / `META-INF/spring/` | `META-INF/services/` + **SweetSPI** |

实现: `plugin/` 模块 + `shared/BootModule.kt`

---

## 模块现状总结

### 已实现 ✅

| 模块 | 对应 Starter | 依赖 |
|------|-------------|------|
| `network/` | spring-boot-starter-web | Ktor + kotlinx.serialization |
| `sqldelight/` | spring-boot-starter-jdbc | SQLDelight |
| `logging/` | spring-boot-starter-logging | kotlin-logging |
| `plugin/` | 自动配置 | SweetSPI + Koin |
| `ui/` | UI 组件 | LiquidGlass + Material3 |
| `subapp/` | 子应用 | Koin |
| `webview/` | WebView | Compose Multiplatform |
| `shared/` | BootModule 整合 | Room3 + Koin |
| `runblocking/` | 协程工具 | kotlinx.coroutines |

### 待实现 ❌

**P0 核心 (覆盖 80% 场景):**
- `auth/` — 认证/授权 (JWT/Session)
- `actuator/` — 健康检查/监控

**P1 扩展:**
- `cache/` — 缓存抽象 (DataStore)
- `store/` — 数据源抽象层
- `validation/` — 参数校验

**P2 业务:**
- `tenant/` — 多租户
- `mail/` — 邮件发送
- `websocket/` — WebSocket

**P3 生态:**
- `batch/` — 批处理
- `image/` — Coil 独立模块

---

## Open Questions

### 已解决

- **Q: 自动配置机制是否已实现?** → ✅ `pluginModule` + SweetSPI 已实现，与 Spring Boot Starter 等价
- **Q: 哪些模块优先实现?** → P0: auth + actuator，覆盖 80% 企业场景
- **Q: Redis 跨平台方案?** → 优先 DataStore (全平台)，可选 Redis (JVM)

### 待决策

- **Q: Auth 方案选择** — JWT (无状态) vs Session (有状态)? 建议 JWT + Refresh Token 方案
- **Q: 多租户数据隔离策略** — Shared DB (行级隔离) vs Separate Schema? 建议 Phase 2 再决策
- **Q: Mail 模块平台限制** — Web/WASM 无 SMTP，建议 JVM only 或 SendGrid API

---

## 并行开发拆分

根据模块依赖关系，可分为以下并行开发组:

### Group A (无依赖，可并行)

- `auth/` — 独立模块，仅依赖 `network/`
- `actuator/` — 独立模块，仅依赖 `network/`

### Group B (依赖 Group A)

- `tenant/` — 需要 `auth/` 完成 (租户用户归属)
- `cache/` — 独立模块，但 `store/` 依赖它

### Group C (可提前独立开发)

- `validation/` — 仅依赖 `network/`，可与 Group A 并行
- `mail/` — JVM only 模块，独立开发

---

## 受影响文件

- `README.md` — 更新模块路线图表格 ✅
- `TODO.md` — 按优先级重组待办事项 ✅
- `docs/plans/` — 新增本计划文档
- `settings.gradle.kts` — 新模块需添加 include

---

## 下一步

1. **Issue 创建:** 为每个 P0 模块 (auth, actuator) 创建独立的 Linear Issue
2. **Phase 1 实现:** auth + actuator 模块开发
3. **Phase 2 调研:** cache + store + validation 技术选型
