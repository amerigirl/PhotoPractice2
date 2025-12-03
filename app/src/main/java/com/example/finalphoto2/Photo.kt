package com.example.finalphoto2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imageData: ByteArray,
    val timestamp: Long = System.currentTimeMillis()
)
