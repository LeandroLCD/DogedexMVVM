package com.leandrolcd.dogedexmvvm.api.models

import android.annotation.SuppressLint
import android.graphics.*
import androidx.camera.core.ImageProxy
import com.leandrolcd.dogedexmvvm.Dog
import java.io.ByteArrayOutputStream

fun DogDTO.toDog(): Dog {
    return  this.run {
    Dog(
        id = this.id,
        index = this.index,
        imageUrl = this.imageUrl,
        lifeExpectancy  = this.lifeExpectancy,
        name = this.nameEs,
        temperament = this.temperament,
        type = this.dogType,
        weightMale  = this.weightMale,
        weightFemale = this.weightFemale,
        heightMale = this.heightMale,
        heightFemale = this.heightFemale,
    )

    }
}

fun List<DogDTO>.toDogList():List<Dog>{
    return this.map {
        Dog(
            id = it.id,
            index = it.index,
            name = it.nameEs,
            type = it.dogType,
            temperament = it.temperament,
            heightFemale = it.heightFemale,
            heightMale = it.heightMale,
            weightFemale = it.weightFemale,
            weightMale = it.weightMale,
            lifeExpectancy = it.lifeExpectancy,
            imageUrl = it.imageUrl
        )

    }
}

fun Any.isNull():Boolean = this == null

@SuppressLint("UnsafeOptInUsageError")
fun ImageProxy.toBitmap(): Bitmap? {
 if(this == null) return null

    val yBuffer = this.planes[0].buffer
    val uBuffer = this.planes[1].buffer
    val vBuffer = this.planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize )

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)


    val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}