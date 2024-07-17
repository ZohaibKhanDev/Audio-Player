package com.example.audioplayer.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FavDao {
    @Query("SELECT * FROM FAV")
    fun getAllFav(): List<Fav>

    @Insert
    fun Insert(fav: Fav)

    @Query("DELETE FROM Fav WHERE id = :id")
    fun delete(id:String)


}