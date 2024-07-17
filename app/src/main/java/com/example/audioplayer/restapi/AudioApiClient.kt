package com.example.audioplayer.restapi

import com.example.audioplayer.album.WorldSong
import com.example.audioplayer.restapi.Constant.TIME_OUT
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object AudioApiClient {
    @OptIn(ExperimentalSerializationApi::class)
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }

            }
        }

        install(HttpTimeout) {
            connectTimeoutMillis = TIME_OUT
            socketTimeoutMillis = TIME_OUT
            requestTimeoutMillis = TIME_OUT
        }
        defaultRequest {
            headers {
                append("x-rapidapi-key", "0e3e36a41dmsh01f5d1b030cc6cfp103c0ejsn9c29801473d0")
                append("x-rapidapi-host", "deezerdevs-deezer.p.rapidapi.com")
            }
        }
    }

    suspend fun Audio(): Audio {
        return client.get("https://deezerdevs-deezer.p.rapidapi.com/search?q=eminem").body()
    }

    suspend fun worldSong():WorldSong{
        return client.get("https://api.deezer.com/search?q=eminem&redirect_uri=http%253A%252F%252Fguardian.mashape.com%252Fcallback&index=25").body()
    }
}