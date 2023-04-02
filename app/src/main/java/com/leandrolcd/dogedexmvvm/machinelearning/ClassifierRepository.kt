package com.leandrolcd.dogedexmvvm.machinelearning

import androidx.camera.core.ImageProxy
import com.leandrolcd.dogedexmvvm.api.models.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClassifierRepository(private val classifier:Classifier) {

    suspend fun recognizeImage(imageProxy: ImageProxy):DogRecognition{
        return withContext(Dispatchers.IO){
            imageProxy.imageInfo.rotationDegrees
            val bitmap = imageProxy.toBitmap()
            if (bitmap == null) {
                DogRecognition("",0f)

            }
            else{
                classifier.recognizeImage(bitmap).first()
            }
        }

    }
}