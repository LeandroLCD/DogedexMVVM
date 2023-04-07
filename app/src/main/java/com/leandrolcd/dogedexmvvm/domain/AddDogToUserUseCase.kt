package com.leandrolcd.dogedexmvvm.domain

import com.leandrolcd.dogedexmvvm.api.models.toDogTDO
import com.leandrolcd.dogedexmvvm.data.model.NetworkCallAnswer
import com.leandrolcd.dogedexmvvm.data.repositoty.FireStoreRepository
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import javax.inject.Inject

class AddDogToUserUseCase @Inject constructor(
    private val repository: FireStoreRepository
)  {
    suspend operator fun invoke(dog: Dog):UiStatus<Boolean>{
       return when(val resp = repository.addDogToUser(dog.toDogTDO())){
            is NetworkCallAnswer.Error -> {
                UiStatus.Error(resp.message)
            }
            is NetworkCallAnswer.Success -> {
                UiStatus.Success(resp.data)
            }
        }
    }
}