package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.createBitmap
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File


object FaceRecognition {
    fun detectFace(image: Bitmap,file: File): MatOfRect {
        var toProcessImage: Mat = Mat();
        Utils.bitmapToMat(image, toProcessImage);
        val cascadeClassifier = CascadeClassifier(file.absolutePath);
        val faceDetections = MatOfRect();
        cascadeClassifier.detectMultiScale(toProcessImage, faceDetections,1.1,2,0, Size(50.0,50.0),Size(2000.0,2000.0));
        return faceDetections;
    }

    fun drawRectangles(image: Bitmap,file: File): Bitmap {
        val mat = detectFace(image,file);
        var toProcessImage = Mat();
        var result = createBitmap(image.width,image.height,Bitmap.Config.ARGB_8888);
        Log.e("debug","face detections found ${mat.toArray().count()}")
        Utils.bitmapToMat(image, toProcessImage);
        for (rt in mat.toArray()) {
            Log.e("debug","coords x:${rt.x}, coord y:${rt.y},width:${rt.width},height:${rt.height}")
            Imgproc.rectangle(
                toProcessImage,
                Point(rt.x.toDouble(), rt.y.toDouble()),
                Point((rt.x + rt.width).toDouble(), (rt.y + rt.height).toDouble()),
                Scalar(
                    0.0, 0.0, 255.0
                ),
                5
            );
        }
        Utils.matToBitmap(toProcessImage, result);
        return result;
    }
}