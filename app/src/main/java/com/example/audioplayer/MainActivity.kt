package com.example.audioplayer

import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.example.audioplayer.appModule.appModule
import com.example.audioplayer.navigation.MainContent
import com.example.audioplayer.ui.theme.AudioPlayerTheme
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    private var audioItems: List<AudioItem> by mutableStateOf(emptyList())

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            androidLogger()
            modules(appModule)
        }
        setContent {
            AudioPlayerTheme {
                MainContent(audioItems = audioItems)
            }
        }
        requestPermission()
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermission() {
        permissionGaranted(this, android.Manifest.permission.READ_MEDIA_AUDIO) {
            if (it) {
                audioItems = getAudioList(applicationContext)
            } else {
                registerActivityResult.launch(android.Manifest.permission.READ_MEDIA_AUDIO)
            }
        }
    }

    private val registerActivityResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                audioItems = getAudioList(applicationContext)
            }
        }

    fun getAudioList(context: Context): List<AudioItem> {
        val audioList = mutableListOf<AudioItem>()
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
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
            while (cursor.moveToNext()) {
                val audioId = cursor.getLong(idColumn)
                val audioUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioId
                ).toString()
                val displayName = cursor.getString(displayNameColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                audioList.add(AudioItem(audioId, audioUri, displayName, mimeType))
            }
        }

        println("Retrieved audio list: $audioList")
        return audioList
    }




}
inline fun permissionGaranted(context: Context, permission: String, call: (Boolean) -> Unit) {
    if (ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        call.invoke(true)
    } else {
        call.invoke(false)
    }
}
data class AudioItem(
    val id: Long,
    val uri: String,
    val displayName: String,
    val mimeType: String
)
