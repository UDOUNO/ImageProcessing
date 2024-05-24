package com.example.myapplication

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dhaval2404.imagepicker.ImagePicker


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val loadImage = findViewById(R.id.load_image) as Button
        loadImage.setOnClickListener {
            openGallery()
        }
        val takePhoto = findViewById(R.id.take_photo) as Button
        takePhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private var listOfNonGrantedPermissions: MutableList<String> = mutableListOf()

    override fun onStart() {
        super.onStart()
        val info = packageManager.getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
        info.requestedPermissions?.forEach {
            if (checkPermission(
                    it, android.os.Process.myPid(), android.os.Process.myUid()
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                listOfNonGrantedPermissions.add(it)
            }
        }
        makeRequestForNotGrantedPermissions()
    }

    private fun makeRequestForNotGrantedPermissions() {
        if (listOfNonGrantedPermissions.isNotEmpty()) {
            val nextPermission = arrayOf(listOfNonGrantedPermissions.first())
            listOfNonGrantedPermissions.removeAt(0)
            ActivityCompat.requestPermissions(
                this, nextPermission, 1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                return makeRequestForNotGrantedPermissions()
            }
        }
    }
    private fun dispatchTakePictureIntent() {
        try {
            ImagePicker.with(this).cameraOnly().start(111)
        } catch (e: ActivityNotFoundException) {
        }
    }

    private fun openGallery() {
        try {
            ImagePicker.with(this).galleryOnly().start(222)
        } catch (e: ActivityNotFoundException) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            goToImageFactory(uri)
        }
    }

    fun goToImageFactory(uri: Uri) {
        val factoryIntent = Intent(this, ImageFactory::class.java)
        factoryIntent.putExtra("imageUri", uri.toString())
        startActivity(factoryIntent)
    }
}