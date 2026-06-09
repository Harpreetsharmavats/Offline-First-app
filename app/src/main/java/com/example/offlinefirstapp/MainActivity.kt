package com.example.offlinefirstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.offlinefirstapp.data.local.WallpaperEntity
import com.example.offlinefirstapp.ui.theme.OfflineFirstAppTheme
import com.example.offlinefirstapp.ui.wallpaper.WallpaperDetailScreen
import com.example.offlinefirstapp.ui.wallpaper.WallpaperScreen
import com.example.offlinefirstapp.ui.wallpaper.WallpaperViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OfflineFirstAppTheme {
                val navController = rememberNavController()
                val viewModel: WallpaperViewModel = hiltViewModel()

                Surface {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            WallpaperScreen(
                                viewModel = viewModel,
                                onWallpaperClick = { wallpaper ->
                                    navController.navigate("detail/${wallpaper.id}")
                                }
                            )
                        }
                        composable("detail/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")
                            val wallpaper = remember { mutableStateOf<WallpaperEntity?>(null) }

                            LaunchedEffect(id) {
                                if (id != null) {
                                    wallpaper.value = viewModel.getWallpaperById(id)
                                }
                            }

                            wallpaper.value?.let { entity ->
                                WallpaperDetailScreen(
                                    wallpaper = entity,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
