package com.example.offlinefirstapp.data.repository

import com.example.offlinefirstapp.data.local.WallpaperEntity
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {
    fun getWallpapers(): Flow<List<WallpaperEntity>>
    suspend fun getWallpaperById(id: String): WallpaperEntity?
    suspend fun refreshWallpapers()
}
