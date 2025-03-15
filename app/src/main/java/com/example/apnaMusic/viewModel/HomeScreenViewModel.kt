package com.example.apnaMusic.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apnaMusic.model.Album
import com.example.apnaMusic.model.Artist
import com.example.apnaMusic.networks.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val musicRepository: MusicRepository) :
    ViewModel() {

    val albums: StateFlow<List<Album>?> get() = musicRepository.albums
    val artists: StateFlow<List<Artist>?> get() = musicRepository.artists

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    init {
        fetchAlbumArtist()
    }

    private fun fetchAlbumArtist() {
        viewModelScope.launch {
            _isLoading.emit(true)
            _errorMessage.emit(null) // Reset error message
            val api1 =  launch {  fetchAlbum() }
            val api2 = launch {  fetchArtist() }
            api1.join()
            api2.join()
            _isLoading.emit(false)
        }
    }

    private suspend fun fetchAlbum() {
            try {
                musicRepository.fetchAlbums()
            } catch (e: Exception) {
                _errorMessage.emit("Failed to fetch album: ${e.localizedMessage}")
            }
    }

    private suspend fun fetchArtist() {
            try {
                musicRepository.fetchArtist()
            } catch (e: Exception) {
                _errorMessage.emit("Failed to fetch album: ${e.localizedMessage}")
            }
    }

}
