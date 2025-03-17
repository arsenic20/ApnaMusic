package com.example.apnaMusic.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.apnaMusic.R
import com.example.apnaMusic.model.Tracks
import com.example.apnaMusic.viewModel.PlayListScreenViewModel

@Composable
fun PlayListScreen(
    type: String,
    name: String,
    onSelectPlayListSong: (List<Tracks>, Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val viewModel = hiltViewModel<PlayListScreenViewModel>()
    val albumTracks by viewModel.albumTracks.collectAsState()
    val artistTracks by viewModel.artistTracks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Extracted track list, image, and date based on type
    val (tracks, imageUrl, releaseDate) = when (type) {
        "album" -> Triple(
            albumTracks?.flatMap { it.tracks ?: emptyList() } ?: emptyList(),
            albumTracks?.firstOrNull()?.image ?: "NA",
            albumTracks?.firstOrNull()?.releasedate ?: "NA"
        )

        else -> Triple(
            artistTracks?.flatMap { it.tracks ?: emptyList() } ?: emptyList(),
            artistTracks?.firstOrNull()?.image ?: "NA",
            artistTracks?.firstOrNull()?.joindate ?: "NA"
        )
    }

    LaunchedEffect(Unit) {
        viewModel.fetchTracks()
    }

    Scaffold(
        topBar = { PlayListTopBar("PlayList",onNavigateBack) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Gray, Color(0xFFD4AF37), Color.Black)
                    )
                )
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> CircularProgressIndicator(color = Color.Gray)
                tracks.isEmpty() -> Text("No Tracks Found", color = Color.White, fontSize = 18.sp)
                else -> TrackListContent(tracks, imageUrl, releaseDate, name, onSelectPlayListSong)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayListTopBar(title: String, onNavigateBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                )
        },
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun TrackListContent(
    tracks: List<Tracks>,
    imageUrl: String,
    releaseDate: String,
    name: String,
    onSelectPlayListSong: (List<Tracks>, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 0.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Space between components
    ) {
        ImageCard(imageUrl, name, releaseDate)
        Text(
            text = "${tracks.size} Songs",
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Gray)
                .padding(10.dp)
        )
        LazyColumn {
            items(tracks.size) { index ->
                TrackItem(tracks.get(index),
                    onItemSelected = {
                        onSelectPlayListSong(tracks, index)
                    }
                )
            }
        }
    }
}

@Composable
fun ImageCard(imageUrl: String, name: String, releaseDate: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.Bottom // Align text at the bottom
    ) {
        // Left Side: Image inside a Card
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(180.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .error(R.drawable.image_not_found) // Default image if URL fails
                    .build(),
                contentDescription = "Album/Artist Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Right Side: Name & Release Date aligned at bottom-end
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp), // Padding to align text properly
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End // Align text to the right
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = releaseDate,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}
