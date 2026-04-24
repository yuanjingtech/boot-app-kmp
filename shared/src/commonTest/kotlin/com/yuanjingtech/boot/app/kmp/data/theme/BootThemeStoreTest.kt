package com.yuanjingtech.boot.app.kmp.data.theme

import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeDao
import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeSettings
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
}
