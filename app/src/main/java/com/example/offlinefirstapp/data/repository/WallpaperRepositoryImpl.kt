package com.example.offlinefirstapp.data.repository

import android.util.Log
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
        Log.d("WallpaperRepo", "Refreshing wallpapers...")
        try {
            // FORCE CLEAR before anything else
            dao.clearAll()
            Log.d("WallpaperRepo", "Database cleared.")

            val remoteWallpapers = api.getWallpapers()
            Log.d("WallpaperRepo", "Fetched ${remoteWallpapers.size} from API")

            val entities = remoteWallpapers.map { dto ->
                val optimizedUrl = "https://picsum.photos/id/${dto.id}/800/1200"
                WallpaperEntity(
                    id = dto.id,
                    url = optimizedUrl,
                    author = dto.author,
                    description = "LATEST API DATA: ${dto.author}"
                )
            }
            
            if (entities.isNotEmpty()) {
                dao.insertWallpapers(entities)
                Log.d("WallpaperRepo", "Inserted ${entities.size} new records")
            } else {
                Log.d("WallpaperRepo", "API returned empty, using mock data")
                insertMockData()
            }
        } catch (e: Exception) {
            Log.e("WallpaperRepo", "Error refreshing", e)
            dao.clearAll() 
            insertMockData()
        }
    }

    private suspend fun insertMockData() {
        Log.d("WallpaperRepo", "Inserting mock data...")
        val mockData = listOf(
            WallpaperEntity("final1", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?q=80&w=1000", "Final Fallback", "LATEST OFFLINE 1"),
            WallpaperEntity("final2", "https://images.unsplash.com/photo-1501785888041-af3ef285b470?q=80&w=1000", "Final Fallback", "LATEST OFFLINE 2"),
            WallpaperEntity("final3", "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?q=80&w=1000", "Final Fallback", "LATEST OFFLINE 3"),
            WallpaperEntity("final4", "https://images.unsplash.com/photo-1506744038136-46273834b3fb?q=80&w=1000", "Final Fallback", "LATEST OFFLINE 4"),
            WallpaperEntity("final5", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?q=80&w=1000", "Final Fallback", "LATEST OFFLINE 5")
        )
        dao.insertWallpapers(mockData)
        Log.d("WallpaperRepo", "Mock data inserted")
    }
}
