package com.example.audioplayer.album


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    @SerialName("cover")
    val cover: String,
    @SerialName("cover_big")
    val coverBig: String,
    @SerialName("cover_medium")
    val coverMedium: String,
    @SerialName("cover_small")
    val coverSmall: String,
    @SerialName("cover_xl")
    val coverXl: String,
    @SerialName("id")
    val id: Int,
    @SerialName("md5_image")
    val md5Image: String,
    @SerialName("title")
    val title: String,
    @SerialName("tracklist")
    val tracklist: String,
    @SerialName("type")
    val type: String
)