package com.leandrolcd.dogedexmvvm.data.repositoty

import android.util.Log
import com.leandrolcd.dogedexmvvm.api.makeNetworkCall
import com.leandrolcd.dogedexmvvm.api.makeNetworkCallAnswer
import com.leandrolcd.dogedexmvvm.api.models.toDog
import com.leandrolcd.dogedexmvvm.api.models.toDogList
import com.leandrolcd.dogedexmvvm.data.model.DogDTO
import com.leandrolcd.dogedexmvvm.data.model.NetworkCallAnswer
import com.leandrolcd.dogedexmvvm.data.network.FireStoreService
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import com.leandrolcd.dogedexmvvm.ui.model.UiStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
interface IFireStoreRepository{
    suspend fun addDogToUser(dogId: String): UiStatus<Boolean>
    suspend fun getDogCollection(): UiStatus<List<Dog>>
    suspend fun getDogById(id:String):UiStatus<Dog>
}

class FireStoreRepository @Inject constructor(
    private val fireStore: FireStoreService,
    private val dispatcher: CoroutineDispatcher):IFireStoreRepository {
    override suspend fun addDogToUser(dogId: String): UiStatus<Boolean> {
        return makeNetworkCall(dispatcher) {
            fireStore.addDogDTOToUser(dogId)
        }

    }

    override suspend fun getDogCollection(): UiStatus<List<Dog>> {
        return withContext(dispatcher) {
            val allDogDeferred = async { getDogListApp() }
            val dogUserDeferred = async { getDogListUser() }

            val allDog = allDogDeferred.await()
            val dogUser = dogUserDeferred.await()

            if(allDog is UiStatus.Error){
                allDog
            }
            if(dogUser is UiStatus.Error){
                dogUser
            }
            if(allDog is UiStatus.Success && dogUser is UiStatus.Success){
                UiStatus.Success(getCollectionList(allDog.data, dogUser.data))
            }
            else{
                UiStatus.Error("Error desconocido")
            }



        }
    }
    override suspend fun getDogById(id:String):UiStatus<Dog>{
        return makeNetworkCall(dispatcher) {
            fireStore.getDogById(id).toDog()
        }
    }
    private suspend fun getDogListApp(): UiStatus<List<DogDTO>> {
        return makeNetworkCall(dispatcher) {
            fireStore.getDogListApp()
        }
    }

    private suspend fun getDogListUser(): UiStatus<List<String>> {
        return makeNetworkCall(dispatcher) {
            fireStore.getDogListUser()
        }
    }

    private fun getCollectionList(allDogList: List<DogDTO>, userDogList: List<String>): List<Dog> {
        return allDogList.toDogList().map {
            if (userDogList.contains(it.id)) {
                it.inCollection = true
                it
            } else {
                Dog(mlId = it.id, index = it.index)
            }
        }.sorted()
    }
}