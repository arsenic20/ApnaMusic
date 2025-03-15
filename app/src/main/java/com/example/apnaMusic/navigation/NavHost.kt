package com.example.apnaMusic.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apnaMusic.R
import com.example.apnaMusic.screens.HomeScreen
import com.example.apnaMusic.screens.PlayListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val toolBarText by remember { mutableStateOf("ApnaMusic") }


    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToPlayList = { name,type ->
                    navController.navigate("playList/$type/$name")
                },
                toolBarText
            )
        }
        composable(
            route = "playList/{type}/{name}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""

            PlayListScreen(
                type = type,
                name = name,
                onSelectAlbumSong = {},
                onSelectArtistSong = {}
            )
        }

    }
}


