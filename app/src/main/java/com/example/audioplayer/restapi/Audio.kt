package com.example.audioplayer.restapi


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Audio(
    @SerialName("data")
    val `data`: List<Data>?=null,
    @SerialName("next")
    val next: String?=null,
    @SerialName("total")
    val total: Int?=null
)