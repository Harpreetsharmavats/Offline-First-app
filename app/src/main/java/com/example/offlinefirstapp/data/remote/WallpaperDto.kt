package com.example.offlinefirstapp.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class WallpaperDto(
    val id: String,
    val urls: WallpaperUrlsDto,
    val user: UserDto,
    val alt_description: String? = null
)

@Serializable
data class WallpaperUrlsDto(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)

@Serializable
data class UserDto(
    val name: String,
    val username: String
)
