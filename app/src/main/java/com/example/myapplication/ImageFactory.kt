package com.example.myapplication

import Retouch
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import org.opencv.android.OpenCVLoader
import java.io.File
import java.io.FileOutputStream
import java.util.Objects

class ImageFactory : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        OpenCVLoader.initLocal()
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
        var mainImage = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        imageDemo.setImageBitmap(mainImage)
        var tempImage = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

        val inputStream = resources.openRawResource(R.raw.lbpcascade_frontalface)
        val weightsDir = applicationContext.getDir("cascade", Context.MODE_PRIVATE)
        val weightsFile = File(weightsDir, "haarcascade_frontalface_alt_tree.xml")
        val outputStream = FileOutputStream(weightsFile)
        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        inputStream.close()
        outputStream.close()

        lifecycleScope.async { prepare(mainImage) }

        val redFilter = findViewById(R.id.red_filter) as ImageButton
        redFilter.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_contrast, null)
            val dismissChange = view.findViewById(R.id.cancel_changes) as ImageButton
            val applyChange = view.findViewById(R.id.apply_filter) as ImageButton

            dismissChange.setOnClickListener {
                dialog.dismiss()
                imageDemo.setImageBitmap(mainImage)
            }
            applyChange.setOnClickListener {
                dialog.dismiss()
                mainImage = Bitmap.createBitmap(tempImage)
                Log.e("", "Filter Applied")
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
            lifecycleScope.async {
                tempImage = ColorFilters.redColor(mainImage)
                imageDemo.setImageBitmap(tempImage)
            }
        }

        val blueFilter = findViewById(R.id.blue_filter) as ImageButton
        blueFilter.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_contrast, null)
            val dismissChange = view.findViewById(R.id.cancel_changes) as ImageButton
            val applyChange = view.findViewById(R.id.apply_filter) as ImageButton

            dismissChange.setOnClickListener {
                dialog.dismiss()
                imageDemo.setImageBitmap(mainImage)
            }
            applyChange.setOnClickListener {
                dialog.dismiss()
                mainImage = Bitmap.createBitmap(tempImage)
                Log.e("", "Filter Applied")
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
            lifecycleScope.async {
                tempImage = ColorFilters.blueColor(mainImage)
                imageDemo.setImageBitmap(tempImage)
            }
        }

        val greenFilter = findViewById(R.id.green_filter) as ImageButton
        greenFilter.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_contrast, null)
            val dismissChange = view.findViewById(R.id.cancel_changes) as ImageButton
            val applyChange = view.findViewById(R.id.apply_filter) as ImageButton

            dismissChange.setOnClickListener {
                dialog.dismiss()
                imageDemo.setImageBitmap(mainImage)
            }
            applyChange.setOnClickListener {
                dialog.dismiss()
                mainImage = Bitmap.createBitmap(tempImage)
                Log.e("", "Filter Applied")
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
            lifecycleScope.async {
                tempImage = ColorFilters.greenColor(mainImage)
                imageDemo.setImageBitmap(tempImage)
            }
        }

        val grayFilter = findViewById(R.id.gray_filter) as ImageButton
        grayFilter.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_contrast, null)
            val dismissChange = view.findViewById(R.id.cancel_changes) as ImageButton
            val applyChange = view.findViewById(R.id.apply_filter) as ImageButton

            dismissChange.setOnClickListener {
                dialog.dismiss()
                imageDemo.setImageBitmap(mainImage)
            }
            applyChange.setOnClickListener {
                dialog.dismiss()
                mainImage = Bitmap.createBitmap(tempImage)
                Log.e("", "Filter Applied")
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
            lifecycleScope.async {
                tempImage = ColorFilters.grayColor(mainImage)
                imageDemo.setImageBitmap(tempImage)
            }
        }

//        val cancelChanges = findViewById(R.id.cancel_changes) as ImageButton
//        cancelChanges.setOnClickListener {
//            val slider = findViewById(R.id.seekBar) as SeekBar
//            slider.visibility = View.INVISIBLE
//            val sliderGaus = findViewById(R.id.seek_bar_gaus) as SeekBar
//            sliderGaus.visibility = View.INVISIBLE
//            imageDemo.setImageBitmap(mainImage)
//        }

        val blackWhiteFilter = findViewById(R.id.black_white_filter) as ImageButton
        blackWhiteFilter.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_contrast, null)
            val dismissChange = view.findViewById(R.id.cancel_changes) as ImageButton
            val applyChange = view.findViewById(R.id.apply_filter) as ImageButton

            dismissChange.setOnClickListener {
                dialog.dismiss()
                imageDemo.setImageBitmap(mainImage)
            }
            applyChange.setOnClickListener {
                dialog.dismiss()
                mainImage = Bitmap.createBitmap(tempImage)
                Log.e("", "Filter Applied")
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
            lifecycleScope.async {
                tempImage = ColorFilters.blackWhiteColor(mainImage)
                imageDemo.setImageBitmap(tempImage)
            }
        }

        val saveImage = findViewById(R.id.save_changes) as ImageButton
        saveImage.setOnClickListener {
            saveBitmap(mainImage)
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
        }

        val contrastFilter = findViewById(R.id.contrast) as ImageButton
        contrastFilter.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_contrast, null)
            val dismissChange = view.findViewById(R.id.cancel_changes) as ImageButton
            val applyChange = view.findViewById(R.id.apply_filter) as ImageButton
            val sliderContrast = view.findViewById(R.id.contrast) as SeekBar

            dismissChange.setOnClickListener {
                dialog.dismiss()
                imageDemo.setImageBitmap(mainImage)
            }
            applyChange.setOnClickListener {
                dialog.dismiss()
                mainImage = Bitmap.createBitmap(tempImage)
                Log.e("", "Filter Applied")
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
            sliderContrast.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    val mTextView = findViewById(R.id.slider_val) as TextView
                    mTextView.visibility = View.VISIBLE
                    mTextView.setText(sliderContrast.getProgress().toString())
                    lifecycleScope.async {
                        tempImage = AlgoFilters.contrast(mainImage, progress)
                        imageDemo.setImageBitmap(tempImage)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val mTextView = findViewById(R.id.slider_val) as TextView
                    mTextView.visibility = View.INVISIBLE
                }
            })
        }

        val gaussianFilter = findViewById(R.id.gaussian_filter) as ImageButton
        gaussianFilter.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_gauss, null)
            val dismissChange = view.findViewById(R.id.cancel_changes) as ImageButton
            val applyChange = view.findViewById(R.id.apply_filter) as ImageButton
            val sliderGaus = view.findViewById(R.id.gaus) as SeekBar

            dismissChange.setOnClickListener {
                dialog.dismiss()
                imageDemo.setImageBitmap(mainImage)
            }
            applyChange.setOnClickListener {
                dialog.dismiss()
                mainImage = Bitmap.createBitmap(tempImage)
                Log.e("", "Filter Applied")
            }

            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
            sliderGaus.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    val mTextView = findViewById(R.id.slider_val) as TextView
                    mTextView.visibility = View.VISIBLE
                    mTextView.setText(sliderGaus.getProgress().toString())
                    lifecycleScope.async {
                        tempImage = AlgoFilters.gaussFilter(mainImage, progress)
                        imageDemo.setImageBitmap(tempImage)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if (sliderGaus.progress % 2 == 0) {
                        sliderGaus.progress += 1
                    }
                    val mTextView = findViewById(R.id.slider_val) as TextView
                    mTextView.visibility = View.INVISIBLE
                }
            })
        }

        val sharpnessFilter = findViewById(R.id.sharpness) as ImageButton
        sharpnessFilter.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_sharp, null)
            val dismissChange = view.findViewById(R.id.cancel_changes) as ImageButton
            val applyChange = view.findViewById(R.id.apply_filter) as ImageButton
            val sliderGaus = view.findViewById(R.id.gaus) as SeekBar

            dismissChange.setOnClickListener {
                dialog.dismiss()
                imageDemo.setImageBitmap(mainImage)
            }
            applyChange.setOnClickListener {
                dialog.dismiss()
                mainImage = Bitmap.createBitmap(tempImage)
                Log.e("", "Filter Applied")
            }

            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
            sliderGaus.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    val mTextView = findViewById(R.id.slider_val) as TextView
                    mTextView.visibility = View.VISIBLE
                    mTextView.setText(sliderGaus.getProgress().toString())
                    lifecycleScope.async {
                        tempImage = AlgoFilters.unSharpMask(mainImage, progress)
                        imageDemo.setImageBitmap(tempImage)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if (sliderGaus.progress % 2 == 0) {
                        sliderGaus.progress += 1
                    }
                    val mTextView = findViewById(R.id.slider_val) as TextView
                    mTextView.visibility = View.INVISIBLE
                }
            })
        }

        val imageTurnRight = findViewById(R.id.image_turn_right) as ImageButton
        imageTurnRight.setOnClickListener {
            lifecycleScope.async {
                mainImage = anotherAlgos.imageTurnRight(mainImage)
                imageDemo.setImageBitmap(mainImage)
            }
        }

        val imageTurnLeft = findViewById(R.id.image_turn_left) as ImageButton
        imageTurnLeft.setOnClickListener {
            lifecycleScope.async {
                mainImage = anotherAlgos.imageTurnLeft(mainImage)
                imageDemo.setImageBitmap(mainImage)
            }
        }

        val resize = findViewById(R.id.image_resize) as ImageButton
        resize.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_resize, null)
            val dismissChange = view.findViewById(R.id.cancel_changes) as ImageButton
            val applyChange = view.findViewById(R.id.apply_filter) as ImageButton
            val sliderRes = view.findViewById(R.id.resize) as SeekBar

            dismissChange.setOnClickListener {
                dialog.dismiss()
                imageDemo.setImageBitmap(mainImage)
            }
            applyChange.setOnClickListener {
                dialog.dismiss()
                mainImage = Bitmap.createBitmap(tempImage)
                Log.e("", "Filter Applied")
            }

            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
            sliderRes.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    val mTextView = findViewById(R.id.slider_val) as TextView
                    mTextView.visibility = View.VISIBLE
                    mTextView.setText(sliderRes.getProgress().toString())
                    var progres:Int
                    if (sliderRes.progress < 5) {
                        progres = 5
                    } else {
                        progres = sliderRes.progress
                    }
                    lifecycleScope.async {
                        tempImage = AlgoFilters.imageResize(mainImage, progres.toDouble() / 10)
                        imageDemo.setImageBitmap(tempImage)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val mTextView = findViewById(R.id.slider_val) as TextView
                    mTextView.visibility = View.INVISIBLE

                }
            })
        }

        val faceDetection = findViewById(R.id.find_face) as ImageButton
        faceDetection.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_opencv, null)
            val dismissChange = view.findViewById(R.id.cancel_changes) as ImageButton
            val applyChange = view.findViewById(R.id.apply_filter) as ImageButton
            val gaus = view.findViewById(R.id.button_gauss) as ImageButton
            val cont = view.findViewById(R.id.button_contrast) as ImageButton
            val unsharp = view.findViewById(R.id.button_unsharp) as ImageButton

            gaus.setOnClickListener {
                lifecycleScope.async {
                    tempImage = FaceRecognition.faceBlur(mainImage, weightsFile, 3)
                    imageDemo.setImageBitmap(tempImage)
                }
            }
            cont.setOnClickListener{
                lifecycleScope.async {
                    tempImage = FaceRecognition.faceContrast(mainImage,weightsFile,50)
                    imageDemo.setImageBitmap(tempImage)
                }
            }
            unsharp.setOnClickListener {
                lifecycleScope.async {
                    tempImage = AlgoFilters.unSharpMask(mainImage,3)
                    imageDemo.setImageBitmap(tempImage)
                }
            }

            dismissChange.setOnClickListener {
                dialog.dismiss()
                imageDemo.setImageBitmap(mainImage)
            }
            applyChange.setOnClickListener {
                dialog.dismiss()
                mainImage = Bitmap.createBitmap(tempImage)
                Log.e("", "Filter Applied")
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
            lifecycleScope.async {
                tempImage = FaceRecognition.drawRectangles(mainImage, weightsFile)
                imageDemo.setImageBitmap(tempImage)
            }
        }
    }

    private suspend fun prepare(image: Bitmap) {
        val gauss = findViewById(R.id.gaussian_filter) as ImageButton
        val unsharpMask = findViewById(R.id.sharpness) as ImageButton
        val con = findViewById(R.id.contrast) as ImageButton
        val bwf = findViewById(R.id.black_white_filter) as ImageButton
        val gray = findViewById(R.id.gray_filter) as ImageButton
        val green = findViewById(R.id.green_filter) as ImageButton
        val blue = findViewById(R.id.blue_filter) as ImageButton
        val red = findViewById(R.id.red_filter) as ImageButton
        gauss.setImageBitmap(AlgoFilters.gaussFilter(image, 3))
        unsharpMask.setImageBitmap(AlgoFilters.unSharpMask(image, 3))
        con.setImageBitmap(AlgoFilters.contrast(image, 100))
        bwf.setImageBitmap(ColorFilters.blackWhiteColor(image))
        gray.setImageBitmap(ColorFilters.grayColor(image))
        green.setImageBitmap(ColorFilters.greenColor(image))
        blue.setImageBitmap(ColorFilters.blueColor(image))
        red.setImageBitmap(ColorFilters.redColor(image))
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.cancel("exit of activity")
    }

    private fun saveBitmap(bitmap: Bitmap) {
        val images: Uri
        val contentResolver: ContentResolver = contentResolver

        images = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val contentValues = ContentValues()

        contentValues.put(
            MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis().toString() + ".jpg"
        )
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/*")
        val uri: Uri? = contentResolver.insert(images, contentValues)

        try {
            val outputStream =
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