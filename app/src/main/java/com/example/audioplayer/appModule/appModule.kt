package com.example.audioplayer.appModule

import androidx.room.Room
import com.example.audioplayer.db.FavDataBase
import com.example.audioplayer.loginauth.AuthRepository
import com.example.audioplayer.loginauth.AuthService
import com.example.audioplayer.loginauth.AuthViewModel
import com.example.audioplayer.restapi.MainViewModel
import com.example.audioplayer.restapi.Repository
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            FavDataBase::class.java,
            "demo.db"
        ).allowMainThreadQueries()
            .build()
    }

    single { Repository(get()) }

    single {
        FirebaseAuth.getInstance()
    }

    single<AuthService> {
        AuthRepository(get())
    }
    single {
        AuthViewModel(get())
    }
    viewModel { MainViewModel(get()) }
}


