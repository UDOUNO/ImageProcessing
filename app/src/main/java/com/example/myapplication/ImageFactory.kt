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
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
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
        var mainImage = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        var temp2Image = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

        val redFilter = findViewById(R.id.red_filter) as ImageButton
        redFilter.setOnClickListener {
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.INVISIBLE
            val sliderSharpness = findViewById(R.id.seek_bar_sharpness) as SeekBar
            sliderSharpness.visibility = View.INVISIBLE
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            bitmap = ColorFilters.redColor(mainImage)
            imageDemo.setImageBitmap(bitmap)
        }

        val blueFilter = findViewById(R.id.blue_filter) as ImageButton
        blueFilter.setOnClickListener {
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.INVISIBLE
            val sliderSharpness = findViewById(R.id.seek_bar_sharpness) as SeekBar
            sliderSharpness.visibility = View.INVISIBLE
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            bitmap = ColorFilters.blueColor(mainImage)
            imageDemo.setImageBitmap(bitmap)
        }

        val greenFilter = findViewById(R.id.green_filter) as ImageButton
        greenFilter.setOnClickListener {
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.INVISIBLE
            val sliderSharpness = findViewById(R.id.seek_bar_sharpness) as SeekBar
            sliderSharpness.visibility = View.INVISIBLE
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            bitmap = ColorFilters.greenColor(mainImage)
            imageDemo.setImageBitmap(bitmap)
        }

        val grayFilter = findViewById(R.id.gray_filter) as ImageButton
        grayFilter.setOnClickListener {
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.INVISIBLE
            val sliderSharpness = findViewById(R.id.seek_bar_sharpness) as SeekBar
            sliderSharpness.visibility = View.INVISIBLE
            bitmap = ColorFilters.grayColor(mainImage)
            imageDemo.setImageBitmap(bitmap)
        }

        val cancelChanges = findViewById(R.id.cancel_changes) as ImageButton
        cancelChanges.setOnClickListener {
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.INVISIBLE
            val sliderSharpness = findViewById(R.id.seek_bar_sharpness) as SeekBar
            sliderSharpness.visibility = View.INVISIBLE
            imageDemo.setImageBitmap(temp2Image)
            bitmap = temp2Image
        }

        val blackWhiteFilter = findViewById(R.id.black_white_filter) as ImageButton
        blackWhiteFilter.setOnClickListener {
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.INVISIBLE
            val sliderSharpness = findViewById(R.id.seek_bar_sharpness) as SeekBar
            sliderSharpness.visibility = View.INVISIBLE
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            bitmap = ColorFilters.blackWhiteColor(mainImage)
            imageDemo.setImageBitmap(bitmap)
        }

        val saveImage = findViewById(R.id.save_changes) as ImageButton
        saveImage.setOnClickListener {
            saveBitmap(bitmap)
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
        }

        val contrastFilter = findViewById(R.id.contrast) as ImageButton
        contrastFilter.setOnClickListener {
            var tempImage = bitmap
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.VISIBLE
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.INVISIBLE
            val sliderSharpness = findViewById(R.id.seek_bar_sharpness) as SeekBar
            sliderSharpness.visibility = View.INVISIBLE
            slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    val mTextView = findViewById(R.id.slider_val) as TextView
                    mTextView.visibility = View.VISIBLE
                    mTextView.setText(slider.getProgress().toString());
                    bitmap = AlgoFilters.contrast(tempImage, progress)
                    mainImage = AlgoFilters.contrast(temp2Image, progress)
                    imageDemo.setImageBitmap(bitmap)

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val mTextView = findViewById(R.id.slider_val) as TextView
                    mTextView.visibility = View.INVISIBLE
                }
            })
        }

        val imageTurnRight = findViewById(R.id.image_turn_right) as ImageButton
        imageTurnRight.setOnClickListener {
            bitmap = anotherAlgos.imageTurnRight(bitmap)
            imageDemo.setImageBitmap(bitmap)
        }

        val imageTurnLeft = findViewById(R.id.image_turn_left) as ImageButton
        imageTurnLeft.setOnClickListener {
            bitmap = anotherAlgos.imageTurnLeft(bitmap)
            imageDemo.setImageBitmap(bitmap)
        }

        val gaussianFilter = findViewById(R.id.gaussian_filter) as ImageButton
        gaussianFilter.setOnClickListener {
            val slider = findViewById(R.id.seek_bar_sharpness) as SeekBar
            slider.visibility = View.INVISIBLE
            val sliderContrast = findViewById(R.id.seekBar) as SeekBar
            sliderContrast.visibility = View.INVISIBLE
            var tempImage = bitmap
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.VISIBLE
            sliderGaus.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if (sliderGaus.progress % 2 == 0) {
                        sliderGaus.progress += 1
                    }
                    bitmap = AlgoFilters.gaussFilter(tempImage, sliderGaus.progress)
                    imageDemo.setImageBitmap(bitmap)
                }
            })
        }

        val sharpnessFilter = findViewById(R.id.sharpness) as ImageButton
        sharpnessFilter.setOnClickListener {
            val slider = findViewById(R.id.seek_bar_gaus) as SeekBar
            slider.visibility = View.INVISIBLE
            val sliderContrast = findViewById(R.id.seekBar) as SeekBar
            sliderContrast.visibility = View.INVISIBLE
            var tempImage = bitmap
            val sliderSharpness = findViewById(R.id.seek_bar_sharpness) as SeekBar
            sliderSharpness.visibility = View.VISIBLE
            sliderSharpness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                    bitmap = AlgoFilters.gaussianFilter(tempImage, sliderSharpness.progress, 3, 0)
                    imageDemo.setImageBitmap(bitmap)
                }
            })
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