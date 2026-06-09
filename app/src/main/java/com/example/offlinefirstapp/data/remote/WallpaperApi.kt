package com.example.offlinefirstapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WallpaperApi {
    @GET("photos")
    suspend fun getWallpapers(
        @Query("client_id") clientId: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): List<WallpaperDto>

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
    }
}
