package com.example.offlinefirstapp.data.repository

import com.example.offlinefirstapp.data.local.WallpaperDao
import com.example.offlinefirstapp.data.local.WallpaperEntity
import com.example.offlinefirstapp.data.remote.WallpaperApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(
    private val api: WallpaperApi,
    private val dao: WallpaperDao
) : WallpaperRepository {

    override fun getWallpapers(): Flow<List<WallpaperEntity>> {
        return dao.getAllWallpapers()
    }

    override suspend fun getWallpaperById(id: String): WallpaperEntity? {
        return dao.getWallpaperById(id)
    }

    override suspend fun refreshWallpapers() {
        try {
            val remoteWallpapers = api.getWallpapers()
            val entities = remoteWallpapers.map { dto ->
                WallpaperEntity(
                    id = dto.id,
                    url = dto.download_url,
                    author = dto.author,
                    description = "Captured by ${dto.author} (Remote)"
                )
            }
            
            dao.clearAll() // Always clear before updating with fresh data
            if (entities.isNotEmpty()) {
                dao.insertWallpapers(entities)
            } else {
                insertMockDataIfEmpty()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            dao.clearAll() // Clear even on failure if requested to refresh
            insertMockDataIfEmpty()
        }
    }

    private suspend fun insertMockDataIfEmpty() {
        val mockData = listOf(
            WallpaperEntity("101", "https://images.unsplash.com/photo-1506744038136-46273834b3fb?q=80&w=1000", "Nature Enthusiast", "Breathtaking Valley View"),
            WallpaperEntity("102", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?q=80&w=1000", "Mountain Explorer", "Ethereal Mountain Mist"),
            WallpaperEntity("103", "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?q=80&w=1000", "Forest Wanderer", "Golden Sunbeams in Ancient Woods"),
            WallpaperEntity("104", "https://images.unsplash.com/photo-1501785888041-af3ef285b470?q=80&w=1000", "Serenity Seeker", "Crystal Clear Alpine Lake"),
            WallpaperEntity("105", "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?q=80&w=1000", "Field Dreamer", "Infinite Rolling Green Hills"),
            WallpaperEntity("106", "https://images.unsplash.com/photo-1532270660266-d47260c7521c?q=80&w=1000", "Desert Nomad", "Majestic Sand Dunes at Sunset"),
            WallpaperEntity("107", "https://images.unsplash.com/photo-1500382017468-9049fed747ef?q=80&w=1000", "Sky Watcher", "Vibrant Sunset over Rural Landscape"),
            WallpaperEntity("108", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?q=80&w=1000", "Peak Climber", "Snow-Capped Mountain Range"),
            WallpaperEntity("109", "https://images.unsplash.com/photo-1502082553048-f009c37129b9?q=80&w=1000", "Park Walker", "Lush Summer Park Canopy"),
            WallpaperEntity("110", "https://images.unsplash.com/photo-1433086563844-c71097b148fa?q=80&w=1000", "Waterfall Fan", "Tropical Jungle Waterfall"),
            WallpaperEntity("111", "https://images.unsplash.com/photo-1426604966848-d7adac402bc4?q=80&w=1000", "Island Hopper", "Dramatic Coastal Cliffs"),
            WallpaperEntity("112", "https://images.unsplash.com/photo-1475924156734-496f6acc671e?q=80&w=1000", "Beach Lover", "Ocean Waves at Dawn")
        )
        dao.insertWallpapers(mockData)
    }
}
