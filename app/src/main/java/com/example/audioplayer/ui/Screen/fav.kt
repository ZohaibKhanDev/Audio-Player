package com.example.audioplayer.ui.Screen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.audioplayer.db.Fav
import com.example.audioplayer.navigation.Screens
import com.example.audioplayer.restapi.MainViewModel
import com.example.audioplayer.restapi.ResultState
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun Fav(navController: NavController) {
    val viewModel: MainViewModel = koinInject()

    LaunchedEffect(Unit) {
        viewModel.getAllFav()
    }

    var favData by remember {
        mutableStateOf<List<Fav>?>(null)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    val state by viewModel.allFav.collectAsState()
    when (state) {
        is ResultState.Error -> {
            isLoading = false
            val error = (state as ResultState.Error).error
            Text(text = "$error")
        }

        ResultState.Loading -> {
            isLoading = true
        }

        is ResultState.Success -> {
            isLoading = false
            val success = (state as ResultState.Success).response
            favData = success
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Favourite Music", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0XFF171717)),
            actions = {
                Icon(imageVector = Icons.Filled.Favorite, contentDescription = "", tint = Color.Red)
            })
    }) { values ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0XFF171717))
                .padding(top = values.calculateTopPadding(), bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            favData?.let { result ->
                items(result) { fav ->
                    FavItem(fav = fav, navController)
                }
            }
        }
    }
}


@Composable
fun FavItem(fav: Fav, navController: NavController) {
    Card(modifier = Modifier
        .padding(6.dp)
        .clickable {
            val id = Uri.encode(fav.id.toString())
            val tittle = Uri.encode(fav.tittle)
            val pic = Uri.encode(fav.pic)
            val audio = Uri.encode(fav.audioUrl)
            val name = Uri.encode(fav.name)
            navController.navigate(Screens.favDetail.route + "/$id/$tittle/$pic/$audio/$name")
        }
        .width(140.dp)
        .height(210.dp),
        elevation = CardDefaults.cardElevation(1.dp, focusedElevation = 16.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)) {
        AsyncImage(
            model = fav.pic,
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .padding(11.dp)
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )
    }
}


