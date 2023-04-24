package com.leandrolcd.dogedexmvvm.ui.camera

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Camera
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.leandrolcd.dogedexmvvm.opacity
import com.leandrolcd.dogedexmvvm.ui.camera.utils.Commons.REQUIRED_PERMISSIONS
import com.leandrolcd.dogedexmvvm.ui.doglist.DogListViewModel
import com.leandrolcd.dogedexmvvm.ui.ui.theme.primaryColor

@Composable
fun CameraCompose(
    viewModel: DogListViewModel = hiltViewModel(),
    onCaptureClick: () -> Unit,
) {
    //region Permission Cam
    val context = LocalContext.current
    var hasCamPermission by remember {
        mutableStateOf(
            REQUIRED_PERMISSIONS.all {
                ContextCompat.checkSelfPermission(context, it) ==
                        PackageManager.PERMISSION_GRANTED
            })
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { granted ->
            hasCamPermission = granted.size == 2
        }
    )
    LaunchedEffect(key1 = true) {
        launcher.launch(
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }
    //endregion



    Surface(modifier = Modifier.fillMaxSize()) {
        if (hasCamPermission) {
            Column(Modifier.fillMaxSize()) {
                AndroidView( modifier = Modifier.fillMaxSize(),
                factory = {
                    viewModel.cameraX.value.startCameraPreviewView {
                    viewModel.recognizerImage(it)
                    }
                }
            )
            }
            Column(Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally) {
                val dog = viewModel.dogRecognition.value.first()
                ButtonCamera(enabled = dog.confidence > 70,
                    modifier = Modifier.padding(bottom = 60.dp)) {
                    onCaptureClick()
                }
            }

        }
    }

}

@Composable
fun ButtonCamera(modifier: Modifier,enabled:Boolean = true, onCaptureClick: () -> Unit) {
    IconButton(
        onClick = { },
        modifier = modifier
            .clip(RoundedCornerShape(20.dp)),
        enabled = enabled,

    ) {
        if(enabled){
            Icon(imageVector = Icons.Sharp.Camera,
                contentDescription ="Capture dog",
                tint = primaryColor,
                modifier = Modifier
                    .clickable { onCaptureClick() }
                    .width(60.dp)
                    .height(60.dp))
        }else{
            Icon(imageVector = Icons.Sharp.Camera,
                contentDescription ="Capture dog",
                tint = primaryColor.opacity(0.5f),
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp))
        }

    }
}
