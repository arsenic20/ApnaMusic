package com.example.apnaMusic.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory

@Composable
fun AudioPlayerScreen() {
    val context = LocalContext.current

    // Initialize ExoPlayer with updated MediaSourceFactory
    val player = remember {
        ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(context)) // Required in 1.5.1+
            .build().apply {
                val mediaItem = MediaItem.fromUri("https://docs.google.com/uc?export=download&id=1c9zgFVgWcxhohbelr5_LuAayf8iGU9eb")
                setMediaItem(mediaItem)
                prepare()
            }
    }

    var isPlaying by remember { mutableStateOf(false) }

    // Release player when composable is removed
    DisposableEffect (Unit) {
        onDispose {
            player.release()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (isPlaying) {
                        player.pause()
                    } else {
                        player.play()
                    }
                    isPlaying = !isPlaying
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(if (isPlaying) "Pause" else "Play")
            }
        }
    }
}
