## Workpad

### YUA-14 — 企业常用boot starter 模块调研

#### 2026-05-07 (run 1) — investigate 阶段

**任务:** 调研企业级应用最常用的 Spring Boot Starter 模块，映射到 boot-app-kmp 的 KMP 模块化设计。

**完成内容:**
1. 完整阅读了 boot-app-kmp 项目结构（12个模块）
2. 阅读了所有模块的 build.gradle.kts 和源码
3. 搜索了企业常用 Spring Boot Starter 相关资料
4. 完成了调研分析并发布到 Linear 评论区

**核心发现:**
- boot-app-kmp 已有 8 个模块，覆盖了最常用的 web 和 data 层
- 缺失最严重的是: auth(安全)、tenant(多租户)、actuator(监控)、cache(缓存)
- Spring Boot 有 ~50+ 官方 Starter，KMP 应聚焦 80/20 法则

**已发布到 Linear 的调查总结要点:**
| 排序 | Starter | boot-app-kmp 状态 |
|------|---------|---------------------|
| 1 | web | ✅ network/ 已实现 |
| 2 | data-jpa | ✅ sqldelight/ 已实现 |
| 3 | security | ❌ auth 模块待实现 |
| 4 | actuator | ❌ 监控待实现 |
| 5 | data-redis | ❌ 缓存待实现 |
| 6 | validation | ❌ 校验待实现 |

**建议方案:**
- Phase 1: auth + tenant
- Phase 2: store + cache + validation
- Phase 3: coil 独立模块 + actuator

**状态:** 调查完成，已发布到 Linear comment。尝试切换状态到 research-review 时网络超时(api.linear.app DNS 解析失败) — 这是环境网络问题，非逻辑错误。

**Blocker:** Linear API 网络不通，无法完成状态转换。