package com.example.finalphoto2

import androidx.room.*

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos")
    suspend fun getPhotosForPrinting(): List<Photo>

    @Insert
    suspend fun insertPhoto(photo: Photo): Long

    @Insert
    suspend fun insertTag(tag: PhotoTag)

    @Query("""
        SELECT p.*, pt.tag FROM photos p 
        LEFT JOIN photo_tags pt ON p.id = pt.photoId 
        WHERE pt.tag = :tag
    """)
    suspend fun searchByTag(tag: String): List<PhotoWithTag>
}

@Database(entities = [Photo::class, PhotoTag::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}

data class PhotoWithTag(val photo: Photo?, val tag: String)
