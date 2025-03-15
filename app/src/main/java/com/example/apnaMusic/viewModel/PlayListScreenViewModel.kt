package com.example.apnaMusic.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apnaMusic.model.Album
import com.example.apnaMusic.model.Artist
import com.example.apnaMusic.model.Tracks
import com.example.apnaMusic.networks.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayListScreenViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    savedStateHandle: SavedStateHandle

): ViewModel() {

    private val name: String = (savedStateHandle["name"] ?: "").replace(" ","+")
    private val type: String = savedStateHandle["type"] ?: ""

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    val albumTracks: StateFlow<List<Album>?> get()  = musicRepository.albumTracks
    val artistTracks: StateFlow<List<Artist>?> get()  = musicRepository.artistTracks

     fun fetchTracks() {
        viewModelScope.launch {
            _isLoading.emit(true)
            if(type == "album")
              musicRepository.fetchAlbumTracks(name)
            else musicRepository.fetchArtistTracks(name)
            _isLoading.emit(false)
        }
    }

}