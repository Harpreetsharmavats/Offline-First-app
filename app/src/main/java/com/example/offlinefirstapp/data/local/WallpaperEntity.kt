package com.example.offlinefirstapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallpapers_v2") // Change table name to force Room to ignore old data
data class WallpaperEntity(
    @PrimaryKey val id: String,
    val url: String,
    val author: String,
    val description: String?
)
