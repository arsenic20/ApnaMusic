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
}

