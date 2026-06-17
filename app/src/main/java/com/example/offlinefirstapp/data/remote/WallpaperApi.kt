package com.example.offlinefirstapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WallpaperApi {
    @GET("v2/list")
    suspend fun getWallpapers(
        @Query("page") page: Int = 3,
        @Query("limit") limit: Int = 30
    ): List<WallpaperDto>

    companion object {
        const val BASE_URL = "https://picsum.photos/"
    }
}
