package com.example.audioplayer.ui.Screen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.audioplayer.album.Data
import com.example.audioplayer.album.WorldSong
import com.example.audioplayer.navigation.Screens
import com.example.audioplayer.restapi.MainViewModel
import com.example.audioplayer.restapi.ResultState
import org.koin.compose.koinInject

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldSong(navController: NavController) {
    val viewModel: MainViewModel = koinInject()
    var worldData by remember {
        mutableStateOf<WorldSong?>(null)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        isLoading = true
        viewModel.getAllWorld()
    }

    val state by viewModel.allWorld.collectAsState()
    when (state) {
        is ResultState.Error -> {
            isLoading = false
            val error = (state as ResultState.Error).error
            Text(text = error.toString())
        }

        ResultState.Loading -> {
            isLoading = true
        }

        is ResultState.Success -> {
            isLoading = false
            val success = (state as ResultState.Success).response
            worldData = success
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "World Songs", color = Color.White)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0XFF171717)),
                actions = {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color.White,
                        modifier = Modifier.clickable {
                            isLoading = true
                            viewModel.getAllWorld()
                        }
                    )
                }
            )
        }
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0XFF171717)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = it.calculateTopPadding(), bottom = 80.dp)
                    .background(Color(0XFF171717)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                worldData?.data?.let { it ->
                    items(it) { item ->
                        WorldItem(data = item, navController = navController)
                    }
                }
            }
        }
    }
}


@Composable
fun WorldItem(data: Data,navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clickable {
                val id = Uri.encode(data.id.toString())
                val tittle = Uri.encode(data.title)
                val pic = Uri.encode(data.album.coverXl)
                val audio = Uri.encode(data.preview)
                val name = Uri.encode(data.artist.name)
                navController.navigate(Screens.Detail.route + "/$id/$tittle/$pic/$audio/$name")
            },
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = data.album.coverXl,
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = data.title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.W400, maxLines = 1
            )

            Text(
                text = data.artist.name,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Color.White, maxLines = 1
            )

            Text(
                text = "${data.duration}k / steams",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold, maxLines = 1
            )
        }


    }
}
