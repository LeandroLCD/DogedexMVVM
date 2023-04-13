package com.leandrolcd.dogedexmvvm.api.models

import android.graphics.*
import androidx.camera.core.ImageProxy
import com.leandrolcd.dogedexmvvm.data.model.DogDTO
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import java.io.ByteArrayOutputStream

fun DogDTO.toDog(): Dog {
    return this.run {
        Dog(
            index = this.index,
            mlId = this.mlId,
            imageUrl = this.imageUrl,
            lifeExpectancy = this.lifeExpectancy,
            name = this.name,
            temperament = this.temperament,
            raza = this.raza,
            weightMale = this.weightMale,
            weightFemale = this.weightFemale,
            heightMale = this.heightMale,
            heightFemale = this.heightFemale,
            curiosities = this.curiosities,
        )

    }
}

fun Dog.toDogTDO(): DogDTO {
    return this.run {
        DogDTO(
            id,
            raza,
            heightFemale,
            heightMale,
            imageUrl,
            lifeExpectancy,
            name,
            curiosities,
            temperament,
            weightFemale,
            weightMale,
            mlId,
            index
        )
    }
}

fun List<DogDTO>.toDogList(): List<Dog> {
    return this.map {
        Dog(
            index = it.index,
            imageUrl = it.imageUrl,
            lifeExpectancy = it.lifeExpectancy,
            name = it.name,
            temperament = it.temperament,
            raza = it.raza,
            weightMale = it.weightMale,
            weightFemale = it.weightFemale,
            heightMale = it.heightMale,
            heightFemale = it.heightFemale,
            curiosities = it.curiosities,
            id = it.id?:"",
            mlId = it.mlId
        )

    }
}

fun Any.isNull(): Boolean = this == null


fun ImageProxy.toBitmap(): Bitmap? {

    if (this == null) return null

    val yBuffer = this.planes[0].buffer
    val uBuffer = this.planes[1].buffer
    val vBuffer = this.planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)


    val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

fun Bitmap.rotate(angle:Float=90f): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}