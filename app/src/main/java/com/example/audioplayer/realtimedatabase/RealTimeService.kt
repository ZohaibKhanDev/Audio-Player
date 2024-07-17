package com.example.audioplayer.realtimedatabase

import org.koin.core.annotation.ScopeId

interface RealTimeService {
    suspend fun storeData(message: Message, userId: String): String

    suspend fun getData(userId: String): Message?
}