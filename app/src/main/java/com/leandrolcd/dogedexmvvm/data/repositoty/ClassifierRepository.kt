package com.leandrolcd.dogedexmvvm.data.repositoty

import androidx.camera.core.ImageProxy
import com.leandrolcd.dogedexmvvm.api.models.rotate
import com.leandrolcd.dogedexmvvm.core.camera.Classifier
import com.leandrolcd.dogedexmvvm.ui.model.DogRecognition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
 interface IClassifierRepository{
     suspend fun recognizeImage(imageProxy: ImageProxy): DogRecognition
 }
class ClassifierRepository @Inject constructor (private val classifier: Classifier): IClassifierRepository {

    override suspend fun recognizeImage(imageProxy: ImageProxy): DogRecognition {
        return withContext(Dispatchers.IO){
            imageProxy.imageInfo.rotationDegrees
            val bitmap = imageProxy.toBitmap()
            if (bitmap == null) {
                DogRecognition("",0f)

            }
            else{
                classifier.recognizeImage(bitmap.rotate()).first()
            }
        }

    }
}