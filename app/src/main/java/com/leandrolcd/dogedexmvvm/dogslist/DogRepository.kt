package com.leandrolcd.dogedexmvvm.dogslist


import com.leandrolcd.dogedexmvvm.api.DogsApi.retrofitService
import com.leandrolcd.dogedexmvvm.api.makeNetworkCall
import com.leandrolcd.dogedexmvvm.api.models.AddDogToUserDTO
import com.leandrolcd.dogedexmvvm.api.models.toDog
import com.leandrolcd.dogedexmvvm.api.models.toDogList
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class  DogRepository @Inject constructor(private val dispatcher: CoroutineDispatcher) {

    suspend fun getDogCollection(): UiStatus<List<Dog>> {

        return withContext(dispatcher) {
            val allDogDeferred = async { downloadDogs() }
            val dogUserDeferred = async { getUserDogs() }

            val allDog = allDogDeferred.await()
            val dogUser = dogUserDeferred.await()
            if (allDog is UiStatus.Error) {
                allDog
            } else if (dogUser is UiStatus.Error) {
                dogUser
            } else if(dogUser is UiStatus.Success && allDog is UiStatus.Success){
//                for(dog in dogUser.data){
//                    val resp = repo(dog)
//                }

                UiStatus.Success(getCollectionList(allDog.data, dogUser.data))
            }
            else{
                UiStatus.Error("Se produjo un error desconocido code: 28")
            }


        }
    }

    private fun getCollectionList(allDogList: List<Dog>, userDogList: List<Dog>): List<Dog> {
        return allDogList.map {
            if (userDogList.contains(it)) {
                it
            } else {
                Dog(id = 0, index = it.index, "", "", "", "",
                    "", "", "","","", false)
            }
        }.sorted()
    }

    private suspend fun getUserDogs(): UiStatus<List<Dog>> {
        return makeNetworkCall {
            val response = retrofitService.getUserDogs()

            response.data.dogs.toDogList()

        }
    }

    suspend fun downloadDogs(): UiStatus<List<Dog>> {
        return makeNetworkCall {
            val response = retrofitService.getAllDogs()
            response.data.dogs.toDogList()

        }

    }

    suspend fun addDogToUser(dogId: Long): UiStatus<Any> {
        return makeNetworkCall {
            val dogResponse = retrofitService.addDog(AddDogToUserDTO(dogId))
            if (!dogResponse.isSuccess) {
                throw Exception(dogResponse.message)
            }
            dogResponse
        }
    }

    suspend fun getDogByMlId(mlDogId: String): UiStatus<Dog> {
        return makeNetworkCall {
            val response = retrofitService.getDogByMlId(mlDogId)
            if (!response.isSuccess) {
                throw Exception(response.message)
            }
            response.data.dog.toDog()
        }
    }
}