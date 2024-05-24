package com.example.myapplication

import android.graphics.Bitmap

object anotherAlgos {
    fun imageTurnRight(image: Bitmap):Bitmap{
        var result = Bitmap.createBitmap(image.height,image.width,Bitmap.Config.ARGB_8888)
        for(j in 0..image.height-1){
            for(i in 0..image.width-1){
                result.setPixel(image.height-1-j,i,image.getPixel(i,j))
            }
        }
        return result
    }

    fun imageTurnLeft(image: Bitmap):Bitmap{
        var result = Bitmap.createBitmap(image.height,image.width,Bitmap.Config.ARGB_8888)
        for(j in 0..image.height-1){
            for(i in 0..image.width-1){
                result.setPixel(j,image.width-1-i,image.getPixel(i,j))
            }
        }
        return result
    }
}