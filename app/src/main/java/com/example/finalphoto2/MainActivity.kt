package com.example.finalphoto2

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize Room database
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "photo-db").build()

        setupCameraLauncher()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupCameraLauncher() {
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as? Bitmap
                bitmap?.let {
                    findViewById<ImageView>(R.id.image).setImageBitmap(it)
                    savePhotoToDatabase(it)
                } ?: Toast.makeText(this, "No photo captured", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // take photo
    fun takePhoto(view: View) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            takePictureLauncher.launch(intent)
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    //print table

    fun myPrintTable(view: View) {
        lifecycleScope.launch {
            val photos = db.photoDao().getPhotosForPrinting()
            Log.d("DATABASE", "=== PHOTOS TABLE ===")
            photos.forEachIndexed { index, photo ->
                Log.d("DATABASE", "Photo $index: ID=${photo.id}, Size=${photo.imageData.size} bytes")
            }
            Log.d("DATABASE", "Total photos: ${photos.size}")
            Toast.makeText(this@MainActivity, "Check Logcat!", Toast.LENGTH_SHORT).show()
        }
    }

    //Save photo
    private fun savePhotoToDatabase(bitmap: Bitmap) {
        lifecycleScope.launch {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageData = baos.toByteArray()

            val photo = Photo(imageData = imageData)
            val photoId = db.photoDao().insertPhoto(photo)

            db.photoDao().insertTag(PhotoTag(photoId, "selfie"))
            db.photoDao().insertTag(PhotoTag(photoId, "2025"))

            Toast.makeText(this@MainActivity, "Photo saved with tags!", Toast.LENGTH_SHORT).show()
        }
    }

    //Search by tag
    fun searchByTag(view: View) {
        val tagInput = findViewById<EditText>(R.id.tagInput).text.toString()
        if (tagInput.isEmpty()) {
            Toast.makeText(this, "Enter a tag to search", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val results = db.photoDao().searchByTag(tagInput)
            Log.d("SEARCH", "Photos with tag '$tagInput': ${results.size}")
            Toast.makeText(this@MainActivity, "Found ${results.size} photos", Toast.LENGTH_SHORT).show()
        }
    }
}
