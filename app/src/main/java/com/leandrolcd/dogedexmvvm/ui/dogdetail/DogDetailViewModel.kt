package com.leandrolcd.dogedexmvvm.ui.dogdetail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandrolcd.dogedexmvvm.data.repositoty.IFireStoreRepository
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import com.leandrolcd.dogedexmvvm.ui.model.UiStatus
import com.leandrolcd.dogedexmvvm.ui.model.UiStatus.Error
import com.leandrolcd.dogedexmvvm.ui.model.UiStatus.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogDetailViewModel @Inject constructor(
    private val repository: IFireStoreRepository
): ViewModel() {
    //region Fields

    var uiStatus = mutableStateOf<UiStatus<Dog>>(UiStatus.Loaded())
    private set

    var dogStatus = mutableStateOf<Dog?>(null)
        private set

    //endregion
fun getDogById(dogId:String){
        uiStatus.value = UiStatus.Loading()
        viewModelScope.launch {

            val status = repository.getDogById(dogId)
            when(status){
                is Success -> {
                    dogStatus.value = status.data
                    dogStatus.value?.id=dogId
                }
                else->{
                    dogStatus.value = null

                }
            }
            uiStatus.value = status
        }
}
    fun addDogToUser(dogId: String){
        uiStatus.value = UiStatus.Loading()
        viewModelScope.launch {

            var resp = repository.addDogToUser(dogId)
            when(resp){
                is Error -> uiStatus.value = Error(resp.message)
                is Success -> uiStatus.value = UiStatus.Loaded()
                else ->{
                    uiStatus.value = UiStatus.Loaded()
                }
            }
        }
    }


    fun TryAgain() {
        uiStatus.value = UiStatus.Loaded()
    }
}