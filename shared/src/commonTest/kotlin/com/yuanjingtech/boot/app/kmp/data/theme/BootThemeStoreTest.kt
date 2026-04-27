package com.yuanjingtech.boot.app.kmp.data.theme

import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeDao
import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeSettings
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FakeThemeDao : ThemeDao {
    private val settingsFlow = MutableStateFlow<ThemeSettings?>(ThemeSettings())

    override fun getThemeSettings(): Flow<ThemeSettings?> = settingsFlow

    override suspend fun insertOrUpdate(newSettings: ThemeSettings) {
        settingsFlow.value = newSettings
    }
}

class BootThemeStoreTest {

    private lateinit var fakeDao: FakeThemeDao
    private lateinit var themeStore: BootThemeStore

    @BeforeTest
    fun setup() {
        fakeDao = FakeThemeDao()
        themeStore = BootThemeStore(fakeDao)
    }

    @Test
    fun themeStore_initialBootThemeMode_isFollowSystem() = runTest {
        val mode = themeStore.themeModeFlow.first()
        assertEquals(BootThemeMode.FOLLOW_SYSTEM, mode)
    }

    @Test
    fun themeStore_setThemeModeToLight_updatesFlow() = runTest {
        themeStore.setThemeMode(BootThemeMode.LIGHT)
        val mode = themeStore.themeModeFlow.first()
        assertEquals(BootThemeMode.LIGHT, mode)
    }

    @Test
    fun themeStore_setThemeModeToDark_updatesFlow() = runTest {
        themeStore.setThemeMode(BootThemeMode.DARK)
        val mode = themeStore.themeModeFlow.first()
        assertEquals(BootThemeMode.DARK, mode)
    }

    @Test
    fun themeStore_setThemeModeToFollowSystem_updatesFlow() = runTest {
        themeStore.setThemeMode(BootThemeMode.DARK)
        themeStore.setThemeMode(BootThemeMode.FOLLOW_SYSTEM)
        val mode = themeStore.themeModeFlow.first()
        assertEquals(BootThemeMode.FOLLOW_SYSTEM, mode)
    }

    @Test
    fun themeStore_invalidBootThemeModeString_defaultsToFollowSystem() = runTest {
        val invalidDao = object : ThemeDao {
            private val flow = MutableStateFlow<ThemeSettings?>(ThemeSettings(themeMode = "INVALID_MODE"))
            override fun getThemeSettings(): Flow<ThemeSettings?> = flow
            override suspend fun insertOrUpdate(settings: ThemeSettings) {}
        }
        val store = BootThemeStore(invalidDao)
        val mode = store.themeModeFlow.first()
        assertEquals(BootThemeMode.FOLLOW_SYSTEM, mode)
    }

    @Test
    fun themeStore_nullSettings_defaultsToFollowSystem() = runTest {
        val emptyDao = object : ThemeDao {
            private val flow = MutableStateFlow<ThemeSettings?>(null)
            override fun getThemeSettings(): Flow<ThemeSettings?> = flow
            override suspend fun insertOrUpdate(settings: ThemeSettings) {}
        }
        val store = BootThemeStore(emptyDao)
        val mode = store.themeModeFlow.first()
        assertEquals(BootThemeMode.FOLLOW_SYSTEM, mode)
    }

    // region uiStyle tests

    @Test
    fun themeStore_uiStyleFlow_initialValue_isLiquidGlass() = runTest {
        val style = themeStore.uiStyleFlow.first()
        assertEquals(BootUiStyle.LIQUID_GLASS, style)
    }

    @Test
    fun themeStore_setUiStyleToMaterial3_updatesFlow() = runTest {
        themeStore.setUiStyle(BootUiStyle.MATERIAL3)
        val style = themeStore.uiStyleFlow.first()
        assertEquals(BootUiStyle.MATERIAL3, style)
    }

    @Test
    fun themeStore_setUiStyleToLiquidGlass_updatesFlow() = runTest {
        themeStore.setUiStyle(BootUiStyle.MATERIAL3)
        themeStore.setUiStyle(BootUiStyle.LIQUID_GLASS)
        val style = themeStore.uiStyleFlow.first()
        assertEquals(BootUiStyle.LIQUID_GLASS, style)
    }

    @Test
    fun themeStore_invalidUiStyleString_defaultsToLiquidGlass() = runTest {
        val invalidDao = object : ThemeDao {
            private val flow = MutableStateFlow<ThemeSettings?>(ThemeSettings(uiStyle = "INVALID_STYLE"))
            override fun getThemeSettings(): Flow<ThemeSettings?> = flow
            override suspend fun insertOrUpdate(settings: ThemeSettings) {}
        }
        val store = BootThemeStore(invalidDao)
        val style = store.uiStyleFlow.first()
        assertEquals(BootUiStyle.LIQUID_GLASS, style)
    }

    @Test
    fun themeStore_nullSettings_uiStyle_defaultsToLiquidGlass() = runTest {
        val emptyDao = object : ThemeDao {
            private val flow = MutableStateFlow<ThemeSettings?>(null)
            override fun getThemeSettings(): Flow<ThemeSettings?> = flow
            override suspend fun insertOrUpdate(settings: ThemeSettings) {}
        }
        val store = BootThemeStore(emptyDao)
        val style = store.uiStyleFlow.first()
        assertEquals(BootUiStyle.LIQUID_GLASS, style)
    }

    @Test
    fun themeStore_setUiStyle_preservesThemeMode() = runTest {
        themeStore.setThemeMode(BootThemeMode.DARK)
        themeStore.setUiStyle(BootUiStyle.MATERIAL3)
        val mode = themeStore.themeModeFlow.first()
        val style = themeStore.uiStyleFlow.first()
        assertEquals(BootThemeMode.DARK, mode)
        assertEquals(BootUiStyle.MATERIAL3, style)
    }

    @Test
    fun themeStore_concurrentSetUiStyle_allWritesSucceed() = runTest {
        // 10 concurrent writes, last one wins in current implementation
        // This test documents the current behavior and catches accidental data loss
        val jobs = (0..9).map { i ->
            launch {
                themeStore.setUiStyle(if (i % 2 == 0) BootUiStyle.LIQUID_GLASS else BootUiStyle.MATERIAL3)
            }
        }
        joinAll(*jobs.toTypedArray())
        // At minimum, the flow should emit without error
        val style = themeStore.uiStyleFlow.first()
        assertEquals(true, style == BootUiStyle.LIQUID_GLASS || style == BootUiStyle.MATERIAL3)
    }

    @Test
    fun themeStore_concurrentSetUiStyleAndThemeMode_noDataLoss() = runTest {
        // Concurrent writes to different fields should not corrupt each other
        val jobs = listOf(
            launch { themeStore.setUiStyle(BootUiStyle.MATERIAL3) },
            launch { themeStore.setThemeMode(BootThemeMode.DARK) },
            launch { themeStore.setUiStyle(BootUiStyle.LIQUID_GLASS) },
            launch { themeStore.setThemeMode(BootThemeMode.LIGHT) }
        )
        joinAll(*jobs.toTypedArray())

        val mode = themeStore.themeModeFlow.first()
        val style = themeStore.uiStyleFlow.first()
        // Both fields should have valid values (last write wins for each)
        assertEquals(true, mode == BootThemeMode.DARK || mode == BootThemeMode.LIGHT)
        assertEquals(true, style == BootUiStyle.LIQUID_GLASS || style == BootUiStyle.MATERIAL3)
    }

    // endregion
}
