package com.example.audioplayer.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Fav::class], version = 2, exportSchema = false)
abstract class FavDataBase : RoomDatabase() {
    abstract fun getFav(): FavDao
}