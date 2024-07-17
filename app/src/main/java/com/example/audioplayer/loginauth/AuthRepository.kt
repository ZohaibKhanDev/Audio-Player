package com.example.audioplayer.loginauth

import com.example.audioplayer.restapi.ResultState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepository(private val auth: FirebaseAuth) : AuthService {
    override fun loginUser(user: User): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        auth.createUserWithEmailAndPassword(user.email.toString(), user.password.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(ResultState.Success("Create User"))
                } else {
                    trySend(ResultState.Error(it.exception!!))
                }
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it))
            }
        awaitClose { close() }
    }

    override fun signUpUser(user: User): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        auth.signInWithEmailAndPassword(user.email.toString(), user.password.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(ResultState.Success("SignUp Successfully"))
                } else {
                    trySend(ResultState.Error(it.exception!!))
                }
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it))
            }
        awaitClose { close() }
    }
}

