package com.leandrolcd.dogedexmvvm.domain

import com.leandrolcd.dogedexmvvm.data.repositoty.FireStoreRepository
import javax.inject.Inject

class GetDogListAppUseCase @Inject constructor(
    private val repository:FireStoreRepository) {
    suspend operator fun invoke() {
        repository.getDogCollection()
    }
}