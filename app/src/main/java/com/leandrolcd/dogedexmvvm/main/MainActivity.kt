package com.leandrolcd.dogedexmvvm.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import com.leandrolcd.dogedexmvvm.LABEL_PATH
import com.leandrolcd.dogedexmvvm.MODEL_PATH
import com.leandrolcd.dogedexmvvm.R
import com.leandrolcd.dogedexmvvm.api.models.isNull
import com.leandrolcd.dogedexmvvm.auth.LoginActivity
import com.leandrolcd.dogedexmvvm.databinding.ActivityMainBinding
import com.leandrolcd.dogedexmvvm.dogdetail.DogDetailComposeActivity
import com.leandrolcd.dogedexmvvm.dogslist.DogListComposeActivity
import com.leandrolcd.dogedexmvvm.core.camera.Classifier
import com.leandrolcd.dogedexmvvm.machinelearning.DogRecognition
import com.leandrolcd.dogedexmvvm.setting.SettingActivity
import com.leandrolcd.dogedexmvvm.ui.authentication.LoginComposeActivity
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.support.common.FileUtil
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@ExperimentalCoilApi
@Suppress("IMPLICIT_CAST_TO_ANY")
@AndroidEntryPoint
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
    private val viewModel: MainViewModel by viewModels()
    private var isCameraReady = false
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (viewModel.userCurrent.value?.uid == null) {

            startActivity(Intent(this, LoginComposeActivity::class.java))
            finish()
        }else{
            Log.d("TAG", "onCreate: ${viewModel.userCurrent.value?.uid }")
        }
        binding.settingFab.setOnClickListener {
            openSettingActivity()
        }
        binding.dogListPhotoFab.setOnClickListener {
            openDogListActivity()
        }

        viewModel.status.observe(this) {
            when (it) {
                is UiStatus.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                is UiStatus.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.takePhotoFab.alpha = 0.2f
                    binding.takePhotoFab.setOnClickListener(null)
                }
                is UiStatus.Success -> binding.pbLoading.visibility = View.GONE
                else -> {}
            }
        }

        viewModel.dog.observe(this) { dog ->
            if (!dog.isNull()) {
                openDogDetailActivity(dog, true)
            }
        }

        viewModel.dogRecognition.observe(this){
            enableTakePhotoButton(it)
        }

        requestCamaraPermission()
    }

    private fun openDogDetailActivity(dog: Dog, isRecognition:Boolean) {
        val intent = Intent(this, DogDetailComposeActivity::class.java)
        intent.putExtra(DogDetailComposeActivity.DOG_KEY, dog)
        intent.putExtra(DogDetailComposeActivity.IS_RECOGNITION_KEY, isRecognition)

        startActivity(intent)
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
                viewModel.recognizerImage(imageProxy)

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

    private fun enableTakePhotoButton(dogRecognition: DogRecognition) {
        if (dogRecognition.confidence > 70.0) {
            binding.takePhotoFab.alpha = 1f
            binding.takePhotoFab.setOnClickListener {
                viewModel.getDogByMlId(dogRecognition.id)
            }
        } else {
            binding.takePhotoFab.alpha = 0.2f
            binding.takePhotoFab.setOnClickListener(null)
        }

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


    //region Methods para tomar foto en desUso
    private fun takePhoto() {
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(getOutPutPhotoFile()).build()
        imageCapture.takePicture(outputFileOptions, cameraExecutors,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_LONG).show()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    val photoUri = outputFileResults.savedUri?.path
//
//                    val bitmap = BitmapFactory.decodeFile(photoUri)
//
//                    val dogRecognition = classifier.recognizeImage(bitmap).first()
//
//                    viewModel.getDogByMlId(dogRecognition.id)

                    // openWholeImageActivity(photoUri ?: "")
                }
            })
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

    //endregion
    override fun onStart() {
        super.onStart()
        viewModel.setupClassifier(
            FileUtil.loadMappedFile(this@MainActivity, MODEL_PATH),
            FileUtil.loadLabels(this@MainActivity, LABEL_PATH)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraExecutors.isInitialized) {
            cameraExecutors.shutdown()
        }

    }
    //endregion

//    private fun openWholeImageActivity(photoUri: String) {
//        val intent = Intent(this, WholeImageActivity::class.java)
//        intent.putExtra(WholeImageActivity.PHOTO_URI, photoUri)
//        startActivity(intent)
//    }

    private fun openDogListActivity() {
        startActivity(Intent(this, DogListComposeActivity::class.java))
    }

    private fun openSettingActivity() {
        startActivity(Intent(this, SettingActivity::class.java))
    }

    private fun openLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}