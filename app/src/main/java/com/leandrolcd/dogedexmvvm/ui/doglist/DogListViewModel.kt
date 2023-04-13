package com.leandrolcd.dogedexmvvm.ui.doglist


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandrolcd.dogedexmvvm.domain.GetDogListUseCase
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import com.leandrolcd.dogedexmvvm.ui.model.UiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogListViewModel @Inject constructor(
    private val dogUseCase: GetDogListUseCase
) : ViewModel() {


    var uiStatus = mutableStateOf<UiStatus<List<Dog>>>(UiStatus.Loaded())
        private set

    init {
        collectionDogs()
    }


    private fun collectionDogs() {
        uiStatus.value = UiStatus.Loading()
        viewModelScope.launch {
            uiStatus.value = dogUseCase()
        }
    }


}