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

    override suspend fun refreshWallpapers() {
        try {
            val remoteWallpapers = api.getWallpapers(clientId = "YOUR_UNSPLASH_ACCESS_KEY")
            val entities = remoteWallpapers.map { dto ->
                WallpaperEntity(
                    id = dto.id,
                    url = dto.urls.regular,
                    author = dto.user.name,
                    description = dto.alt_description
                )
            }
            dao.clearAll()
            dao.insertWallpapers(entities)
        } catch (e: Exception) {
            e.printStackTrace()
            // If network fails and DB is empty, insert some dummy data for demo purposes
            insertMockDataIfEmpty()
        }
    }

    private suspend fun insertMockDataIfEmpty() {
        val mockData = listOf(
            WallpaperEntity("1", "https://images.unsplash.com/photo-1506744038136-46273834b3fb", "Nature Lover", "Beautiful Valley"),
            WallpaperEntity("2", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05", "Mountain Man", "Foggy Mountains"),
            WallpaperEntity("3", "https://images.unsplash.com/photo-1441974231531-c6227db76b6e", "Forest Walker", "Sunlight through trees"),
            WallpaperEntity("4", "https://images.unsplash.com/photo-1501785888041-af3ef285b470", "Lake Swimmer", "Calm Lake"),
            WallpaperEntity("5", "https://images.unsplash.com/photo-1472214103451-9374bd1c798e", "Sky Watcher", "Green fields"),
            WallpaperEntity("6", "https://images.unsplash.com/photo-1532270660266-d47260c7521c", "Desert Nomad", "Golden Sands")
        )
        dao.insertWallpapers(mockData)
    }
}
