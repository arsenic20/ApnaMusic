package com.example.apnaMusic.navigation

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apnaMusic.model.Tracks
import com.example.apnaMusic.screens.HomeScreen
import com.example.apnaMusic.screens.PlayListScreen
import com.example.apnaMusic.screens.PlayMusicScreen
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

@OptIn(UnstableApi::class)
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
                onSelectPlayListSong = { trackList, selectedIndex ->
                        val trackListJson = Uri.encode(Gson().toJson(trackList))
                        navController.navigate("playMusic/$trackListJson/$selectedIndex")
                },
                onNavigateBack = {navController.popBackStack()}
            )
        }

        // Add PlayMusicScreen navigation
        composable(
            route = "playMusic/{trackListJson}/{currentIndex}",
            arguments = listOf(
                navArgument("trackListJson") { type = NavType.StringType },
                navArgument("currentIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val trackListJson = backStackEntry.arguments?.getString("trackListJson") ?: "[]"
            val currentIndex = backStackEntry.arguments?.getInt("currentIndex") ?: 0

            val trackList: List<Tracks> = try {
            Gson().fromJson(trackListJson, object : TypeToken<List<Tracks>>() {}.type)
            } catch (e: Exception) {
                emptyList()
            }

            PlayMusicScreen(trackList, currentIndex)
        }

    }
}


