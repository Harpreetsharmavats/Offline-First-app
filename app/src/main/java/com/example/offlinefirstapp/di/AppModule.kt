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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideWallpaperApi(okHttpClient: OkHttpClient): WallpaperApi {
        val contentType = "application/json".toMediaType()
        val json = Json { 
            ignoreUnknownKeys = true
            coerceInputValues = true // Useful if API returns null for non-nullable fields
        }
        return Retrofit.Builder()
            .baseUrl(WallpaperApi.BASE_URL)
            .client(okHttpClient)
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
