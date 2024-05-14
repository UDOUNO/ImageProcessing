package com.example.myapplication

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier


object FaceRecognition {
    fun detectFace(image: Bitmap): MatOfRect {
        var toProcessImage: Mat = Mat();
        Utils.bitmapToMat(image, toProcessImage);
        val cascadeClassifier: CascadeClassifier = CascadeClassifier("lbpcascade_frontalface.xml");
        val faceDetections = MatOfRect()
        cascadeClassifier.detectMultiScale(toProcessImage, faceDetections)
        return faceDetections;
    }

    fun drawRectangles(image: Bitmap): Bitmap {
        val mat = detectFace(image);
        var toProcessImage: Mat = Mat();
        var result = image;
        Utils.bitmapToMat(image, toProcessImage);
        for (rt in mat.toArray()) {
            Imgproc.rectangle(
                toProcessImage,
                Point(rt.x.toDouble(), rt.y.toDouble()),
                Point((rt.x + rt.width).toDouble(), (rt.y + rt.height).toDouble()),
                Scalar(
                    0.0, 0.0, 255.0
                ),
                3
            );
        }
        Utils.matToBitmap(toProcessImage, result);
        return result;
    }
}