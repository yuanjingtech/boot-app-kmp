package com.yuanjingtech.boot.app.kmp.data.theme

import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeDao
import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeSettings
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
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

    // endregion
}
