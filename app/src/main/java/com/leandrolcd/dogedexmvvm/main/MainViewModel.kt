package com.leandrolcd.dogedexmvvm.main

import androidx.camera.core.ImageProxy
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import com.leandrolcd.dogedexmvvm.dogslist.DogRepository
import com.leandrolcd.dogedexmvvm.domain.AuthLoginUseCase
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus
import com.leandrolcd.dogedexmvvm.core.camera.Classifier
import com.leandrolcd.dogedexmvvm.data.repositoty.ClassifierRepository
import com.leandrolcd.dogedexmvvm.machinelearning.DogRecognition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.MappedByteBuffer
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val auth: AuthLoginUseCase): ViewModel() {
private val dogRepository = DogRepository(Dispatchers.IO)
    private lateinit var classifierRepository: ClassifierRepository
    val userCurrent = mutableStateOf(auth.getUser())
    //region Properties
    private val _dog = MutableLiveData<Dog>()
    val dog: LiveData<Dog> = _dog
    private val _status = MutableLiveData<UiStatus<Dog>>()
    val status: LiveData<UiStatus<Dog>> = _status
    private val _dogRecognition = MutableLiveData<DogRecognition>()
    val dogRecognition: LiveData<DogRecognition> = _dogRecognition
    //endregion
    fun setupClassifier(tfLiteModel: MappedByteBuffer,
                        labels: List<String>){
        val classifier = Classifier(tfLiteModel, labels)
        classifierRepository= ClassifierRepository(classifier)
    }

    fun getDogByMlId(mlDogId: String){
        _status.value = UiStatus.Loading()
        viewModelScope.launch {
            handleResponseStatus(dogRepository.getDogByMlId(mlDogId))
        }
    }
    fun recognizerImage(imageProxy: ImageProxy){
        viewModelScope.launch {
            _dogRecognition.value = classifierRepository.recognizeImage(imageProxy)
            imageProxy.close()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun handleResponseStatus(uiStatus: UiStatus<Dog>) {
        if (uiStatus is UiStatus.Success) {
            //_dog.value = uiStatus.data!!
        }
        //_status.value = uiStatus
    }
}