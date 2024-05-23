package com.example.myapplication

import android.R.attr.bitmap
import android.R.attr.height
import android.R.attr.width
import android.R.color
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.core.graphics.BitmapCompat
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.createBitmap
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt


object AlgoFilters {
    private fun extrapolate(image: Bitmap, kernelSize: Int): Bitmap {
        var result = createBitmap(
            (image.width + (floor((kernelSize / 2).toDouble()) * 2)).toInt(),
            (image.height + (floor((kernelSize / 2).toDouble()) * 2)).toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(result)
        val paint = Paint()
        paint.setColor(Color.argb(255, 255, 255, 255))
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        var pixels = IntArray(image.width * image.height)
        image.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight())
        result.setPixels(
            pixels,
            0,
            image.width,
            floor((kernelSize / 2).toDouble()).toInt(),
            floor((kernelSize / 2).toDouble()).toInt(),
            image.width,
            image.height
        )
        return result
    }

    private fun gausValue(x: Double): Double {
        var sigma: Double = 3.0
        var mu: Double = 0.0
        return 1.0f / (sqrt(2 * PI) * sigma) * exp(-((x - mu) * (x - mu)) / (2 * sigma * sigma))
    }

    private fun makeGausKernel(maskSize: Int): Array<Double> {
        var weights: Array<Double> = emptyArray()
        var sum: Double = 0.0
        var gausV: Double = 0.0
        var halfMask: Int = maskSize / 2

        for (x in -halfMask..halfMask) {
            for (y in -halfMask..halfMask) {
                gausV = gausValue(sqrt((x * x + y * y).toDouble()))
                sum += gausV
                weights += gausV
            }
        }
        for (i in 0..<maskSize * maskSize) {
            weights[i] /= sum
        }
        return weights
    }

    fun makeSharpnessKernel(koeff: Int): Array<Double> {
        return arrayOf(
            0.0, -1.0 * koeff, 0.0, -1.0 * koeff, 5.0 * koeff, -1.0 * koeff, 0.0, -1.0 * koeff, 0.0
        )
    }

    fun applyKernelToBitmap(image: Bitmap, weights: Array<Double>): Bitmap {
        var result: Bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
        val kernelSize = sqrt(weights.size.toDouble()).toInt()
        var tempIm = extrapolate(image, kernelSize)
        val width = tempIm.width
        val height = tempIm.height
        val border1: Int = floor((kernelSize / 2).toDouble()).toInt()
        val border2: Int = height - floor((kernelSize / 2).toDouble()).toInt()
        val border2B: Int = width - floor((kernelSize / 2).toDouble()).toInt()
        for (y in border1..<border2) {
            for (x in border1..<border2B) {

                var sumColorRed: Double = 0.0
                var sumColorGreen: Double = 0.0
                var sumColorBlue: Double = 0.0
                var sumColorAlpha: Double = 0.0
                for (kY in -border1..border1) {
                    for (kX in -border1..border1) {

                        var maskPos =
                            ((kX + floor((kernelSize / 2).toDouble())) + (kY + floor((kernelSize / 2).toDouble())) * kernelSize).toInt()

                        sumColorRed += (tempIm.getPixel(x - kX, y - kY).red * weights[maskPos])
                        sumColorGreen += (tempIm.getPixel(x - kX, y - kY).green * weights[maskPos])
                        sumColorBlue += (tempIm.getPixel(x - kX, y - kY).blue * weights[maskPos])
                        sumColorAlpha += (tempIm.getPixel(x - kX, y - kY).alpha * weights[maskPos])
                    }
                }
                result.setPixel(
                    x - floor((kernelSize / 2).toDouble()).toInt(),
                    y - floor((kernelSize / 2).toDouble()).toInt(),
                    Color.argb(
                        sumColorAlpha.toInt(),
                        sumColorRed.toInt(),
                        sumColorGreen.toInt(),
                        sumColorBlue.toInt()
                    )
                )
            }
        }

        return result
    }

    fun unSharpMask(image: Bitmap, kernelSize: Int): Bitmap {
        var blurImage = gaussFilter(image, kernelSize)
        var sharpImage = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
        for (i in 0..<image.height) {
            for (j in 0..<image.width) {
                var origPix = image.getPixel(j, i)
                var blurPix = blurImage.getPixel(j, i)
                var pixRed = max(min(origPix.red - blurPix.red + origPix.red, 255), 0)
                var pixGreen = max(min(origPix.green - blurPix.green + origPix.green, 255), 0)
                var pixBlue = max(min(origPix.blue - blurPix.blue + origPix.blue, 255), 0)
                sharpImage.setPixel(j, i, Color.argb(blurPix.alpha, pixRed, pixGreen, pixBlue))
            }
        }
        return sharpImage
    }

    fun gaussFilter(image: Bitmap, kernelSize: Int): Bitmap {
        return applyKernelToBitmap(image, makeGausKernel(kernelSize));
    }

    fun contrast(image: Bitmap, Value: Int): Bitmap {
        var value = (100.0 + Value) / 100.0
        value = value * value
        var pixels = IntArray(image.getWidth() * image.getHeight())
        image.getPixels(
            pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight()
        )
        for (pix in 0..pixels.size - 1) {
            var newR = max(min(((pixels[pix].red / 255.0 - 0.5) * value + 0.5) * 255.0, 255.0), 0.0)
            var newB = max(min(((pixels[pix].blue / 255.0 - 0.5) * value + 0.5) * 255.0, 255.0), 0.0)
            var newG = max(min(((pixels[pix].green / 255.0 - 0.5) * value + 0.5) * 255.0, 255.0), 0.0)
            var newPix = Color.argb(pixels[pix].alpha, newR.toInt(), newG.toInt(), newB.toInt())
            pixels[pix] = newPix
        }
        var result =
            Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888)
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight())
        return result
    }

    fun mozaic(image: Bitmap): Bitmap {
        return image
    }

    fun imageResize(image: Bitmap, koeff: Double): Bitmap {
        if (koeff > 1) {
            return enlargeImage(image, koeff);
        } else {
            return reduceImage(image, koeff);
        }
    }

    fun applyBilinear(image: Bitmap, koeff: Double): Bitmap {
        val result = Bitmap.createBitmap(
            Math.round(image.width * koeff).toInt(),
            Math.round(image.height * koeff).toInt(),
            Bitmap.Config.ARGB_8888
        )

        var alpha: Double
        var red: Double
        var green: Double
        var blue: Double
        for (j in 0..<result.height) {
            for (i in 0..<result.width) {
                val x = (i / koeff)
                val y = (j / koeff)

                val x1 = x.toInt()
                val x2 = minOf(x1 + 1, image.width - 1)

                val y1 = y.toInt()
                val y2 = minOf(y1 + 1, image.height - 1)

                val pix1 = image.getPixel(x1, y1)
                val pix2 = image.getPixel(x2, y1)
                val pix3 = image.getPixel(x1, y2)
                val pix4 = image.getPixel(x2, y2)

                val kx = x - x1
                val ky = y - y1

                alpha =
                    (Color.alpha(pix1) * (1 - kx) + Color.alpha(pix2) * kx) * (1 - ky) + (Color.alpha(
                        pix3
                    ) * (1 - kx) + Color.alpha(pix4) * kx) * (ky)
                red =
                    (Color.red(pix1) * (1 - kx) + Color.red(pix2) * kx) * (1 - ky) + (Color.red(pix3) * (1 - kx) + Color.red(
                        pix4
                    ) * kx) * (ky)
                green =
                    (Color.green(pix1) * (1 - kx) + Color.green(pix2) * kx) * (1 - ky) + (Color.green(
                        pix3
                    ) * (1 - kx) + Color.green(pix4) * kx) * (ky)
                blue =
                    (Color.blue(pix1) * (1 - kx) + Color.blue(pix2) * kx) * (1 - ky) + (Color.blue(
                        pix3
                    ) * (1 - kx) + Color.blue(pix4) * kx) * (ky)

                val newPixel: Int =
                    Color.argb(alpha.toInt(), red.toInt(), green.toInt(), blue.toInt())


                result.setPixel(i, j, newPixel)
            }
        }
        return result
    }

    fun applyReduction(image: Bitmap, koeff: Double): Bitmap {
        val firstMip = image.copy(Bitmap.Config.ARGB_8888, true)
        val c: Double = 1 - ((1 - koeff) / 2)
        val secondMip = applyBilinear(firstMip, c)
        val firstResultBitmap = applyBilinear(firstMip, koeff)
        val secondResultBitmap = applyBilinear(secondMip, (koeff / c))
        val newBitmap = Bitmap.createBitmap(
            Math.round(firstMip.width * koeff).toInt(),
            Math.round(firstMip.height * koeff).toInt(),
            firstMip.config
        )


        var alpha: Int
        var red: Int
        var green: Int
        var blue: Int
        for (j in 0 until newBitmap.height - 1) {
            for (i in 0 until newBitmap.width - 1) {
                if (i < secondResultBitmap.width || j < secondResultBitmap.height) {
                    val pix1 = firstResultBitmap.getPixel(i, j)
                    val pix2 = secondResultBitmap.getPixel(i, j)

                    alpha = (Color.alpha(pix1) + Color.alpha(pix2)) / 2
                    red = (Color.red(pix1) + Color.red(pix2)) / 2
                    green = (Color.green(pix1) + Color.green(pix2)) / 2
                    blue = (Color.blue(pix1) + Color.blue(pix2)) / 2

                    val newPixel: Int = Color.argb(alpha, red, green, blue)


                    newBitmap.setPixel(i, j, newPixel)
                } else {
                    newBitmap.setPixel(i, j, firstResultBitmap.getPixel(i, j))
                }
            }
        }

        return newBitmap
    }

    fun enlargeImage(image: Bitmap, koeff: Double): Bitmap {
        return applyBilinear(image, koeff);
    }

    fun reduceImage(image: Bitmap, koeff: Double): Bitmap {
        return applyReduction(image, koeff)
    }
}