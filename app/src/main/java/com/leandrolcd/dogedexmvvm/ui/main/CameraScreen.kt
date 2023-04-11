package com.leandrolcd.dogedexmvvm.ui.main

import android.annotation.SuppressLint
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.leandrolcd.dogedexmvvm.getCameraProvider
import com.leandrolcd.dogedexmvvm.machinelearning.DogRecognition
import com.leandrolcd.dogedexmvvm.opacity
import com.leandrolcd.dogedexmvvm.ui.ui.theme.backGroudColor
import com.leandrolcd.dogedexmvvm.ui.ui.theme.primaryColor

@ExperimentalMaterial3Api
@SuppressLint("RestrictedApi", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CameraScream(
    navigationController: NavHostController,
    viewModel: CameraScreamViewModel = hiltViewModel()
) {

    //region Fields
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder().build()
    }

    val previewView = remember { PreviewView(context) }

    val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    imageAnalysis.setAnalyzer(viewModel.executorCamera) { imageProxy ->
        viewModel.recognizerImage(imageProxy)

    }

    val preview = Preview.Builder().build()

    val lensFacing = CameraSelector.LENS_FACING_BACK

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture,
            imageAnalysis
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

//endregion

    MyScaffolf(previewView, navigationController, viewModel.dogRecognition.value, viewModel)

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyScaffolf(
    previewView: PreviewView,
    navigationController: NavHostController,
    dogRecognition: DogRecognition,
    viewModel: CameraScreamViewModel
) {
    androidx.compose.material.Scaffold(
        bottomBar = { MyButtonNavigation(navigationController) },
        floatingActionButton = {
            MyFab(dogRecognition.confidence > 70) {
                viewModel.getDogByMlId(
                    dogRecognition.id
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true

    ) {

        AndroidView({ previewView }, modifier = Modifier.fillMaxSize()) {
        }


    }
}

@Composable
fun MyFab(enabledPhoto: Boolean, onClick: () -> Unit) {
    if (enabledPhoto) {
        FloatingActionButton(
            onClick = { onClick() },
            shape = CircleShape,
            backgroundColor = primaryColor
        ) {
            Icon(
                imageVector = Icons.Sharp.Camera,
                contentDescription = "Button Photo",
                tint = Color.Black
            )
        }
    } else {
        FloatingActionButton(
            onClick = { },
            shape = CircleShape,
            backgroundColor = primaryColor.opacity(0.3f)
        ) {
            Icon(
                imageVector = Icons.Sharp.Camera, tint = Color.Black,
                contentDescription = "Button Photo"
            )
        }
    }

}

@Composable
fun MyButtonNavigation(navigationController: NavHostController) {
    var index by rememberSaveable { mutableStateOf(0) }
    BottomNavigation(backgroundColor = backGroudColor, contentColor = Color.Black) {
        BottomNavigationItem(selected = index == 0, onClick = {
            index = 0
            //navegate doglist
        },
            icon = {
                Icon(imageVector = Icons.Sharp.List, "Dog List")
            })
        BottomNavigationItem(selected = index == 1,
            onClick = {
                index = 1
                //navegate seting
            }, icon = {
                androidx.compose.material.Icon(
                    imageVector = Icons.Sharp.Settings,
                    contentDescription = "Setting"
                )
            })

    }
}



