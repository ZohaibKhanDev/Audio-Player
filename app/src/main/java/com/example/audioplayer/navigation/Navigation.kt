@file:Suppress("NAME_SHADOWING")

package com.example.audioplayer.navigation

import ProfileEditScreen
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.QueueMusic
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.audioplayer.AudioItem
import com.example.audioplayer.loginauth.screens.LoginScreen
import com.example.audioplayer.loginauth.screens.MainScreen
import com.example.audioplayer.ui.Screen.AboutUs
import com.example.audioplayer.ui.Screen.DetailScreen
import com.example.audioplayer.ui.Screen.Fav
import com.example.audioplayer.ui.Screen.FavDetail
import com.example.audioplayer.ui.Screen.HomeScreen
import com.example.audioplayer.ui.Screen.My_Detail
import com.example.audioplayer.ui.Screen.My_Music
import com.example.audioplayer.ui.Screen.ProfileScreen
import com.example.audioplayer.ui.Screen.WorldSong

@Composable
fun Navigation(navController: NavHostController, audioItems: List<AudioItem>) {

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("SignUp", Context.MODE_PRIVATE)
    val sharedPreferencesId = sharedPreferences.getString("userId", null)
    val navController = rememberNavController()
    val destination = remember {
        if (sharedPreferencesId != null) {
            Screens.Home.route
        } else {
            Screens.MainScreen.route
        }
    }

    NavHost(navController = navController, startDestination = destination) {
        composable(Screens.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screens.MainScreen.route) {
            MainScreen(navController)
        }
        composable(Screens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Screens.Fav.route) {
            Fav(navController = navController)
        }

        composable(Screens.WorldSong.route) {
            WorldSong(navController = navController)
        }

        composable(Screens.My_Music.route) {
            My_Music(navController = navController, audioItems)
        }

        composable(
            Screens.Detail.route + "/{id}/{title}/{pic}/{audio}/{name}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                },

                navArgument("title") {
                    type = NavType.StringType
                },

                navArgument("pic") {
                    type = NavType.StringType
                },

                navArgument("audio") {
                    type = NavType.StringType
                },

                navArgument("name") {
                    type = NavType.StringType
                },
            )
        ) {
            val id = Uri.encode(it.arguments?.getString("id").toString())
            val tittle = Uri.encode(it.arguments?.getString("title"))
            val pic = it.arguments?.getString("pic")
            val audio = it.arguments?.getString("audio")
            val name = Uri.encode(it.arguments?.getString("name"))
            DetailScreen(navController = navController, id, tittle, pic, audio, name)
        }

        composable(
            Screens.My_Detail.route + "/{id}/{title}/{audio}/{name}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                },

                navArgument("title") {
                    type = NavType.StringType
                },


                navArgument("audio") {
                    type = NavType.StringType
                },

                navArgument("name") {
                    type = NavType.StringType
                },
            )
        ) {
            val id = Uri.encode(it.arguments?.getString("id").toString())
            val tittle = Uri.encode(it.arguments?.getString("title"))
            val audio = it.arguments?.getString("audio")
            val name = Uri.encode(it.arguments?.getString("name"))
            My_Detail(navController, id, tittle, audio, name)
        }

        composable(
            Screens.favDetail.route + "/{id}/{title}/{pic}/{audio}/{name}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                },

                navArgument("title") {
                    type = NavType.StringType
                },

                navArgument("pic") {
                    type = NavType.StringType
                },

                navArgument("audio") {
                    type = NavType.StringType
                },

                navArgument("name") {
                    type = NavType.StringType
                },
            )
        ) {
            val id = Uri.encode(it.arguments?.getString("id").toString())
            val tittle = Uri.encode(it.arguments?.getString("title"))
            val pic = it.arguments?.getString("pic")
            val audio = it.arguments?.getString("audio")
            val name = Uri.encode(it.arguments?.getString("name"))
            FavDetail(
                navController = navController,
                id,
                tittle,
                pic.toString(),
                audio.toString(),
                name
            )
        }

        composable(Screens.ProfileEdit.route) {
            ProfileEditScreen(navController = navController, userId = sharedPreferencesId.toString())
        }
        composable(route = Screens.Profile.route) {
            ProfileScreen(navController = navController)
        }

        composable(Screens.AboutUs.route) {
            AboutUs(navController = navController)
        }

    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavHost(navController: NavHostController) {
    var showBottomNav by remember { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    showBottomNav = currentRoute != Screens.LoginScreen.route && currentRoute != Screens.MainScreen.route

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavigation(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.MainScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.LoginScreen.route) {
                LoginScreen(navController = navController)
            }
            composable(Screens.MainScreen.route) {
                MainScreen(navController = navController)
            }
        }
    }
}


sealed class Screens(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : Screens("Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Fav : Screens("Fav", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    object WorldSong : Screens("World Song", Icons.Filled.QueueMusic, Icons.Outlined.QueueMusic)

    object My_Music : Screens("My Music", Icons.Filled.MusicNote, Icons.Outlined.MusicNote)
    object Detail : Screens("Detail", Icons.Filled.MusicNote, Icons.Outlined.MusicNote)
    object My_Detail : Screens("Detail", Icons.Filled.MusicNote, Icons.Outlined.MusicNote)
    object favDetail : Screens("favDetail", Icons.Filled.MusicNote, Icons.Outlined.MusicNote)
    object MainScreen : Screens("MainScreen", Icons.Filled.MusicNote, Icons.Outlined.MusicNote)
    object LoginScreen : Screens("LoginScren", Icons.Filled.MusicNote, Icons.Outlined.MusicNote)
    object Profile : Screens("Profile", Icons.Filled.MusicNote, Icons.Outlined.MusicNote)
    object ProfileEdit : Screens(
        "com.example.audioplayer.ui.Screen.ProfileEdit",
        Icons.Filled.MusicNote,
        Icons.Outlined.MusicNote
    )

    object AboutUs : Screens("AboutUs", Icons.Filled.MusicNote, Icons.Outlined.MusicNote)
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf(
        Screens.Home,
        Screens.Fav,
        Screens.WorldSong,
        Screens.My_Music
    )

    NavigationBar(containerColor = Color(0XFF1E1E1E)) {
        val navStack by navController.currentBackStackEntryAsState()
        val current = navStack?.destination?.route
        items.forEach {
            NavigationBarItem(
                selected = current == it.route,
                onClick = {
                    navController.navigate(it.route) {
                        navController.graph?.let { graph ->
                            graph.route?.let { route -> popUpTo(route) }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    val iconColor = if (current == it.route) Color.Red else Color.White
                    Icon(imageVector = if (current == it.route) it.selectedIcon else it.unselectedIcon, contentDescription = "", tint = iconColor)
                },
                label = {
                    val textColor = if (current == it.route) Color.Red else Color.White
                    Text(text = it.route, color = textColor)
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}
