package com.example.finalphoto2

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var our_request_code: Int = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun takePhoto(view: View) {
        //start an intent to capture image

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //start the result

         //check if the task can be performed or not
        if(intent.resolveActivity(packageManager) !==null){
            startActivityForResult(intent, our_request_code)
        }
    }

    //now onactivity

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == our_request_code && resultCode == RESULT_OK){

            //if result ok and equal to request code

            val imageView : ImageView = findViewById((R.id.image))
            //start bitmap
            val bitmap = data?.extras?.get("data") as Bitmap
            //set image bitmap
            imageView.setImageBitmap(bitmap)

        }
    }
}