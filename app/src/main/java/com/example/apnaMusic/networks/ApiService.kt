package com.example.apnaMusic.networks

import com.example.apnaMusic.model.Album
import com.example.apnaMusic.model.ApiResponse
import com.example.apnaMusic.model.Artist
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v3.0/artists/")
    suspend fun getArtists(
        @Query("client_id") clientId: String = "5407f3d9",
        @Query("limit") limit: Int = 20
    ): ApiResponse<Artist>

    @GET("v3.0/albums/")
    suspend fun getAlbums(
        @Query("client_id") clientId: String = "5407f3d9",
        @Query("limit") limit: Int = 20
    ): ApiResponse<Album>

    @GET("v3.0/albums/tracks/")
    suspend fun getAlbumTracks(
        @Query("client_id") clientId: String = "5407f3d9",
        @Query("name",encoded = true) albumName: String
    ): ApiResponse<Album>

    @GET("v3.0/artists/tracks/")
    suspend fun getArtistTracks(
        @Query("client_id") clientId: String = "5407f3d9",
        @Query("name",encoded = true) artistName: String
    ): ApiResponse<Artist>

}
