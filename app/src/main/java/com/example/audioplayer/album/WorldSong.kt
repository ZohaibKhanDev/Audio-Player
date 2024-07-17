package com.example.audioplayer.album


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorldSong(
    @SerialName("data")
    val `data`: List<Data>?=null,
    @SerialName("next")
    val next: String?=null,
    @SerialName("prev")
    val prev: String?=null,
    @SerialName("total")
    val total: Int?=null
)