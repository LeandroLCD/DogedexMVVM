package com.leandrolcd.dogedexmvvm.ui.camera

import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.leandrolcd.dogedexmvvm.data.network.FireStoreService
import com.leandrolcd.dogedexmvvm.data.repositoty.IClassifierRepository
import com.leandrolcd.dogedexmvvm.data.repositoty.IFireStoreRepository
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import com.leandrolcd.dogedexmvvm.ui.model.DogRecognition
import com.leandrolcd.dogedexmvvm.ui.model.UiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import javax.inject.Inject

@HiltViewModel
class CameraScreamViewModel @Inject constructor(
    cameraExecutors: ExecutorService,
    private val classifierRepository: IClassifierRepository,
    private val repository: IFireStoreRepository,
    private val dataStore: FireStoreService
) : ViewModel() {
    init {
        val d =
            "{\"index\"=1,\"raza\": \"Pequeeña\",\"heightFemale\": \"15.2-20.3 cm (6-8 in)\", \"heightMale\": \"15.2-22.9 cm (6-9 in)\", \"imageUrl\": \"https://soyunperro.com/wp-content/uploads/2017/10/tipos-de-chihuahua.jpg\", \"lifeExpectancy\": \"12 - 20\",\"name\": \"CHIHUAHUA\", \"curiosities\": \"El Chihuahua es una de las razas de perros más antiguas de América del Norte, con evidencia de su existencia que se remonta a la época de los toltecas en México. Aunque es una raza pequeña, son conocidos por ser valientes y protectores de sus dueños, lo que los convierte en excelentes perros de compañía. Hay dos variedades Chihuahua: el de pelo largo y el de pelo corto. Ambos son igual de populares y tienen características únicas. Debido a su pequeño tamaño, son vulnerables a lesiones y enfermedades. Es importante que los dueños de Chihuahuas los cuiden y los protejan adecuadamente. El Chihuahua es una raza inteligente y fácil de entrenar, pero puede ser obstinado y terco a veces. A pesar de su tamaño, los Chihuahuas son excelentes perros guardianes y tienen una gran capacidad para alertar a sus dueños de cualquier peligro. Los Chihuahuas tienen una expectativa de vida relativamente larga en comparación con otras razas de perros pequeños, con una vida promedio de 12 a 20 años. El Chihuahua es una raza muy popular en todo el mundo y es especialmente querido en México, donde es considerado un símbolo de la cultura y la historia mexicanas.\",\"temperament\": \"Alegre, sobreprotector, inquieto, valiente.\", \"weightFemale\": \"1.5-3 kg (3-6.6 lb)\", \"weightMale\": \"1.5-3 kg (3-6.6 lb)\",\"mlId\": \"n02085620-chihuahua\"}"
        val dog = Gson().fromJson(d, Dog::class.java)
        viewModelScope.launch {
            var list = dataStore.getDogListApp()
            Log.d("DogList", ": $list")

            //var id = dataStore.addDog(dog.toDogTDO())
        }
    }

    //region Properties
    val executorCamera = cameraExecutors

    var uiStatus = mutableStateOf<UiStatus<Dog>>(UiStatus.Loaded())
        private set

    val dogRecognition = mutableStateOf(DogRecognition("", 0f))
    //endregion


    //region Methods


    fun getDogByMlId(mlDogId: String) {
        uiStatus.value = UiStatus.Loading()

        viewModelScope.launch {
            uiStatus.value = repository.getDogById(mlDogId)
        }
    }

    fun recognizerImage(imageProxy: ImageProxy) {
        viewModelScope.launch {
            dogRecognition.value = classifierRepository.recognizeImage(imageProxy)
            imageProxy.close()
        }
    }

    //endregion
}