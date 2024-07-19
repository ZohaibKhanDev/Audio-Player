package com.example.audioplayer.realtimedatabase

interface RealTimeService {
    suspend fun storeData(message: Message, userId: String): String

    suspend fun getData(userId: String): Message?
}