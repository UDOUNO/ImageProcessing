package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ImageFactory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_image_factory)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val goToMain = findViewById(R.id.go_to_main) as ImageButton
        goToMain.setOnClickListener{
            val mainActivity = Intent(this,MainActivity::class.java)
            startActivity(mainActivity)
        }
        val intent = intent
        val temp = intent.getStringExtra("imageUri")
        val uri:Uri= Uri.parse(temp)
        val imageDemo: ImageView = findViewById(R.id.image_demo)
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        imageDemo.setImageBitmap(bitmap)
    }
}