package com.leandrolcd.dogedexmvvm.dogslist


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {

    private val _dogList = MutableLiveData<List<Dog>>()

    val dogList: LiveData<List<Dog>> = _dogList

    var dogComposeList = mutableStateOf<List<Dog>>(listOf())
        private set
    var statusCompose = mutableStateOf<UiStatus<Any>?>(null)
        private set

    private val _status = MutableLiveData<UiStatus<Any>>()

    val status: LiveData<UiStatus<Any>> = _status



    private val repository = DogRepository(Dispatchers.IO)

    init {
        collectionDogs()
    }


    private fun collectionDogs(){
        viewModelScope.launch {
            _status.value = UiStatus.Loading()
            handleResponseStatus(repository.getDogCollection())
        }
    }



    @Suppress("UNCHECKED_CAST")
    private fun handleResponseStatus(uiStatus: UiStatus<List<Dog>>) {
        if (uiStatus is UiStatus.Success) {
           // _dogList.value = uiStatus.data ?: listOf<Dog>()
        }
        _status.value = uiStatus as UiStatus<Any>
    }

}