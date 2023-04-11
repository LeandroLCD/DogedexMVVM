package com.leandrolcd.dogedexmvvm.dogdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandrolcd.dogedexmvvm.dogslist.DogRepository
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DogDetailViewModel: ViewModel() {
    //region Fields

    var composeStatus = mutableStateOf<UiStatus<Any>?>(null)
    private set


    private val _status = MutableLiveData<UiStatus<Any>>()

    val status: LiveData<UiStatus<Any>> = _status

    private val repository = DogRepository(Dispatchers.IO)
    //endregion

    fun addDogToUser(dogId:Long){
        viewModelScope.launch {
            _status.value = UiStatus.Loading()
            composeStatus.value = UiStatus.Loading()
            handleAddDogStatus(repository.addDogToUser(dogId))
        }
    }
    private fun handleAddDogStatus(uiStatus: UiStatus<Any>) {

        composeStatus.value = uiStatus
            _status.value = uiStatus
    }

    fun resetUiState() {
        composeStatus.value = null
    }
}