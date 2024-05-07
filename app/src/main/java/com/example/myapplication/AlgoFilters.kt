package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sqrt

object AlgoFilters {

    fun contrast(image: Bitmap, Value: Int): Bitmap {
        var value = (100.0 + Value) / 100.0
        value = value * value
        var pixels = IntArray(image.getWidth() * image.getHeight())
        image.getPixels(
            pixels, 0, image.getWidth(), 0, 0,
            image.getWidth(), image.getHeight()
        )
        for (pix in 0..pixels.size - 1) {
            var newR = ((pixels[pix].red / 255.0 - 0.5) * value + 0.5) * 255.0
            var newB = ((pixels[pix].blue / 255.0 - 0.5) * value + 0.5) * 255.0
            var newG = ((pixels[pix].green / 255.0 - 0.5) * value + 0.5) * 255.0
            if(newR<0){newR= 0.0 }
            if(newB<0){newB= 0.0 }
            if(newG<0){newG= 0.0 }
            if(newR>255){newR= 255.0 }
            if(newB>255){newB= 255.0 }
            if(newG>255){newG= 255.0 }
            var newPix = Color.argb(pixels[pix].alpha, newR.toInt(), newG.toInt(), newB.toInt())
            pixels[pix] = newPix
        }
        var result = Bitmap.createBitmap(image.getWidth(),image.getHeight(),Bitmap.Config.ARGB_8888)
        result.setPixels(pixels,0,result.getWidth(),0,0,result.getWidth(),result.getHeight())
        return result
    }

    fun mozaic(image: Bitmap): Bitmap {
        return image
    }
}