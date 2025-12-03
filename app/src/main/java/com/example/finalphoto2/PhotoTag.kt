package com.example.finalphoto2

import androidx.room.Entity

@Entity(tableName = "photo_tags", primaryKeys = ["photoId", "tag"])
data class PhotoTag(
    val photoId: Long,
    val tag: String
)
