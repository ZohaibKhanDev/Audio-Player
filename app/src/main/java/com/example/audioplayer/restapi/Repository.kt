package com.example.audioplayer.restapi

import com.example.audioplayer.album.WorldSong
import com.example.audioplayer.db.Fav
import com.example.audioplayer.db.FavDataBase

class Repository(private val dataBase: FavDataBase) : ApiClient {
    override suspend fun getAudio(): Audio {
        return AudioApiClient.Audio()
    }

    override suspend fun getWorld(): WorldSong {
        return AudioApiClient.worldSong()
    }

    fun getAllFav(): List<Fav> {
        return dataBase.getFav().getAllFav()
    }

    fun Insert(fav: Fav) {
        dataBase.getFav().Insert(fav)
    }

     fun delete(id: String) {
       return dataBase.getFav().delete(id)
    }
}
