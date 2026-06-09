package com.example.offlinefirstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.offlinefirstapp.ui.theme.OfflineFirstAppTheme
import com.example.offlinefirstapp.ui.wallpaper.WallpaperScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OfflineFirstAppTheme {
                Surface {
                    WallpaperScreen(viewModel = hiltViewModel())
                }
            }
        }
    }
}
