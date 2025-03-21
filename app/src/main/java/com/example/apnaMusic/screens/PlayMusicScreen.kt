package com.example.apnaMusic.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.apnaMusic.model.Tracks
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.SeekParameters
import com.example.apnaMusic.R
import kotlin.random.Random

@OptIn(UnstableApi::class)
@Composable
fun PlayMusicScreen(orderedTrackList: List<Tracks>, currentIndex: Int) {

    val trackList = remember { androidx.compose.runtime.snapshots.SnapshotStateList<Tracks>().apply { addAll(orderedTrackList) } }
    var currentTrackIndex by remember { mutableIntStateOf(currentIndex) }
    var isPlaying by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0f) }
    var isShuffle by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val currentTrack = trackList.getOrNull(currentTrackIndex)
    val duration = currentTrack?.duration?.toInt() ?: 0

    // Initialize ExoPlayer
    val player = remember {
        ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(context))
            .build().apply {
                setSeekParameters(SeekParameters.CLOSEST_SYNC)
                setForegroundMode(true)
                playbackParameters = PlaybackParameters(1f)
            }
    }

    // Function to Play Selected Track
    fun playTrack() {
        trackList[currentTrackIndex].let {
            val mediaItem = MediaItem.fromUri(it.audio)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = isPlaying // Maintain play state when switching tracks
        }
    }

    LaunchedEffect(currentTrack) {
        val wasPlaying = isPlaying
        progress = 0f // Reset slider when track changes
        // Ensure player is ready immediately
        if (player.playbackState == Player.STATE_IDLE) {
            player.prepare()
        }
        playTrack()
        isPlaying = wasPlaying
        if (wasPlaying) player.play() else player.pause()
    }

    // Track Progress Listener
    LaunchedEffect(isPlaying, currentTrackIndex) {
        while (isPlaying) {
            progress = player.currentPosition / 1000f // Convert ms to seconds
            kotlinx.coroutines.delay(500) // Update every 500ms
        }
    }

    //Auto-Play Next Track When Current Track Ends
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    if (currentTrackIndex <= trackList.size - 1) {
                        currentTrackIndex = (currentTrackIndex+1)% trackList.size  // Move to next track
                        isPlaying = true     // Keep playing
                    } else {
                        isPlaying = false    // Stop playback if no next song
                    }
                }
            }
        }
        player.addListener(listener)

        onDispose {
            player.removeListener(listener)
            player.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Gray, Color(0xFFD4AF37), Color.Black)
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Album Art
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.onplay_logo), // Replace with your actual SVG file name
                    contentDescription = "Album Art",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Start, // Align items to start
                verticalAlignment = Alignment.CenterVertically, // Align items vertically
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    isShuffle = !isShuffle
                    Toast.makeText(context, if (isShuffle) "Shuffle Mode On" else "Shuffle Mode Off", Toast.LENGTH_SHORT).show()

                    val currentTrack2 = trackList[currentTrackIndex] // Keep track of current song
                    val newList = if (isShuffle) orderedTrackList.shuffled() else orderedTrackList

                    trackList.clear()
                    trackList.addAll(newList)

                    // Restore the current song position in shuffled/non-shuffled list
                    currentTrackIndex = trackList.indexOfFirst { it.id == currentTrack2.id }
                })

                {
                    Image(
                        painter = if (isShuffle) painterResource(id = R.drawable.ic_shuffle) else painterResource(
                            id = R.drawable.ic_inorder
                        ),
                        contentDescription = "Shuffle",
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                    )
                }
                // Song Title
                trackList[currentTrackIndex].name?.split(" ")?.take(4)?.let {
                    Text(
                        text = returnPosition(
                            orderedTrackList,
                            trackList[currentTrackIndex]
                        ) + " " + it.joinToString(" "),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        color = Color.Black,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar & Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formatTime(progress.toInt()), color = Color.Black)
                Text(text = formatTime(duration), color = Color.Black)
            }
            Slider(
                value = progress,
                onValueChange = {},
                valueRange = 0f..duration.toFloat(),
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = SliderDefaults.colors(
                    disabledActiveTrackColor = Color.DarkGray, // Running bar color
                    disabledInactiveTrackColor = Color.Gray,   // Empty bar color
                    thumbColor = Color.Transparent // Hide the thumb
                ),
                interactionSource = remember { MutableInteractionSource() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Control Buttons
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    if (currentTrackIndex > 0) {
                        currentTrackIndex--
                    } else currentTrackIndex = orderedTrackList.size-1
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_previous),
                        contentDescription = "Previous",
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                    )
                }

                IconButton(onClick = {
                    isPlaying = !isPlaying
                    if (isPlaying) {
                        player.play()
                    } else {
                        player.pause()
                    }
                }) {
                    Image(
                        painter = if (isPlaying) painterResource(id = R.drawable.ic_pause) else painterResource(
                            id = R.drawable.ic_play
                        ),
                        contentDescription = "Play/Pause",
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                    )
                }

                IconButton(onClick = {
                        currentTrackIndex = (currentTrackIndex+1) % trackList.size
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_next),
                        contentDescription = "Next",
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%d:%02d", minutes, secs)
}

fun shuffleTracks(tracks: List<Tracks>): List<Tracks> {
    val shuffledList = tracks.toMutableList()
    for (i in shuffledList.size - 1 downTo 1) {
        val j = Random.nextInt(i + 1)
        shuffledList[i] = shuffledList[j].also { shuffledList[j] = shuffledList[i] }
    }
    return shuffledList
}

fun returnPosition(tracks: List<Tracks>, track: Tracks): String {
    val position = tracks.indexOfFirst { it.id == track.id }
    return if (position != -1) (position + 1).toString() else ""
}
