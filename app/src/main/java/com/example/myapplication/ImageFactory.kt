package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

private fun saveBitmap(finalBitmap: Bitmap) {
    val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
    val myDir = File("$root")
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val fname = "Shutta_$timeStamp.jpg"
    val file = File(myDir, fname)
    if (file.exists()) file.delete()
    try {
        val out = FileOutputStream(file)
        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
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
        var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        imageDemo.setImageBitmap(bitmap)
        var mainImage =  MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        var temp2Image =  MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

        val redFilter = findViewById(R.id.red_filter) as ImageButton
        redFilter.setOnClickListener{
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            bitmap = ColorFilters.redColor(mainImage)
            imageDemo.setImageBitmap(bitmap)
        }

        val blueFilter = findViewById(R.id.blue_filter) as ImageButton
        blueFilter.setOnClickListener{
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            bitmap = ColorFilters.blueColor(mainImage)
            imageDemo.setImageBitmap(bitmap)
        }

        val greenFilter = findViewById(R.id.green_filter) as ImageButton
        greenFilter.setOnClickListener{
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            bitmap = ColorFilters.greenColor(mainImage)
            imageDemo.setImageBitmap(bitmap)
        }

        val grayFilter = findViewById(R.id.gray_filter) as ImageButton
        grayFilter.setOnClickListener{
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            bitmap = ColorFilters.grayColor(mainImage)
            imageDemo.setImageBitmap(bitmap)
        }

        val cancelChanges = findViewById(R.id.cancel_changes) as ImageButton
        cancelChanges.setOnClickListener{
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            imageDemo.setImageBitmap(temp2Image)
        }

        val blackWhiteFilter = findViewById(R.id.black_white_filter) as ImageButton
        blackWhiteFilter.setOnClickListener{
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            bitmap = ColorFilters.blackWhiteColor(mainImage)
            imageDemo.setImageBitmap(bitmap)
        }

        val saveImage = findViewById(R.id.save_changes) as ImageButton
        saveImage.setOnClickListener {
            saveBitmap(bitmap)
            val mainActivity = Intent(this,MainActivity::class.java)
            startActivity(mainActivity)
        }

        val contrastFilter = findViewById(R.id.contrast) as ImageButton
        contrastFilter.setOnClickListener{
            var tempImage = bitmap
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.VISIBLE
            slider.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val mTextView = findViewById(R.id.slider_val) as TextView
                    mTextView.visibility = View.VISIBLE
                    mTextView.setText(slider.getProgress().toString());
                    bitmap = AlgoFilters.contrast(tempImage,progress)
                    mainImage= AlgoFilters.contrast(temp2Image,progress)
                    imageDemo.setImageBitmap(bitmap)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val mTextView = findViewById(R.id.slider_val) as TextView
                    mTextView.visibility = View.INVISIBLE
                }
            })
        }
    }
}