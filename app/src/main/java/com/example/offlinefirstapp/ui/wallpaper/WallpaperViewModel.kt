package com.example.offlinefirstapp.ui.wallpaper

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offlinefirstapp.data.local.WallpaperEntity
import com.example.offlinefirstapp.data.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    val wallpapers: StateFlow<List<WallpaperEntity>> = repository.getWallpapers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )


    init {
        viewModelScope.launch {
            wallpapers.collect { list ->
                Log.d("WallpaperRepo", "VM RECEIVED ${list.size}")
            }
        }

        refresh()
    }


    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.update { true }
            repository.refreshWallpapers()
            _isRefreshing.update { false }
        }
    }

    suspend fun getWallpaperById(id: String): WallpaperEntity? {
        return repository.getWallpaperById(id)
    }
}
