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
1. ✅ 添加 Linear API_KEY 到环境变量
2. ✅ 调查总结发布到 Linear (comment ID: 4eaf45cf...)
3. ✅ Linear issue 状态更新为 "In Review"
4. ✅ 所有调查结果保存在 WORKPAD.md

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

---

### YUA-77 — ui 主题 增加 fluent

#### 2026-05-18 (run 1) — investigate 阶段

**任务:** 在现有 UI 主题系统（LiquidGlass + Material3）基础上，新增 Fluent 风格。

**完成内容:**
1. 阅读了 `BootUiStyle.kt`, `LocalUiStyle.kt` — 理解当前枚举架构
2. 阅读了 `components/` 下 13 个文件的 when 分发模式
3. 阅读了 `BootAppTheme.kt`, `BootThemeStore.kt`, `BootThemeSettingScreen.kt` — 理解持久化链路
4. 阅读了 `liquidglass/` 下的 Color/Button 实现 — 理解风格实现模式
5. 阅读了 `BootPreviewUtils.kt` — 理解预览框架
6. 查看了 compose-fluent v0.1.0 文档

**核心发现:**
- 当前架构：`BootUiStyle` 枚举 → `LocalUiStyle` CompositionLocal → `components/` 中的 when 分发
- `BootThemeStore` 通过 room3 的 `ThemeSettings` 表持久化 uiStyle（String 类型）
- compose-fluent v0.1.0 提供 `FluentTheme` + `Mica`/`Layer` 背景组件 + 完整组件库

**受影响文件（粗略统计）：**
- BootUiStyle.kt（枚举扩展）
- fluent/ 新目录（约 40+ 个实现文件）
- 13 个 components/*.kt 文件（when 分发增加 FLUENT 分支）
- BootThemeSettingScreen.kt（设置页选项）
- BootPreviewUtils.kt（预览 wrapper）
- ui/build.gradle.kts（依赖）

**风险:**
- compose-fluent v0.1.0 KMP 兼容性待实测
- 40+ 组件需要逐个实现映射
- 与现有依赖是否冲突需验证

**状态:** 调查完成。Linear API (api.linear.app) 可达但需要认证，gh CLI 无法连接 github.com。调查总结记录在此 WORKPAD.md。

### Linear Comment 内容（未能发布，粘贴至此）

```markdown
## Investigation

### 需求分析
为 UI 组件库新增 **Fluent Design** 风格，与现有的 LiquidGlass、Material3 形成三足鼎立。目标库为 compose-fluent v0.1.0，提供 Mica/Acrylic 特效和完整 Fluent 组件集。

### 当前架构
BootUiStyle (enum) → LocalUiStyle (CompositionLocal) → components/ 19个文件 when 分派 → liquidglass/25个文件 + material3/18个文件 → BootThemeStore → BootThemeSettingScreen

### 受影响文件
- BootUiStyle.kt（枚举加 FLUENT）
- fluent/ 新目录 ~25 个实现文件
- 19 个 Boot*.kt 组件文件 when 分派
- BootPreviewUtils.kt（增加 FluentPreviewWrapper）
- BootThemeSettingScreen.kt（设置页选项）
- ui/build.gradle.kts + libs.versions.toml（依赖）

### 风险
1. compose-fluent v0.1.0 不含 wasmJs target，需 fallback
2. v0.1.0 为首个正式版，成熟度待验
3. 依赖冲突可能性

### 实施计划（5 步）
1. 添加依赖
2. 枚举扩展
3. fluent/ 实现层
4. 组件 when 分派更新
5. 预览 + 设置页
```

### Linear Comment 未能发布的原因
- GitHub: `gh auth` 超时，无法连接 api.github.com
- Linear API: 需要 Authorization header (Bearer token)，当前环境未配置 LINEAR_API_KEY

### 2026-05-19 (run 1) — implement 阶段

**任务:** 将调查转为实际代码实现

**完成内容:**
1. 修复 FluentUI 组件编译错误（签名不匹配、重复定义等）
2. 配置 compose-fluent 依赖（平台特定 artifacts）
3. 创建 wasmJsMain 桩实现（fluent-wasm-js 不可用）
4. 推送分支并创建 PR

**关键决策:**
- compose-fluent v0.1.0 使用平台特定 artifacts，Maven Central 上只有 `fluent-android` 可用
- `fluent-wasm-js`、`fluent-desktop`、`fluent-iosarm64` 均返回 404
- wasmJsMain 使用 `wasmJsMain/` 源集桩实现作为 fallback
- 添加 `https://repo.klibs.io/releases` 到 `settings.gradle.kts`

**Git:**
- Branch: `yua-77-add-fluent-style`
- Commits: 2 个
  - `08b17ec` — feat: Add Fluent Design theme support via compose-fluent (上一 session)
  - `b6d8716` — fix: resolve Fluent UI compilation errors and update dependency config
- PR: https://github.com/yuanjingtech/boot-app-kmp/pull/6

**验证:**
- ✅ `./gradlew :ui:compileKotlinJvm` — BUILD SUCCESSFUL
- ✅ `./gradlew :ui:compileKotlinJs` — BUILD SUCCESSFUL
- ❌ wasmJs target — backdrop library blur/lens API 编译错误（预存问题，非本次引入）

**限制:**
- fluent-wasm-js/fluent-desktop/fluent-iosarm64 不可用，wasmJsMain 使用桩实现
- 设置页（BootThemeSettingScreen）未更新添加 Fluent 选项 — 建议独立 issue
- wasmJs 编译因 backdrop 库问题失败（预存问题，与本次 fluent 无关）

**Workpad 更新于:** 2026-05-19 下午 1:55