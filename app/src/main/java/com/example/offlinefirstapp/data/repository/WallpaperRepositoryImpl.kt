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
                // Use a smaller version for the grid
                val optimizedUrl = "https://picsum.photos/id/${dto.id}/800/1200"

                WallpaperEntity(
                    id = dto.id,
                    url = optimizedUrl,
                    author = dto.author,
                    description = "NEW LIVE: ${dto.author}"
                )
            }
            
            dao.clearAll() 
            if (entities.isNotEmpty()) {
                dao.insertWallpapers(entities)
            } else {
                insertMockData()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            dao.clearAll() 
            insertMockData()
        }
    }

    private suspend fun insertMockData() {
        val mockData = listOf(
            WallpaperEntity("n1", "https://images.unsplash.com/photo-1506744038136-46273834b3fb?q=80&w=1000", "Nature", "NEW OFFLINE: Valley"),
            WallpaperEntity("n2", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?q=80&w=1000", "Mountain", "NEW OFFLINE: Mist"),
            WallpaperEntity("n3", "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?q=80&w=1000", "Forest", "NEW OFFLINE: Woods"),
            WallpaperEntity("n4", "https://images.unsplash.com/photo-1501785888041-af3ef285b470?q=80&w=1000", "Lake", "NEW OFFLINE: Alpine"),
            WallpaperEntity("n5", "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?q=80&w=1000", "Field", "NEW OFFLINE: Hills"),
            WallpaperEntity("n6", "https://images.unsplash.com/photo-1532270660266-d47260c7521c?q=80&w=1000", "Desert", "NEW OFFLINE: Dunes"),
            WallpaperEntity("n7", "https://images.unsplash.com/photo-1500382017468-9049fed747ef?q=80&w=1000", "Sky", "NEW OFFLINE: Sunset"),
            WallpaperEntity("n8", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?q=80&w=1000", "Peaks", "NEW OFFLINE: Snow"),
            WallpaperEntity("n9", "https://images.unsplash.com/photo-1502082553048-f009c37129b9?q=80&w=1000", "Park", "NEW OFFLINE: Canopy"),
            WallpaperEntity("n10", "https://images.unsplash.com/photo-1433086563844-c71097b148fa?q=80&w=1000", "Waterfall", "NEW OFFLINE: Jungle"),
            WallpaperEntity("n11", "https://images.unsplash.com/photo-1426604966848-d7adac402bc4?q=80&w=1000", "Island", "NEW OFFLINE: Cliffs"),
            WallpaperEntity("n12", "https://images.unsplash.com/photo-1475924156734-496f6acc671e?q=80&w=1000", "Beach", "NEW OFFLINE: Dawn")
        )
        dao.insertWallpapers(mockData)
    }
}
