package com.example.myapplication

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
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
import androidx.core.view.drawToBitmap
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import java.util.Objects

class ImageFactory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
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
        var imageHistory = Array<String>(5, init = { "" })
        val temp = intent.getStringExtra("imageUri")
        val uri: Uri = Uri.parse(temp)
        val imageDemo: ImageView = findViewById(R.id.image_demo)
        var mainImage = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        imageDemo.setImageBitmap(mainImage)
        var tempImage = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

        val redFilter = findViewById(R.id.red_filter) as ImageButton
        redFilter.setOnClickListener {
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.INVISIBLE
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            tempImage = ColorFilters.redColor(mainImage)
            imageDemo.setImageBitmap(tempImage)
        }

        val blueFilter = findViewById(R.id.blue_filter) as ImageButton
        blueFilter.setOnClickListener {
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.INVISIBLE
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            tempImage = ColorFilters.blueColor(mainImage)
            imageDemo.setImageBitmap(tempImage)
        }

        val greenFilter = findViewById(R.id.green_filter) as ImageButton
        greenFilter.setOnClickListener {
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.INVISIBLE
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            tempImage = ColorFilters.greenColor(mainImage)
            imageDemo.setImageBitmap(tempImage)
        }

        val grayFilter = findViewById(R.id.gray_filter) as ImageButton
        grayFilter.setOnClickListener {
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.INVISIBLE
            tempImage = ColorFilters.grayColor(mainImage)
            imageDemo.setImageBitmap(tempImage)
        }

        val cancelChanges = findViewById(R.id.cancel_changes) as ImageButton
        cancelChanges.setOnClickListener {
            val slider = findViewById(R.id.seekBar) as SeekBar
            slider.visibility = View.INVISIBLE
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.INVISIBLE
            imageDemo.setImageBitmap(mainImage)
        }

        val blackWhiteFilter = findViewById(R.id.black_white_filter) as ImageButton
        blackWhiteFilter.setOnClickListener {
            tempImage = ColorFilters.blackWhiteColor(mainImage)
            imageDemo.setImageBitmap(tempImage)
        }

        val saveImage = findViewById(R.id.save_changes) as ImageButton
        saveImage.setOnClickListener {
            saveBitmap(mainImage)
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
        }

        val contrastFilter = findViewById(R.id.contrast) as ImageButton
        contrastFilter.setOnClickListener {
            val sliderContrast = findViewById(R.id.seekBar) as SeekBar
            sliderContrast.visibility = View.VISIBLE
            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderGaus.visibility = View.INVISIBLE
            sliderContrast.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    val mTextView = findViewById(R.id.slider_val) as TextView
                    mTextView.visibility = View.VISIBLE
                    mTextView.setText(sliderContrast.getProgress().toString());
                    tempImage = AlgoFilters.contrast(mainImage, progress)
                    imageDemo.setImageBitmap(tempImage)

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
            mainImage = anotherAlgos.imageTurnRight(mainImage)
            imageDemo.setImageBitmap(mainImage)
        }

        val imageTurnLeft = findViewById(R.id.image_turn_left) as ImageButton
        imageTurnLeft.setOnClickListener {
            mainImage = anotherAlgos.imageTurnLeft(mainImage)
            imageDemo.setImageBitmap(mainImage)
        }

        val gaussianFilter = findViewById(R.id.gaussian_filter) as ImageButton
        gaussianFilter.setOnClickListener {
            val sliderContrast = findViewById(R.id.seekBar) as SeekBar
            sliderContrast.visibility = View.INVISIBLE
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
                    tempImage = AlgoFilters.gaussFilter(mainImage, sliderGaus.progress)
                    imageDemo.setImageBitmap(tempImage)
                }
            })
        }

        val applyFilter = findViewById(R.id.apply_filter) as ImageButton
        applyFilter.setOnClickListener {
            val slider = findViewById(R.id.seek_bar_gaus) as SeekBar
            slider.visibility = View.INVISIBLE
            val sliderContrast = findViewById(R.id.seekBar) as SeekBar
            sliderContrast.visibility = View.INVISIBLE
            mainImage = Bitmap.createBitmap(tempImage)
            Log.e("", "Filter Applied")
        }

        val sharpnessFilter = findViewById(R.id.sharpness) as ImageButton
        sharpnessFilter.setOnClickListener {
            val slider = findViewById(R.id.seek_bar_gaus) as SeekBar
            slider.visibility = View.INVISIBLE
            val sliderContrast = findViewById(R.id.seekBar) as SeekBar
            sliderContrast.visibility = View.INVISIBLE
            val sliderSharpness = findViewById(R.id.seek_bar_gaus) as SeekBar
            sliderSharpness.visibility = View.VISIBLE
            sliderSharpness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if (sliderSharpness.progress % 2 == 0) {
                        sliderSharpness.progress += 1
                    }
                    tempImage = AlgoFilters.unSharpMask(mainImage, sliderSharpness.progress)
                    imageDemo.setImageBitmap(tempImage)
                }
            })
        }
        val imageReSize = findViewById(R.id.image_resize) as ImageButton
        imageReSize.setOnClickListener {
            val slider = findViewById(R.id.seek_bar_gaus) as SeekBar
            slider.visibility = View.INVISIBLE
            val sliderContrast = findViewById(R.id.seekBar) as SeekBar
            sliderContrast.visibility = View.INVISIBLE
            tempImage = AlgoFilters.imageResize(mainImage, 2.0)
            imageDemo.setImageBitmap(tempImage)
        }

        val faceDetection = findViewById(R.id.find_face) as ImageButton
        faceDetection.setOnClickListener{
            val slider = findViewById(R.id.seek_bar_gaus) as SeekBar
            slider.visibility = View.INVISIBLE
            val sliderContrast = findViewById(R.id.seekBar) as SeekBar
            sliderContrast.visibility = View.INVISIBLE
            tempImage = FaceRecognition.drawRectangles(mainImage);
            imageDemo.setImageBitmap(tempImage);
        }
    }

    private fun saveToTemp() {

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