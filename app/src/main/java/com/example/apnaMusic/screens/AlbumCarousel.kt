package com.example.apnaMusic.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.apnaMusic.R
import com.example.apnaMusic.model.Album

@Composable
fun AlbumCarousel(artists: List<Album>, onNavigateToPlayList: (String, String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().width(250.dp) // Ensures alignment consistency
    ) {
        Text(
            text = "Artists Album",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(10.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(artists.size) { index ->
                val item = artists[index]
                ImageCard(
                    item.image,
                    item.name ?: "NA",
                    item.releasedate ?: "NA",
                    onNavigateToPlayList
                )
            }
        }
    }
}
@Composable
fun ImageCard(
    imageUrl: String?,
    name: String,
    joinDate: String,
    onNavigateToPlayList: (String, String) -> Unit
) {
    Column(
        //horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(180.dp).clickable {
            onNavigateToPlayList(name,"album")
        } // Ensures alignment consistency
    ) {
        // Card with Image
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
                contentDescription = "Carousel Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Spacing between Card and Text
        Spacer(modifier = Modifier.height(8.dp))

        // Name & Join Date (Below the Card)
        Text(
            text = name,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(5.dp)
        )
        Text(
            text = joinDate,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(5.dp)
        )
    }
}
