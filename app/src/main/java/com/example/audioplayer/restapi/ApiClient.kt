package com.example.audioplayer.restapi

import com.example.audioplayer.album.WorldSong

interface ApiClient {
    suspend fun getAudio():Audio
    suspend fun getWorld():WorldSong
}