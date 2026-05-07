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

**Blocker:** Linear API 网络不通，无法完成状态转换和评论更新。

---

### 2026-05-08 (run 1) — implement 阶段

**任务:** 将调研结果落实为文档更新 + PR

**完成内容:**
1. 更新 `README.md` — 新增两大章节:
   - **Boot Starter 架构**: Spring Boot Starter 与 boot-app-kmp 的等价关系
   - **企业常用 Starter 模块映射**: 已实现(5个) + 待实现(9个, P0-P3 优先级)
   - 修正过时条目: ktor network、sqldelight wasm、coil、webview 均已实现

2. 重构 `TODO.md` — 按 P0/P1/P2/P3 优先级列出模块路线图

**关键决策:**
- 自动配置机制已实现: pluginModule + SweetSPI，等价 Spring Boot AutoConfiguration
- 聚焦 80/20 法则: P0(auth + actuator) 覆盖 80% 企业场景
- 不追求 50+ 全覆盖

**Git:**
- Branch: `feat/enterprise-starters-research`
- PR: https://github.com/yuanjingtech/boot-app-kmp/pull/5

**验证:**
- ✅ `./gradlew :shared:compileKotlinJvm` — BUILD SUCCESSFUL
- ✅ `./gradlew test` — BUILD SUCCESSFUL (229 tasks)

**状态:** PR 已创建，Linear API 评论失败（网络问题），手动记录到 WORKPAD.md

**后续子任务拆分 (建议创建独立 Issue):**
- YUA-15: auth 模块开发 (P0)
- YUA-16: actuator 模块开发 (P0)
- YUA-17: cache 模块开发 (P1)
- YUA-18: store 模块开发 (P1)
- YUA-19: tenant 模块开发 (P2)
- YUA-20: mail 模块开发 (P2)