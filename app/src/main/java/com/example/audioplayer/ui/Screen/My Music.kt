package com.example.audioplayer.ui.Screen

import MediaPlayer
import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.audioplayer.AudioItem
import com.example.audioplayer.R
import com.example.audioplayer.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun My_Music(navController: NavController, audioItems: List<AudioItem>) {

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "My Music", color = Color.White)
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0XFF171717)))
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0XFF171717))
                .padding(top = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(audioItems) { audioItem ->
                AudioItem(audioItem,navController)
            }
        }
    }
}


@Composable
fun AudioItem(audioItem: AudioItem,navController: NavController) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth().clickable {
                val id = Uri.encode(audioItem.id.toString())
                val tittle = Uri.encode(audioItem.displayName)
                val audio = Uri.encode(audioItem.uri)
                val name = Uri.encode(audioItem.mimeType)
                navController.navigate(Screens.My_Detail.route + "/$id/$tittle/$audio/$name")
            }
            .height(54.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0XFF6b6b6b)),
        shape = RoundedCornerShape(9.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(
                        Color.White
                    )
                )
            }

            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
                Text(text = audioItem.displayName, color = Color.White, maxLines = 1, minLines = 1)

                Spacer(modifier = Modifier.height(3.dp))

                Text(text = audioItem.mimeType, color = Color.White, maxLines = 1)
            }

            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "", tint = Color.White)
        }
    }
}
