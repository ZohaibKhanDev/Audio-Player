package com.example.audioplayer.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fav(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo("tittle")
    val tittle: String,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("audioUrl")
    val audioUrl: String,
    @ColumnInfo("pic")
    val pic:String
)
