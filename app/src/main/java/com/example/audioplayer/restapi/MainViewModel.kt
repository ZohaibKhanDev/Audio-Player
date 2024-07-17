package com.example.audioplayer.restapi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.audioplayer.album.WorldSong
import com.example.audioplayer.db.Fav
import com.example.audioplayer.loginauth.AuthRepository
import com.example.audioplayer.loginauth.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _allAudio = MutableStateFlow<ResultState<Audio>>(ResultState.Loading)
    val allAudio: StateFlow<ResultState<Audio>> = _allAudio.asStateFlow()

    private val _allWorld = MutableStateFlow<ResultState<WorldSong>>(ResultState.Loading)
    val allWorld: StateFlow<ResultState<WorldSong>> = _allWorld.asStateFlow()

    private val _allFav = MutableStateFlow<ResultState<List<Fav>>>(ResultState.Loading)
    val allFav: StateFlow<ResultState<List<Fav>>> = _allFav.asStateFlow()

    private val _allInsert = MutableStateFlow<ResultState<Unit>>(ResultState.Loading)
    val allInsert: StateFlow<ResultState<Unit>> = _allInsert.asStateFlow()

    private var _allDelete = MutableStateFlow<ResultState<Unit>>(ResultState.Loading)
    val allDelete:StateFlow<ResultState<Unit>> = _allDelete.asStateFlow()



    fun getAllFav() {
        viewModelScope.launch {
            _allFav.value = ResultState.Loading
            try {
                val response = repository.getAllFav()
                _allFav.value = ResultState.Success(response)
            } catch (e: Exception) {
                _allFav.value = ResultState.Error(e)
            }
        }
    }

    fun Insert(fav: Fav) {
        viewModelScope.launch {
            _allInsert.value = ResultState.Loading
            try {
                repository.Insert(fav)
                _allInsert.value = ResultState.Success(Unit)
            } catch (e: Exception) {
                _allInsert.value = ResultState.Error(e)
            }
        }
    }


    fun delete(id: String) {
        viewModelScope.launch {
            _allDelete.value=ResultState.Loading
            try {
                val response = repository.delete(id)
                _allDelete.value = ResultState.Success(response)
            } catch (e: Exception) {
                _allDelete.value = ResultState.Error(e)
            }
        }
    }

    fun getAllWorld() {
        viewModelScope.launch {
            _allWorld.value = ResultState.Loading
            try {
                val response = repository.getWorld()
                _allWorld.value = ResultState.Success(response)
            } catch (e: Exception) {
                _allWorld.value = ResultState.Error(e)
            }
        }
    }

    fun getAllAudio() {
        viewModelScope.launch {
            _allAudio.value = ResultState.Loading
            try {
                val response = repository.getAudio()
                _allAudio.value = ResultState.Success(response)
            } catch (e: Exception) {
                _allAudio.value = ResultState.Error(e)
            }
        }
    }
}

