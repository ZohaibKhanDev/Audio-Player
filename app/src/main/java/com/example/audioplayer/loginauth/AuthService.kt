package com.example.audioplayer.loginauth

import com.example.audioplayer.restapi.ResultState
import kotlinx.coroutines.flow.Flow

interface AuthService {
    fun loginUser(user: User):Flow<ResultState<String>>

    fun signUpUser(user: User):Flow<ResultState<String>>
}