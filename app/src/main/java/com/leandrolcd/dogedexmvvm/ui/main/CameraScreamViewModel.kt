package com.leandrolcd.dogedexmvvm.ui.main

import androidx.camera.core.ImageProxy
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandrolcd.dogedexmvvm.data.repositoty.IClassifierRepository
import com.leandrolcd.dogedexmvvm.dogslist.DogRepository
import com.leandrolcd.dogedexmvvm.machinelearning.DogRecognition
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import javax.inject.Inject

@HiltViewModel
class CameraScreamViewModel @Inject constructor(
    cameraExecutors: ExecutorService,
    private val classifierRepository: IClassifierRepository,
    private val dogRepository: DogRepository
    ): ViewModel() {
    //region Properties
    val executorCamera = cameraExecutors

    var uiStatus = mutableStateOf<UiStatus<Dog>>(UiStatus.Loaded())
        private set

    val dogRecognition = mutableStateOf(DogRecognition("", 0f))
    //endregion


    //region Methods


    fun getDogByMlId(mlDogId: String){
        uiStatus.value = UiStatus.Loading()
        viewModelScope.launch {
            handleResponseStatus(dogRepository.getDogByMlId(mlDogId))
        }
    }
    fun recognizerImage(imageProxy: ImageProxy){
        viewModelScope.launch {
            dogRecognition.value = classifierRepository.recognizeImage(imageProxy)
            imageProxy.close()
        }
    }

    private fun handleResponseStatus(status: UiStatus<Dog>) {

        uiStatus.value = status
    }
    //endregion
}