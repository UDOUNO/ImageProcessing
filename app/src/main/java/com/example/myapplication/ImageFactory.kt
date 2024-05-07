package com.example.myapplication

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Objects


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
        goToMain.setOnClickListener {
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
        }
        val intent = intent
        val temp = intent.getStringExtra("imageUri")
        val uri: Uri = Uri.parse(temp)
        val imageDemo: ImageView = findViewById(R.id.image_demo)
        var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        imageDemo.setImageBitmap(bitmap)

        val redFilter = findViewById(R.id.red_filter) as ImageButton
        redFilter.setOnClickListener {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            bitmap = ColorFilters.redColor(bitmap)
            imageDemo.setImageBitmap(bitmap)
        }

        val blueFilter = findViewById(R.id.blue_filter) as ImageButton
        blueFilter.setOnClickListener {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            bitmap = ColorFilters.blueColor(bitmap)
            imageDemo.setImageBitmap(bitmap)
        }

        val greenFilter = findViewById(R.id.green_filter) as ImageButton
        greenFilter.setOnClickListener {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            bitmap = ColorFilters.greenColor(bitmap)
            imageDemo.setImageBitmap(bitmap)
        }

        val grayFilter = findViewById(R.id.gray_filter) as ImageButton
        grayFilter.setOnClickListener {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            bitmap = ColorFilters.grayColor(bitmap)
            imageDemo.setImageBitmap(bitmap)
        }

        val cancelChanges = findViewById(R.id.cancel_changes) as ImageButton
        cancelChanges.setOnClickListener {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            imageDemo.setImageBitmap(bitmap)
        }

        val blackWhiteFilter = findViewById(R.id.black_white_filter) as ImageButton
        blackWhiteFilter.setOnClickListener {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            bitmap = ColorFilters.blackWhiteColor(bitmap)
            imageDemo.setImageBitmap(bitmap)
        }

        val saveImage = findViewById(R.id.save_changes) as ImageButton
        saveImage.setOnClickListener {
            saveBitmap(bitmap)
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
        }
    }

    private fun saveBitmap(bitmap: Bitmap) {
        val images: Uri
        val contentResolver: ContentResolver = contentResolver

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        var contentValues = ContentValues()

        contentValues.put(
            MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis().toString() + ".jpg"
        )
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/*")
        var uri: Uri? = contentResolver.insert(images, contentValues)

        try {
            var outputStream =
                Objects.requireNonNull(uri)?.let { contentResolver.openOutputStream(it) }
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            Objects.requireNonNull(outputStream)
            Toast.makeText(this, "ImageSaved", Toast.LENGTH_LONG).show()
        } catch (_: Exception) {
            Toast.makeText(this, "ImageNotSaved", Toast.LENGTH_LONG).show()
        }
    }

}