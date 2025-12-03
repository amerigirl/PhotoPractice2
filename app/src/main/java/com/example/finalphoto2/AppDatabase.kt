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

//Query count
    @Query("SELECT COUNT(*) FROM photo_tags WHERE tag = :tag")
    suspend fun searchByTag(tag: String): Int
}

@Database(entities = [Photo::class, PhotoTag::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}
