#!/usr/bin/env bash
#
# 在**隔离 worktree** 里做变异测试 —— 不碰共享工作树。
#
# ## 为什么要有这个脚本
#
# 我们两个 Agent 共用同一个工作树。变异测试要**故意改坏生产代码**，
# 如果在共享工作树里做 ⇒ **对方正在验收时会测到半成品**：
#   - 2026-10-23 实踩：@远景react native开发 跑验收时我的变异还在
#     ⇒ 他的新测试红了 ⇒ 他差点误判成"我的测试有问题"。
#
# ⇒ **判据：变异测试必须在隔离 worktree 里做。**
#
# ## 用法
#
#   ./tools/mutation-test.sh <相对路径> <要替换的原文> <替换后的文本> [期望红的测试名片段]
#
# 例：
#   ./tools/mutation-test.sh \
#     features/activity/src/commonMain/kotlin/com/yuanjingtech/activity/data/ActivityModels.kt \
#     'val url: String,' \
#     'val url: String = "",'
#
# ⇒ 脚本会：建 worktree → 变异 → 跑指定模块的 desktopTest → **断言"恰好 N 个红"** → 清理
# ⇒ **期望红的数量默认为 1**（变异应当只影响一个测试）；不符合就报错退出。
#
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
# ⚠️ **每次 run 用一个全新的路径**（带 PID + 时间戳）
#    ⇒ 上一次 run 残留的 Gradle daemon 还可能握着旧目录的文件句柄
#    ⇒ 复用同一路径 ⇒ 新 run 在构建期报
#    `Unable to delete directory .../caches-jvm` ⇒ 构建失败 ⇒ 拿不到测试结果
#    ⇒ 实测踩过（2026-10-23）。**路径唯一 = 不会和上次的 daemon 抢句柄。**
WT_DIR="$(dirname "$REPO_ROOT")/.mutation-wt-$$-$(date +%s)"

# 用法：mutation-test.sh <相对路径> <原文> <替换后> [测试任务] [期望红数] [测试名片段]
FILE_PATH="${1:?用法: mutation-test.sh <相对路径> <原文> <替换后> [任务] [期望红数] [测试名片段]}"
OLD_TEXT="${2:?缺 原文}"
NEW_TEXT="${3:?缺 替换后}"
TEST_TASK="${4:-:webview-parkwoocheol:jvmTest}"
EXPECT_RED="${5:-1}"
TEST_FILTER="${6:-}"

echo "==> 仓库: $REPO_ROOT"
echo "==> 隔离 worktree: $WT_DIR"

# ── 1. 建隔离 worktree ────────────────────────────────────────────────
git -C "$REPO_ROOT" worktree add --detach "$WT_DIR" HEAD >/dev/null

cleanup() {
  echo "==> 清理 worktree"
  git -C "$REPO_ROOT" worktree remove --force "$WT_DIR" 2>/dev/null || rm -rf "$WT_DIR"
  # ⚠️ **必须 prune** —— `worktree remove` 失败时（目录被占用等）目录还在，
  #    而 `rm -rf` 删掉目录后 **git 的登记记录还在** ⇒ `git worktree list` 里留一条
  #    `prunable` ⇒ **越积越多**（实测：目录没了但登记还在 ⇒ prune 一下就干净了）。
  #    ⇒ 残留的 worktree 里**可能还带着一处变异** ⇒ 清理前要顺手查一下。
  git -C "$REPO_ROOT" worktree prune 2>/dev/null || true
}
trap cleanup EXIT
# ⚠️ **Ctrl-C 也要清** —— 只 trap EXIT 时，SIGINT 在某些路径下会跳过 EXIT trap
#    ⇒ 留下一个**带着变异的** worktree（@远景react native开发 2026-10-23 见过一个 `mt-7921`）。
trap 'cleanup; exit 130' INT
trap 'cleanup; exit 143' TERM

# ── 2. 补上 git-ignored 但构建必需的产物 ──────────────────────────────
# app/webApp 的 wasm worker 是**构建期产物、不进版本库**
# ⇒ 新 worktree 里缺它 ⇒ 构建报 "Dependency on local path should point on directory"
# ⚠️ boot 仓没有 app/webApp（wasm worker 那一步是 KMP 仓特有的）⇒ 这里不需要
# local.properties（构建期注入，不进版本库）
cp "$REPO_ROOT/local.properties" "$WT_DIR/local.properties" 2>/dev/null || true

# ── 3. 变异 ───────────────────────────────────────────────────────────
TARGET="$WT_DIR/$FILE_PATH"
[ -f "$TARGET" ] || { echo "✗ 文件不存在: $TARGET" >&2; exit 1; }

python3 - "$TARGET" "$OLD_TEXT" "$NEW_TEXT" <<'PY'
import sys
path, old, new = sys.argv[1], sys.argv[2], sys.argv[3]
s = open(path, encoding='utf-8').read()
n = s.count(old)
if n != 1:
    print(f"✗ 期望命中 1 处，实际 {n} 处 ⇒ 变异目标不唯一，放弃", file=sys.stderr)
    sys.exit(1)
open(path, 'w', encoding='utf-8').write(s.replace(old, new))
print(f"==> 已变异: {old!r} → {new!r}")
PY

# ── 4. 跑测试 ─────────────────────────────────────────────────────────
cd "$WT_DIR"
export JAVA_HOME="${JAVA_HOME:-/Users/liubinbin/.local/share/mise/installs/java/21.0.2}"

if [ -n "$TEST_FILTER" ]; then
  # ⚠️ **bash 3.2（macOS 默认）下 `"${A[@]}"` 在数组为空时 + `set -u` 会报
  #    `A[@]: unbound variable` ⇒ 所以**只在有内容时才建数组**。
  GRADLE_CMD=("$TEST_TASK" --tests "$TEST_FILTER")
else
  GRADLE_CMD=("$TEST_TASK")
fi

echo "==> 跑 ${TEST_TASK}（期望恰好 ${EXPECT_RED} 个红）"
set +e
# ⚠️ **先停掉 Kotlin daemon** ——
#    `--no-daemon` 只停 **Gradle** daemon，**Kotlin** daemon 还会活着
#    ⇒ 它会**跨 worktree / 跨版本串味**（拿旧的 KLIB/类缓存来编译新目录）
#    ⇒ 表现为**莫名其妙的编译失败**（`:webview-parkwoocheol:compileKotlinJvm FAILED`，报的还是 KLIB/ABI）
#    ⇒ **实测：同一个 worktree 立刻重跑就过了** ⇒ 这不是代码问题，是 daemon 污染。
./gradlew --stop >/dev/null 2>&1 || true
OUT=$(./gradlew "${GRADLE_CMD[@]}" \
        --no-daemon --no-configuration-cache --no-build-cache 2>&1)
GRADLE_RC=$?
set -e

# ⚠️⚠️ **不要用 `X=$(cmd | grep ...)` 拿计数** ——
#    `set -e` + `pipefail` 下，grep 匹配不到就**整个脚本静默退出**，
#    而且是在"报告问题"之前就死了 ⇒ 报错信息全丢 ⇒ 看起来像"脚本坏了"。
#    ⇒ 一律用 `|| true` 兜住，让脚本**自己**去报告真正的原因。
FAILED_LINE=$(echo "$OUT" | grep -oE "[0-9]+ tests completed, [0-9]+ failed" | tail -1 || true)
RED=$(echo "$FAILED_LINE" | grep -oE "[0-9]+ failed" | grep -oE "[0-9]+" || true)
RED="${RED:-0}"

echo "$OUT" | grep -E "FAILED|tests completed|What went wrong" | head -10 || true

# ⚠️ **构建本身失败 ≠ 断言不符** —— 这两种情况必须分开报，
#    否则会把"环境问题"误读成"测试没有鉴别力"。
if [ "$GRADLE_RC" -ne 0 ] && [ -z "$FAILED_LINE" ]; then
  echo ""
  echo "✗ **构建/测试跑挂了，没拿到测试结果**（不是断言不符）"
  echo "  ⇒ 常见原因 ①：Kotlin daemon 跨版本串味（报 :webview-parkwoocheol:compileKotlinJvm FAILED / KLIB / ABI）"
  echo "     ⇒ **重跑一次通常就好了**（脚本已在跑前 --stop，但并发时仍可能撞上）"
  echo "  ⇒ 常见原因 ②：Gradle 缓存争用（'Unable to delete directory'）⇒ 隔几秒重试"
  echo "  ⇒ 常见原因 ③：--tests 过滤串写错 ⇒ 会报 'No tests found for given includes'"
  echo "  ⇒ 若反复出现：换 worktree 路径（每次用新目录，避免和上次的 daemon 抢句柄）"
  echo "  ⇒ 常见原因 ④：**变异本身编不过**（比如把 suspend 调用搬出了协程作用域）"
  echo "     ⇒ 这是**变异写错了**，不是测试没鉴别力 ⇒ 换个能编过的写法再试"
  echo ""
  echo "  --- 构建失败详情（前 25 行）---"
  echo "$OUT" | grep -A 3 "What went wrong" | head -25 || echo "$OUT" | tail -25
  exit 2
fi

if [ "$RED" -ne "$EXPECT_RED" ]; then
  echo ""
  echo "✗ 期望恰好 ${EXPECT_RED} 个红，实际 ${RED} 个 ⇒ 变异可能没生效，或影响面超出预期"
  echo "  （'恰好 1 个红'是变异测试的判据：影响面太宽说明这条断言不够有鉴别力）"
  exit 1
fi

echo ""
echo "✅ 恰好 ${EXPECT_RED} 个红 ⇒ 变异测试通过 ⇒ 该测试真的有鉴别力"
echo "   （共享工作树全程未被修改）"
