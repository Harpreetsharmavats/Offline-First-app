package com.example.offlinefirstapp.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class WallpaperDto(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String
)
