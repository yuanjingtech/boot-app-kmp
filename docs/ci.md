# CI / 产物发布

## 工作流一览

| 文件 | 触发 | 作用 |
| --- | --- | --- |
| `.github/workflows/ci-build-test.yml` | PR 到 `main` | Android debug APK、Web(wasm) 构建、`:app:composeApp:jvmTest`、iOS framework 编译检查 |
| `.github/workflows/release-artifacts.yml` | tag `v*` / release published / 手动 | 构建并发布 Android / iOS / Desktop / Web 四端产物 |
| `.github/workflows/alpha.yml` | push `main` | Maven Central alpha 发布 |
| `.github/workflows/publish.yml` | release | Maven Central 正式发布 |

## release-artifacts.yml

### 触发方式

- `push` tag `v*`：构建四端产物，并创建/更新同名 GitHub Release 后附加产物
- `release: published`：构建四端产物并附加到该 Release
- `workflow_dispatch`：手动构建，可填 `version`（用于产物命名，留空则取 tag 或 `1.0.<run_number>`），产物以 Actions Artifact 形式保留 30 天

### 产物清单

| Job | Runner | 产物 | 说明 |
| --- | --- | --- | --- |
| `android` | ubuntu-latest | `<name>-<version>-debug.apk`、`<name>-<version>-release-unsigned.apk` | release APK 未签名 |
| `ios` | macos-latest | `<name>-<version>-unsigned.ipa` | 无签名，不可直接装到非越狱设备 |
| `desktop` | ubuntu / macos / windows | `.deb` / `.dmg` / `.msi` + `.exe` | 使用 [KMP Build Desktop App](https://github.com/marketplace/actions/kmp-build-desktop-app) action |
| `web` | ubuntu-latest | `<name>-<version>-web-wasm.zip`、`<name>-<version>-web-js.zip` | 解压后目录可直接静态托管 |

`desktop` job 通过 marketplace action `openMF/mifos-x-actionhub-build-desktop-app-kmp@v1.0.1`
在三个 OS 的 matrix 中执行 `./gradlew packageDistributionForCurrentOS`
（Gradle 会在拥有该任务的 `:app:desktopApp` 中执行），并分别上传
`desktop-app-{ubuntu,macos,windows}-latest` artifact。
Linux 上额外安装 `fakeroot`，否则 jpackage 无法生成 `.deb`。

### 前置条件与已知限制

1. **iOS 目前无法归档**（会先失败）：`app/iosApp` 的 Xcode target 依赖本地 Swift Package
   `KotlinMultiplatformLinkedPackage` 提供 `import ComposeApp` 与 `import GoogleMobileAds`，
   该包由 `:feature:ad` 的 `swiftPMDependencies` / `integrateLinkagePackage` 生成；
   而 `settings.gradle.kts` 当前注释掉了 `include(":feature:ad")`，`project.pbxproj` 中也没有
   `FRAMEWORK_SEARCH_PATHS` / `OTHER_LDFLAGS`。恢复 swiftPM 集成或补齐 framework 搜索路径后，
   `ios` job 即可通过。
2. **Desktop 使用 `build_type: 'Debug'`**：`packageReleaseDistributionForCurrentOS` 会先跑
   Compose Desktop 的 `proguardReleaseJars`，实测因 710 处 unresolved references、
   60 处 unresolved program class members 失败（webview/CEF、slf4j 等可选依赖）。
   需要 Release 包时，先在 `app/desktopApp/build.gradle.kts` 的
   `buildTypes.release.proguard` 中补 `-dontwarn`/`-ignorewarnings` 或关闭 ProGuard，
   再把 action 的 `build_type` 改回 `'Release'`。
   Debug 包本身是完整可安装的（只是未做 ProGuard 压缩）。
3. **Android release 未签名**：`app/androidApp/build.gradle.kts` 未配置 `signingConfig`，
   产出 `*-release-unsigned.apk`。需要正式签名时补充 keystore secrets 并配置签名。
4. **iOS 正式分发**需要 Apple 证书 + provisioning profile，当前仅产出无签名 `.ipa`。
5. **Desktop 产物体积较大**（单平台 200–500 MB），附加到 Release 前请留意仓库配额。
6. Desktop 包的版本号来自 `app/desktopApp/build.gradle.kts` 中硬编码的
   `packageVersion = "1.0.0"`，不随 `APP_VERSION` 变化；需要对齐时请同步修改该值。
7. 任一平台失败时，`publish-release` 仍会发布其余平台的产物（整体流水线保持红色）。

### 本地等价命令

```bash
./gradlew :app:androidApp:assembleDebug :app:androidApp:assembleRelease
./gradlew :app:desktopApp:packageDistributionForCurrentOS         # 仅当前 OS；app/desktopApp/build/compose/binaries/main/
./gradlew :app:webApp:wasmJsBrowserDistribution                    # app/webApp/build/dist/wasmJs/productionExecutable
./gradlew :app:composeApp:jvmTest                                  # 共享测试
./gradlew :app:composeApp:linkDebugFrameworkIosArm64               # 仅 macOS

# iOS 归档（无签名）
cd app/iosApp
xcodebuild archive -project iosApp.xcodeproj -scheme iosApp \
  -configuration Release -destination 'generic/platform=iOS' \
  -archivePath /tmp/iosApp.xcarchive \
  CODE_SIGN_IDENTITY="" CODE_SIGNING_REQUIRED=NO CODE_SIGNING_ALLOWED=NO
```

`xcodebuild -scheme iosApp` 依赖共享 scheme
`app/iosApp/iosApp.xcodeproj/xcshareddata/xcschemes/iosApp.xcscheme`（随本次改动新增，
此前只有被 gitignore 的 `xcuserdata` 个人 scheme，CI 上无法解析 `-scheme`）。
