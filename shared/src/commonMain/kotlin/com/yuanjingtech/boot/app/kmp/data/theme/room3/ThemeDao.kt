package com.yuanjingtech.boot.app.kmp.data.theme.room3

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemeDao {
    @Query("SELECT * FROM theme_settings WHERE id = 1")
    fun getThemeSettings(): Flow<ThemeSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: ThemeSettings)
}