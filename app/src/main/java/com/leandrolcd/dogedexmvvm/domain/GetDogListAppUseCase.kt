package com.leandrolcd.dogedexmvvm.domain

import com.leandrolcd.dogedexmvvm.api.models.toDogList
import com.leandrolcd.dogedexmvvm.data.model.NetworkCallAnswer
import com.leandrolcd.dogedexmvvm.data.repositoty.FireStoreRepository
import com.leandrolcd.dogedexmvvm.ui.model.UiStatus
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import javax.inject.Inject

class GetDogListAppUseCase @Inject constructor(
    private val repository:FireStoreRepository) {
    suspend operator fun invoke() {
        repository.getDogCollection()
    }
}