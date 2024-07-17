@file:Suppress("NAME_SHADOWING")

package com.example.audioplayer.navigation

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
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
import androidx.compose.runtime.remember
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
import com.example.audioplayer.ui.Screen.Fav
import com.example.audioplayer.ui.Screen.DetailScreen
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

        composable(Screens.MainScreen.route) {
            MainScreen(navController)
        }
        composable(Screens.LoginScreen.route) {
            LoginScreen(navController)
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
fun NavEntry(audioItems: List<AudioItem>) {
    val navController = rememberNavController()
    Scaffold(bottomBar = { ButtonNavigation(navController = navController) }) {
        Navigation(navController = navController, audioItems)
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
    object AboutUs : Screens("AboutUs", Icons.Filled.MusicNote, Icons.Outlined.MusicNote)
}

@Composable
fun ButtonNavigation(navController: NavHostController) {
    val item = listOf(
        Screens.Home,
        Screens.Fav,
        Screens.WorldSong,
        Screens.My_Music
    )

    NavigationBar(containerColor = Color(0XFF1E1E1E)) {
        val navStack by navController.currentBackStackEntryAsState()
        val current = navStack?.destination?.route
        item?.forEach {
            NavigationBarItem(selected = current == it.route, onClick = {
                navController.navigate(it.route) {
                    navController.graph?.let {
                        it.route?.let { it1 -> popUpTo(it1) }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }, icon = {
                if (current == it.route) {
                    Icon(imageVector = it.selectedIcon, contentDescription = "", tint = Color.Red)
                } else {
                    Icon(
                        imageVector = it.unselectedIcon,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }, label = {
                if (current == it.route) {
                    Text(text = it.route, color = Color.Red)
                } else {
                    Text(text = it.route, color = Color.White)
                }
            }, colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent))
        }
    }


}

fun getAudioList(context: Context): List<String> {
    val audioList = mutableListOf<String>()
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.MIME_TYPE
    )
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null
    )

    cursor?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        while (cursor.moveToNext()) {
            val audioId = cursor.getLong(idColumn)
            val audioUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioId
            )
            audioList.add(audioUri.toString())
        }
    }

    return audioList
}