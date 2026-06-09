package com.example.offlinefirstapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDao {
    @Query("SELECT * FROM wallpapers")
    fun getAllWallpapers(): Flow<List<WallpaperEntity>>

    @Query("SELECT * FROM wallpapers WHERE id = :id")
    suspend fun getWallpaperById(id: String): WallpaperEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpapers(wallpapers: List<WallpaperEntity>)

    @Query("DELETE FROM wallpapers")
    suspend fun clearAll()
}
