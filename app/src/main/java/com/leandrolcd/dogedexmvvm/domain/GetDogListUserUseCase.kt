package com.leandrolcd.dogedexmvvm.domain

import com.leandrolcd.dogedexmvvm.api.models.toDogList
import com.leandrolcd.dogedexmvvm.data.model.NetworkCallAnswer
import com.leandrolcd.dogedexmvvm.data.repositoty.FireStoreRepository
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import javax.inject.Inject

class GetDogListUserUseCase @Inject constructor(
    private val repository: FireStoreRepository
) {
    suspend operator fun invoke(): UiStatus<List<Dog>> {
        return when(val resp = repository.getDogListUser()){
            is NetworkCallAnswer.Error -> {
                UiStatus.Error(resp.message)
            }
            is NetworkCallAnswer.Success -> {

                UiStatus.Success(resp.data.toDogList())
            }
        }
    }
}