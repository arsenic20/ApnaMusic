package com.example.apnaMusic.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apnaMusic.model.Album
import com.example.apnaMusic.model.Artist
import com.example.apnaMusic.viewModel.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPlayList: (String, String) -> Unit,
    toolBarText: String
) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()
    val album: State<List<Album>?> = viewModel.albums.collectAsState()
    val artist: State<List<Artist>?> = viewModel.artists.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = toolBarText,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarColors(Color.Gray, Color.Gray, Color.Gray, Color.Gray, Color.Gray)
            )
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Gray, Color(0xFF654321), Color.Black)
                    ))
                /*.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFff7e5f),
                            Color(0xFFfd3a69)
                        ) // Light Blue → Dark Blue
                    )
                )*/
                .padding(innerPadding),
            contentAlignment = Alignment.Center

        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.Gray)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Space between album & artist
                ) {
                    Spacer(modifier = Modifier.height(16.dp)) // Extra spacing between album & artist
                    album.value?.let { AlbumCarousel(it, onNavigateToPlayList) }
                    Spacer(modifier = Modifier.height(16.dp)) // Extra spacing between album & artist
                    artist.value?.let { ArtistSlider(it, onNavigateToPlayList) }
                }
            }
        }
    }
}
