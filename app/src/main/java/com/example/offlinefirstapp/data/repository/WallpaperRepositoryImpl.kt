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
                    description = "High resolution photo by ${dto.author}"
                )
            }
            if (entities.isNotEmpty()) {
                dao.clearAll()
                dao.insertWallpapers(entities)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to more comprehensive mock data if DB is empty
            insertMockDataIfEmpty()
        }
    }

    private suspend fun insertMockDataIfEmpty() {
        val mockData = listOf(
            WallpaperEntity("10", "https://picsum.photos/id/10/1000/1500", "Paul Jarvis", "Professional Landscape"),
            WallpaperEntity("11", "https://picsum.photos/id/11/1000/1500", "Paul Jarvis", "Mountain View"),
            WallpaperEntity("12", "https://picsum.photos/id/12/1000/1500", "Paul Jarvis", "Nature Trails"),
            WallpaperEntity("13", "https://picsum.photos/id/13/1000/1500", "Paul Jarvis", "Calm Waters"),
            WallpaperEntity("14", "https://picsum.photos/id/14/1000/1500", "Paul Jarvis", "Coastal Line"),
            WallpaperEntity("15", "https://picsum.photos/id/15/1000/1500", "Paul Jarvis", "Forest Depth"),
            WallpaperEntity("16", "https://picsum.photos/id/16/1000/1500", "Paul Jarvis", "River Stones"),
            WallpaperEntity("17", "https://picsum.photos/id/17/1000/1500", "Paul Jarvis", "Pathways"),
            WallpaperEntity("18", "https://picsum.photos/id/18/1000/1500", "Paul Jarvis", "Meadows"),
            WallpaperEntity("19", "https://picsum.photos/id/19/1000/1500", "Paul Jarvis", "Lush Greenery"),
            WallpaperEntity("20", "https://picsum.photos/id/20/1000/1500", "Adam Gault", "Modern Architecture"),
            WallpaperEntity("21", "https://picsum.photos/id/21/1000/1500", "Alejandro Escamilla", "Abstract Art"),
            WallpaperEntity("22", "https://picsum.photos/id/22/1000/1500", "Alejandro Escamilla", "City Life"),
            WallpaperEntity("23", "https://picsum.photos/id/23/1000/1500", "Alejandro Escamilla", "Minimalist Sky"),
            WallpaperEntity("24", "https://picsum.photos/id/24/1000/1500", "Alejandro Escamilla", "Winter Frost")
        )
        dao.insertWallpapers(mockData)
    }
}
