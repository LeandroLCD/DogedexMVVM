package com.leandrolcd.dogedexmvvm.dogdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandrolcd.dogedexmvvm.Dog
import com.leandrolcd.dogedexmvvm.dogslist.DogRepository
import com.leandrolcd.dogedexmvvm.dogslist.UiStatus
import kotlinx.coroutines.launch

class DogDetailViewModel: ViewModel() {
    //region Fields
    private val _dogList = MutableLiveData<List<Dog>>()

    val dogList: LiveData<List<Dog>> = _dogList

    private val _status = MutableLiveData<UiStatus<Any>>()

    val status: LiveData<UiStatus<Any>> = _status

    private val repository = DogRepository()
    //endregion

    fun addDogToUser(dogId:Long){
        Log.i("dogId", "Long $dogId")
        viewModelScope.launch {
            _status.value = UiStatus.Loading()

            Log.i("dogIdClick", "DogID $dogId")
            handleAddDogStatus(repository.addDogToUser(dogId))
        }
    }
    private fun handleAddDogStatus(uiStatus: UiStatus<Any>) {
        if (uiStatus is UiStatus.Success) {

        }
        _status.value = uiStatus
    }
}