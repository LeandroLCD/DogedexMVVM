package com.leandrolcd.dogedexmvvm.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.leandrolcd.dogedexmvvm.data.model.DogDTO
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireStoreService @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val loginService: LoginService
) {
    suspend fun getDogListApp(): List<DogDTO> {
        val querySnapshot =
            fireStore.collection("DogList")
                .orderBy("id", Query.Direction.ASCENDING)
                .get()
                .await()

        val dogList = mutableListOf<DogDTO>()
        for (document in querySnapshot.documents) {
            val dog = document.toObject(DogDTO::class.java)
            dog?.let {
                dogList.add(it)
            }
        }
        return dogList
    }

    suspend fun getDogListUser(): List<DogDTO> {

        val user = loginService.getUser()
        val querySnapshot = user?.uid?.let { uid ->
            val db = fireStore.collection("users").document(uid).collection("dogList")
            db.get().await()
        }
        val dogList = mutableListOf<DogDTO>()
        for (document in querySnapshot!!.documents) {
            val dog = document.toObject(DogDTO::class.java)
            dog?.let {
                dogList.add(it)
            }
        }
        return dogList
    }

    suspend fun addDogDTOToUser(dog: DogDTO): Boolean {
        val user = loginService.getUser()
        user?.run {
            val db = fireStore.collection("users").document(uid).collection("dogList")
            db.add(dog).await()
            return true
        }
        return false
    }
}



