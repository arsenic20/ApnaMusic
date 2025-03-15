package com.example.apnaMusic.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.apnaMusic.R
import com.example.apnaMusic.model.Tracks

@Composable
fun TrackItem(track: Tracks?) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Left Side: Music Icon (Since no track image is available)
            Image(
                painter = painterResource(id = R.drawable.music_logo), // Replace with your actual drawable
                contentDescription = "Music Icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Right Side: Song Name & Duration
            Column {
                Text(
                    text = track?.name ?: "NA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "Duration ${formatDuration(track?.duration)}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatDuration(seconds: String?): String {
    val totalSeconds = seconds?.toIntOrNull() ?: return "0:00" // Handle invalid input
    val minutes = totalSeconds / 60
    val remainingSeconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds) // Ensures two-digit seconds
}