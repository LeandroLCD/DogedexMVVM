package com.leandrolcd.dogedexmvvm

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(
                cameraProvider.get()
            )
        }, ContextCompat.getMainExecutor(this))
    }
}

fun Color.opacity(alpha:Float): Color {

    return Color(this.red, this.green, this.blue, alpha)
}