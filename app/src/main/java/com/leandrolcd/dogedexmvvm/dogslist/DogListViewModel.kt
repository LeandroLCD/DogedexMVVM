package com.leandrolcd.dogedexmvvm.dogslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandrolcd.dogedexmvvm.Dog
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {

    private val _dogList = MutableLiveData<List<Dog>>()

    val dogList: LiveData<List<Dog>> = _dogList

    private val _status = MutableLiveData<UiStatus<Any>>()

    val status: LiveData<UiStatus<Any>> = _status

    private val repository = DogRepository()

    init {
        collectionDogs()
    }


    private fun collectionDogs(){
        viewModelScope.launch {
            _status.value = UiStatus.Loading()
            handleResponseStatus(repository.getDogCollection())
        }
    }


    fun addDogToUser(dogId:Long){
        Log.i("dogId", "Long $dogId")
        viewModelScope.launch {
            _status.value = UiStatus.Loading()

            Log.i("dogIdClick", "DogID $dogId")
            handleAddDogStatus(repository.addDogToUser(dogId))
        }
    }
    @Suppress("UNCHECKED_CAST")
    private fun handleResponseStatus(uiStatus: UiStatus<List<Dog>>) {
        if (uiStatus is UiStatus.Success) {
            _dogList.value = uiStatus.data!!
        }
        _status.value = uiStatus as UiStatus<Any>
    }
    private fun handleAddDogStatus(uiStatus: UiStatus<Any>) {
        if (uiStatus is UiStatus.Success) {
            collectionDogs()
        }
        _status.value = uiStatus
    }
}