package com.leandrolcd.dogedexmvvm.dogslist


import com.leandrolcd.dogedexmvvm.Dog
import com.leandrolcd.dogedexmvvm.api.DogsApi.retrofitService
import com.leandrolcd.dogedexmvvm.api.makeNetworkCall
import com.leandrolcd.dogedexmvvm.api.models.AddDogToUserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DogRepository {

    suspend fun getDogCollection(): UiStatus<List<Dog>> {

        return withContext(Dispatchers.IO) {
            val allDogDeferred = async { downloadDogs() }
            val dogUserDeferred = async { getUserDogs() }

            val allDog = allDogDeferred.await()
            val dogUser = dogUserDeferred.await()
            if (allDog is UiStatus.Error) {
                allDog
            } else if (dogUser is UiStatus.Error) {
                dogUser
            } else if(dogUser is UiStatus.Success && allDog is UiStatus.Success){

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

            response.data.dogs.map {
                Dog(
                    id = it.id,
                    index = it.index,
                    name = it.nameEs,
                    type = it.dogType,
                    temperament = it.temperament,
                    heightFemale = it.heightFemale,
                    heightMale = it.heightMale,
                    weightFemale = it.weightFemale,
                    weightMale = it.weightMale,
                    lifeExpectancy = it.lifeExpectancy,
                    imageUrl = it.imageUrl
                )

            }

        }
    }

    suspend fun downloadDogs(): UiStatus<List<Dog>> {
        return makeNetworkCall {
            val response = retrofitService.getAllDogs()
            response.data.dogs.map {
                Dog(
                    id = it.id,
                    index = it.index,
                    name = it.nameEs,
                    type = it.dogType,
                    temperament = it.temperament,
                    heightFemale = it.heightFemale,
                    heightMale = it.heightMale,
                    weightFemale = it.weightFemale,
                    weightMale = it.weightMale,
                    lifeExpectancy = it.lifeExpectancy,
                    imageUrl = it.imageUrl
                )

            }

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


}