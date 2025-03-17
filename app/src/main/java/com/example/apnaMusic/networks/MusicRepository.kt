package com.example.apnaMusic.networks

import com.example.apnaMusic.model.Album
import com.example.apnaMusic.model.Artist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class MusicRepository @Inject constructor(private val apiService: ApiService) {

    private val _albums = MutableStateFlow<List<Album>?>(null)
    val albums:StateFlow<List<Album>?> get() = _albums.asStateFlow()

    private val _artists = MutableStateFlow<List<Artist>?>(null)
    val artists: StateFlow<List<Artist>?> get() = _artists.asStateFlow()

    private val _albumTracks = MutableStateFlow<List<Album>>(emptyList())
    val albumTracks: StateFlow<List<Album>> get() = _albumTracks.asStateFlow()


    private val _artistTracks = MutableStateFlow<List<Artist>>(emptyList())
    val artistTracks: StateFlow<List<Artist>> get() = _artistTracks.asStateFlow()


    suspend fun fetchAlbums() {
       withContext(Dispatchers.IO){
           try {
               val response = apiService.getAlbums()
               if (response.headers.status == "success" && response.results.isNotEmpty()) {
                   _albums.emit(response.results)
               } else {
                   throw IOException("Failed to load album: ${response.headers.error_message}")
               }
           } catch (e: IOException) {
               _albums.emit(null) // Emit null to signify no data
               throw e
           }
       }
    }

    suspend fun fetchArtist() {
       withContext(Dispatchers.IO){
           try {
               val response = apiService.getArtists()
               if (response.headers.status == "success" && response.results.isNotEmpty()) {
                   _artists.emit(response.results)
               } else {
                   throw IOException("Failed to load album: ${response.headers.error_message}")
               }
           } catch (e: IOException) {
               _artists.emit(null) // Emit null to signify no data
               throw e
           }
       }
    }

    suspend fun fetchAlbumTracks(albumName: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAlbumTracks(albumName =albumName)
                if (response.headers.status == "success" && response.results.isNotEmpty()) {
                    _albumTracks.emit(response.results)  // Ensure this is always called
                } else {
                    _albumTracks.emit(emptyList())  // Set an empty list if no tracks are found
                }
            } catch (e: IOException) {
                _albumTracks.emit(emptyList())  // Handle errors by emitting an empty list
            }
        }
    }


    suspend fun fetchArtistTracks(artistName: String) {
        withContext(Dispatchers.IO){
            try {
                val response = apiService.getArtistTracks(artistName = artistName)
                if (response.headers.status == "success" && response.results.isNotEmpty()) {
                    _artistTracks.emit(response.results)
                } else {
                    _artistTracks.emit(emptyList())
                }
            } catch (e: IOException) {
                _artistTracks.emit(emptyList()) // Emit null to signify no data
            }
        }
    }
}

