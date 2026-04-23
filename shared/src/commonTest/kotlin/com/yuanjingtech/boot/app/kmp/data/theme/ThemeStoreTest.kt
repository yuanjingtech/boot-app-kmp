package com.yuanjingtech.boot.app.kmp.data.theme

import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeDao
import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeSettings
import com.yuanjingtech.boot.app.kmp.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FakeThemeDao : ThemeDao {
    private val settingsFlow = MutableStateFlow(ThemeSettings())

    override fun getThemeSettings(): Flow<ThemeSettings> = settingsFlow

    override suspend fun insertOrUpdate(newSettings: ThemeSettings) {
        settingsFlow.value = newSettings
    }
}

class ThemeStoreTest {

    private lateinit var fakeDao: FakeThemeDao
    private lateinit var themeStore: ThemeStore

    @BeforeTest
    fun setup() {
        fakeDao = FakeThemeDao()
        themeStore = ThemeStore(fakeDao)
    }

    @Test
    fun themeStore_initialThemeMode_isFollowSystem() = runTest {
        val mode = themeStore.themeModeFlow.first()
        assertEquals(ThemeMode.FOLLOW_SYSTEM, mode)
    }

    @Test
    fun themeStore_setThemeModeToLight_updatesFlow() = runTest {
        themeStore.setThemeMode(ThemeMode.LIGHT)
        val mode = themeStore.themeModeFlow.first()
        assertEquals(ThemeMode.LIGHT, mode)
    }

    @Test
    fun themeStore_setThemeModeToDark_updatesFlow() = runTest {
        themeStore.setThemeMode(ThemeMode.DARK)
        val mode = themeStore.themeModeFlow.first()
        assertEquals(ThemeMode.DARK, mode)
    }

    @Test
    fun themeStore_setThemeModeToFollowSystem_updatesFlow() = runTest {
        themeStore.setThemeMode(ThemeMode.DARK)
        themeStore.setThemeMode(ThemeMode.FOLLOW_SYSTEM)
        val mode = themeStore.themeModeFlow.first()
        assertEquals(ThemeMode.FOLLOW_SYSTEM, mode)
    }

    @Test
    fun themeStore_invalidThemeModeString_defaultsToFollowSystem() = runTest {
        val invalidDao = object : ThemeDao {
            private val flow = MutableStateFlow(ThemeSettings(themeMode = "INVALID_MODE"))
            override fun getThemeSettings(): Flow<ThemeSettings> = flow
            override suspend fun insertOrUpdate(settings: ThemeSettings) {}
        }
        val store = ThemeStore(invalidDao)
        val mode = store.themeModeFlow.first()
        assertEquals(ThemeMode.FOLLOW_SYSTEM, mode)
    }
}
