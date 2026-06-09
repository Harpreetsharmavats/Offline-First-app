package com.example.offlinefirstapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallpapers")
data class WallpaperEntity(
    @PrimaryKey val id: String,
    val url: String,
    val author: String,
    val description: String?
)
