package com.example.myapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val loadImage = findViewById(R.id.load_image) as Button
        loadImage.setOnClickListener{
            openGallery()

        }
        val takePhoto = findViewById(R.id.take_photo) as Button
        takePhoto.setOnClickListener{
            dispatchTakePictureIntent()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, 228)
        } catch (e: ActivityNotFoundException) {
        }
    }

    private fun openGallery() {
        val openGalleryIntent = Intent(Intent.ACTION_PICK,EXTERNAL_CONTENT_URI)
        try{
            startActivityForResult(openGalleryIntent,2001)
        } catch (e: ActivityNotFoundException){
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 2001 && resultCode == RESULT_OK){
            goToImageFactory()
        }
        if(requestCode == 228 && resultCode == RESULT_OK){
            val bitmap = data?.extras?.get("data") as Bitmap
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "pictureFromCam" ,"")
            goToImageFactory()
        }
    }

    fun goToImageFactory(){
        val factoryIntent = Intent(this,ImageFactory::class.java)
        startActivity(factoryIntent)
    }
}