package com.example.offlinefirstapp.ui.wallpaper

import android.app.WallpaperManager
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.offlinefirstapp.data.local.WallpaperEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperDetailScreen(
    wallpaper: WallpaperEntity,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isSettingWallpaper by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            AsyncImage(
                model = wallpaper.url,
                contentDescription = wallpaper.description,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Photo by ${wallpaper.author}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                if (!wallpaper.description.isNullOrEmpty()) {
                    Text(
                        text = wallpaper.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            isSettingWallpaper = true
                            val success = setWallpaper(context, wallpaper.url)
                            isSettingWallpaper = false
                            if (success) {
                                Toast.makeText(context, "Wallpaper set!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to set wallpaper", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSettingWallpaper
                ) {
                    if (isSettingWallpaper) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Set as Wallpaper")
                    }
                }
            }
        }
    }
}

private suspend fun setWallpaper(context: android.content.Context, url: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val loader = context.imageLoader
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false) // Important for Bitmap conversion
                .build()

            val result = (loader.execute(request) as? SuccessResult)?.drawable
            val bitmap = (result as? BitmapDrawable)?.bitmap

            if (bitmap != null) {
                val wallpaperManager = WallpaperManager.getInstance(context)
                wallpaperManager.setBitmap(bitmap)
                true
            } else false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
