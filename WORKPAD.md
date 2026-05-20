## Workpad

### YUA-75 — 增加BootApplicationPreview(content) 用来简化预览

#### 2026-05-15 (run 1) — investigate 阶段

**任务:** 参考 Koin 官方文档 `KoinApplicationPreview` API，新增 `BootApplicationPreview(content)` 简化 Compose 预览，并编写使用文档。

**完成内容:**
1. 阅读了 `BootApplication.kt`、`BootAppTheme.kt`、`BootModule.kt` 等核心文件
2. 阅读了 `skills/boot-app-kmp/SKILL.md` 现有文档
3. 研究了 `docs/skills.md` 中的 compose-ui-preview 配置说明
4. 参考了 `webview-parkwoocheol/WebViewScreen.kt` 中已使用 `KoinApplicationPreview` 的实际案例
5. 了解了 gradle 依赖配置 (koin 4.2.1, compose 1.11.0-beta03)

**核心发现:**
- `BootApplication` 已使用 `KoinApplication`，包含 `bootModule` + `pluginModule`
- 现有预览需要手动 `modules(...)` 配置，与主入口不一致
- Koin 4.x 的 `KoinApplicationPreview` 已在 `koin-compose` 中提供
- 已有 `webview-parkwoocheol` 模块成功使用 `KoinApplicationPreview`

**受影响文件:**
| 文件 | 说明 |
|------|------|
| `shared/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/BootApplication.kt` | 新增 `BootApplicationPreview(content)` 函数 |
| `skills/boot-app-kmp/SKILL.md` | 新增预览章节文档 |
| `docs/skills.md` | 新增 compose-ui-preview 章节 |

**Proposed approach:**
1. 新增 `BootApplicationPreview(content)` — 封装 `KoinApplicationPreview`，自动注入 `bootModule` + `pluginModule`
2. 文档聚焦：gradle 依赖配置 + 使用示例（与 `KoinApplicationPreview` 对比）
3. 保持 API 简洁，无额外参数，与 `BootApplication` 行为一致

**Blocker:** Linear API 网络不通，无法完成状态转换和评论更新。

---

### YUA-75 — implement 阶段 (run 1)

**状态:** ✅ 完成

**完成内容:**
1. 新增 `BootApplicationPreview(content)` 函数于 `shared/.../BootApplication.kt`
   - 封装 `KoinApplicationPreview`，自动注入 `bootModule` + `pluginModule` + `BootAppTheme`
   - 预览使用只需一行 `@Composable` lambda，无需手动配置 modules
2. 更新 `docs/skills.md` — 新增 `compose-ui-preview` 章节说明
3. 更新 `skills/boot-app-kmp/SKILL.md` — 新增 3.6 Koin Preview 依赖注入使用文档（含对比示例）

**质量验证:**
- ✅ `./gradlew :shared:compileKotlinJvm` — BUILD SUCCESSFUL
- ✅ `./gradlew test` — BUILD SUCCESSFUL (225 tasks)

**Git 状态:**
- Branch: `yua-75-boot-application-preview`
- Commit: `2cecbad feat(shared): 新增 BootApplicationPreview(content) 简化 Compose 预览`
- Push: ✅ `git push -u origin HEAD` 成功

**PR 创建:** ✅ PR #7 已存在并 OPEN
- URL: https://github.com/yuanjingtech/boot-app-kmp/pull/7
- 网络问题已恢复，PR 已正常创建

---

### YUA-75 — implement 阶段 (rework run 1)

**日期:** 2026-05-19

**状态:** ✅ 完成

**复验内容:**
- ✅ PR 已存在并 OPEN: https://github.com/yuanjingtech/boot-app-kmp/pull/7
- ✅ 分支已推送到 origin
- ✅ `./gradlew :shared:jvmTest` — BUILD SUCCESSFUL
- ✅ `./gradlew :ui:jvmTest` — BUILD SUCCESSFUL

**受影响文件（无变更）:**
- `shared/.../BootApplication.kt` — 已存在
- `docs/skills.md` — 已存在
- `skills/boot-app-kmp/SKILL.md` — 已存在

---

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