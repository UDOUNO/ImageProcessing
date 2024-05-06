package com.example.myapplication

import android.R.attr.bitmap
import android.R.attr.height
import android.R.attr.width
import android.R.attr.x
import android.R.attr.y
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red


object ColorFilters {
    fun redColor(image: Bitmap): Bitmap {
        var pixels = IntArray(image.getWidth() * image.getHeight())
        image.getPixels(
            pixels, 0, image.getWidth(), 0, 0,
            image.getWidth(), image.getHeight()
        )
        for (pix in 0..pixels.size-1){
            var newPix = Color.argb(pixels[pix].alpha,pixels[pix].red,0,0)
            pixels[pix] = newPix
        }
        var result = Bitmap.createBitmap(image.getWidth(),image.getHeight(),Bitmap.Config.ARGB_8888)
        result.setPixels(pixels,0,result.getWidth(),0,0,result.getWidth(),result.getHeight())
        return result;
    }

    fun greenColor(image: Bitmap): Bitmap {
        var pixels = IntArray(image.getWidth() * image.getHeight())
        image.getPixels(
            pixels, 0, image.getWidth(), 0, 0,
            image.getWidth(), image.getHeight()
        )
        for (pix in 0..pixels.size-1){
            var newPix = Color.argb(pixels[pix].alpha,0,pixels[pix].green,0)
            pixels[pix] = newPix
        }
        var result = Bitmap.createBitmap(image.getWidth(),image.getHeight(),Bitmap.Config.ARGB_8888)
        result.setPixels(pixels,0,result.getWidth(),0,0,result.getWidth(),result.getHeight())
        return result;
    }

    fun blueColor(image: Bitmap): Bitmap {
        var pixels = IntArray(image.getWidth() * image.getHeight())
        image.getPixels(
            pixels, 0, image.getWidth(), 0, 0,
            image.getWidth(), image.getHeight()
        )
        for (pix in 0..pixels.size-1){
            var newPix = Color.argb(pixels[pix].alpha,0,0,pixels[pix].blue)
            pixels[pix] = newPix
        }
        var result = Bitmap.createBitmap(image.getWidth(),image.getHeight(),Bitmap.Config.ARGB_8888)
        result.setPixels(pixels,0,result.getWidth(),0,0,result.getWidth(),result.getHeight())
        return result;
    }

    fun grayColor(image: Bitmap): Bitmap {
        var pixels = IntArray(image.getWidth() * image.getHeight())
        image.getPixels(
            pixels, 0, image.getWidth(), 0, 0,
            image.getWidth(), image.getHeight()
        )
        for (pix in 0..pixels.size-1){
            var midCol = (pixels[pix].red+pixels[pix].blue+pixels[pix].green)/3
            var newPix = Color.argb(pixels[pix].alpha,midCol,midCol,midCol)
            pixels[pix] = newPix
        }
        var result = Bitmap.createBitmap(image.getWidth(),image.getHeight(),Bitmap.Config.ARGB_8888)
        result.setPixels(pixels,0,result.getWidth(),0,0,result.getWidth(),result.getHeight())
        return result;
    }

    fun blackWhiteColor(image: Bitmap): Bitmap {
        var pixels = IntArray(image.getWidth() * image.getHeight())
        image.getPixels(
            pixels, 0, image.getWidth(), 0, 0,
            image.getWidth(), image.getHeight()
        )
        for (pix in 0..pixels.size-1){
            var midCol = (pixels[pix].red+pixels[pix].blue+pixels[pix].green)/3
            var newPix = Color.argb(pixels[pix].alpha,0,0,0)
            if(midCol>128){
                newPix = Color.argb(pixels[pix].alpha,255,255,255)
            }
            pixels[pix] = newPix
        }
        var result = Bitmap.createBitmap(image.getWidth(),image.getHeight(),Bitmap.Config.ARGB_8888)
        result.setPixels(pixels,0,result.getWidth(),0,0,result.getWidth(),result.getHeight())
        return result;
    }
}