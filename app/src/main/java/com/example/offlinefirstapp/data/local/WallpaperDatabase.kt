package com.example.offlinefirstapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WallpaperEntity::class], version = 1)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract val dao: WallpaperDao
}
