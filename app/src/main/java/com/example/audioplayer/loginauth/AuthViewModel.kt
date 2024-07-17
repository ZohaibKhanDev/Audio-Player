package com.example.audioplayer.loginauth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel(private val repo: AuthService) : ViewModel() {

    fun loginUser(user: User) = repo.loginUser(user)

    fun signUpUser(user: User) = repo.signUpUser(user)
}
