package com.leandrolcd.dogedexmvvm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.leandrolcd.dogedexmvvm.api.ApiServiInterceptor
import com.leandrolcd.dogedexmvvm.auth.LoginActivity
import com.leandrolcd.dogedexmvvm.auth.model.User
import com.leandrolcd.dogedexmvvm.databinding.ActivityMainBinding
import com.leandrolcd.dogedexmvvm.dogslist.DogListActivity
import com.leandrolcd.dogedexmvvm.machinelearning.Classifier
import com.leandrolcd.dogedexmvvm.setting.SettingActivity
import org.tensorflow.lite.support.common.FileUtil
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Suppress("IMPLICIT_CAST_TO_ANY")
class MainActivity : AppCompatActivity() {
    //region Fields
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                setupCamera()
            } else {
                Toast.makeText(this, getString(R.string.msjPermission), Toast.LENGTH_LONG).show()
            }
        }
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutors: ExecutorService
    private lateinit var classifier:Classifier
    private var isCameraReady = false
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user = User.getLoggedInUser(this)

        if (user == null) {
            openLogin()
            return
        }
        ApiServiInterceptor.setSessionToken(user.authenticationToken)
        Log.i("user", user.toString())
        binding.settingFab.setOnClickListener {
            openSettingActivity()
        }
        binding.dogListPhotoFab.setOnClickListener {
            openDogListActivity()
        }
        binding.takePhotoFab.setOnClickListener {
            if (isCameraReady) {
                takePhoto()
            }

        }
        requestCamaraPermission()
    }

    //region Camera methods
    private fun setupCamera() {
        binding.cameraPreview.post {
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.cameraPreview.display.rotation)
                .build()
            cameraExecutors = Executors.newSingleThreadExecutor()

            startCamera()
            isCameraReady = true
        }

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val imageAnalysis = ImageAnalysis.Builder()

                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(cameraExecutors) { imageProxy ->
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                // insert your code here.

                // after done, release the ImageProxy object
                imageProxy.close()
            }
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalysis
            )
        }, ContextCompat.getMainExecutor(this))

    }

    private fun requestCamaraPermission() = when {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED -> {
            setupCamera()
        }
        shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
            AlertDialog.Builder(this)
                .setMessage(R.string.app_name)
                .setMessage(getString(R.string.messageCamera))
                .setPositiveButton(R.string.OK) { _, _ ->
                    this.requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
                .setNegativeButton(R.string.Cancelar) { _, _ -> }
                .show()


        }
        else -> {
            this.requestPermissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }

    private fun getOutPutPhotoFile(): File {

        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name) + ".jpg").apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists()) {
            mediaDir
        } else {
            filesDir
        }


    }

    private fun takePhoto() {
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(getOutPutPhotoFile()).build()
        imageCapture.takePicture(outputFileOptions, cameraExecutors,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_LONG).show()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val photoUri = outputFileResults.savedUri?.path

                    val bitmap = BitmapFactory.decodeFile(photoUri)

                    classifier.recognizeImage(bitmap)

                    openWholeImageActivity(photoUri ?: "")
                }
            })
    }

    override fun onStart() {
        super.onStart()
        classifier = Classifier(FileUtil.loadMappedFile(this@MainActivity, MODEL_PATH),
            FileUtil.loadLabels(this@MainActivity,LABEL_PATH)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraExecutors.isInitialized) {
            cameraExecutors.shutdown()
        }

    }
    //endregion

    private fun openWholeImageActivity(photoUri: String) {
        val intent = Intent(this, WholeImageActivity::class.java)
        intent.putExtra(WholeImageActivity.PHOTO_URI, photoUri)
        startActivity(intent)
    }

    private fun openDogListActivity() {
        startActivity(Intent(this, DogListActivity::class.java))
    }

    private fun openSettingActivity() {
        startActivity(Intent(this, SettingActivity::class.java))
    }

    private fun openLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}