package com.leandrolcd.dogedexmvvm.data.repositoty

import com.leandrolcd.dogedexmvvm.api.makeNetworkCallAnswer
import com.leandrolcd.dogedexmvvm.data.model.DogDTO
import com.leandrolcd.dogedexmvvm.data.model.NetworkCallAnswer
import com.leandrolcd.dogedexmvvm.data.network.FireStoreService
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import javax.inject.Inject

class FireStoreRepository @Inject constructor(private val fireStore: FireStoreService) {
    suspend fun getDogListApp():NetworkCallAnswer<List<DogDTO>>{
        return makeNetworkCallAnswer {
            fireStore.getDogListApp()
        }
    }

    suspend fun getDogListUser():NetworkCallAnswer<List<DogDTO>>{
        return makeNetworkCallAnswer {
            fireStore.getDogListUser()
        }
    }

    suspend fun addDogToUser(dog: DogDTO):NetworkCallAnswer<Boolean>{
        return makeNetworkCallAnswer {
            fireStore.addDogDTOToUser(dog)
        }
    }
}