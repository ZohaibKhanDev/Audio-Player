package com.example.audioplayer.ui.Screen

import MediaPlayer
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.audioplayer.restapi.MainViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavDetail(
    navController: NavController,
    id: String,
    title: String?,
    pic: String?,
    audio: String?,
    name: String?
) {
    val scroll = rememberScrollState()
    var play by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel: MainViewModel = koinInject()
    val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
    var fav by remember {
        mutableStateOf(false)
    }
    val volumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val maxVolumeLevel = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val volumePercent = (volumeLevel.toFloat() / maxVolumeLevel * 100).toInt()

    val currentVolume = remember { mutableStateOf(volumePercent) }


    Column(
        modifier = Modifier
            .background(Color(0XFF171717))
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(top = 50.dp, bottom = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.clickable { navController.popBackStack() }
            )

            Text(
                text = "$title",
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                minLines = 1, modifier = Modifier.width(111.dp)
            )


            if (fav) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.White,
                    modifier = Modifier.clickable {
                        fav = !fav
                        viewModel.getAllFav()
                    }
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favorite",
                    tint = Color.Red, modifier = Modifier.clickable {
                        fav = !fav
                        viewModel.delete(id)
                        navController.navigateUp()

                    }
                )
            }


        }

        Column(
            modifier = Modifier
                .width(350.dp)
                .padding(20.dp)
                .height(450.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = pic,
                contentDescription = "Audio Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(11.dp))
            )
        }

        Text(text = name ?: "", color = Color.LightGray, fontWeight = FontWeight.W400)

        Row(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .background(Color.DarkGray),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MediaPlayer(
                modifier = Modifier.fillMaxSize(),
                url = audio.toString(),
                startTime = Color.White,
                endTime = Color.White,
                volumeIconColor = Color.White,
                playIconColor = Color.White,
                sliderTrackColor = Color.Red,
                sliderIndicatorColor = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .padding(bottom = 70.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.DarkGray)
                .width(330.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Download",
                tint = Color.White,
                modifier = Modifier.clickable {
                    val downloadManager =
                        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val uri = Uri.parse(audio)
                    val request = DownloadManager.Request(uri).setTitle(audio)
                        .setDescription("This is description")
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                        .setAllowedOverRoaming(true)
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS, "audio.mp3"
                        )

                    downloadManager.enqueue(request)
                }
            )
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.White,
                modifier = Modifier.clickable {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "$audio")
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            )

            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = "",
                tint = if (play) Color.Red else Color.White
            )
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Volume Up",
                tint = Color.White,
                modifier = Modifier.clickable {
                    audioManager.adjustVolume(
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND
                    )
                    val volumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                    val maxVolumeLevel = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                    val volumePercent = (volumeLevel.toFloat() / maxVolumeLevel * 100).toInt()
                    currentVolume.value = volumePercent
                }
            )

            Icon(
                imageVector = Icons.Default.VolumeDown,
                contentDescription = "Volume Down",
                tint = Color.White,
                modifier = Modifier.clickable {
                    audioManager.adjustVolume(
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND
                    )
                    val volumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                    val maxVolumeLevel = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                    val volumePercent = (volumeLevel.toFloat() / maxVolumeLevel * 100).toInt()
                    currentVolume.value = volumePercent
                }
            )
        }
    }
}

