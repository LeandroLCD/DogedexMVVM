package com.leandrolcd.dogedexmvvm.domain

import android.util.Log
import com.leandrolcd.dogedexmvvm.api.models.toDogList
import com.leandrolcd.dogedexmvvm.data.model.NetworkCallAnswer
import com.leandrolcd.dogedexmvvm.data.repositoty.FireStoreRepository
import com.leandrolcd.dogedexmvvm.data.repositoty.IFireStoreRepository
import com.leandrolcd.dogedexmvvm.ui.model.UiStatus
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import javax.inject.Inject

class GetDogListUseCase @Inject constructor(
    private val repository: IFireStoreRepository
) {
    suspend operator fun invoke(): UiStatus<List<Dog>> {
        Log.d("TAG", "invoke: Repository")
        return  repository.getDogCollection()        }

}