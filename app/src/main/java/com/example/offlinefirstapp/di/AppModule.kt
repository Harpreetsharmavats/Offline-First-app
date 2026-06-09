package com.example.offlinefirstapp.di

import android.app.Application
import androidx.room.Room
import com.example.offlinefirstapp.data.local.WallpaperDatabase
import com.example.offlinefirstapp.data.remote.WallpaperApi
import com.example.offlinefirstapp.data.repository.WallpaperRepository
import com.example.offlinefirstapp.data.repository.WallpaperRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWallpaperApi(): WallpaperApi {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(WallpaperApi.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(WallpaperApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWallpaperDatabase(app: Application): WallpaperDatabase {
        return Room.databaseBuilder(
            app,
            WallpaperDatabase::class.java,
            "wallpaper_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWallpaperRepository(
        api: WallpaperApi,
        db: WallpaperDatabase
    ): WallpaperRepository {
        return WallpaperRepositoryImpl(api, db.dao)
    }
}
