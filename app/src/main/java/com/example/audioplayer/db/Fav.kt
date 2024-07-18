package com.example.audioplayer.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fav(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "tittle") val tittle: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "audioUrl") val audioUrl: String,
    @ColumnInfo(name = "pic") val pic: String
)