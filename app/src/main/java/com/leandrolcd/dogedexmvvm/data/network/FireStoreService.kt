package com.leandrolcd.dogedexmvvm.data.network

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.gson.Gson
import com.leandrolcd.dogedexmvvm.data.model.DogDTO
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireStoreService @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val loginService: LoginService
) {
    @SuppressLint("SuspiciousIndentation")
    suspend fun getDogListApp(): List<DogDTO> {
        val dogList = mutableListOf<DogDTO>()
            val querySnapshot = fireStore.collection("DogListApp")
                .get()
                .await()

                    Log.d("TAG", "getDogListApp: ${querySnapshot.documents}")
                    for (document in querySnapshot.documents) {
                        document.id
                        val dog = document.toObject<DogDTO>()
                        dog?.let {
                            dogList.add(it)
                        }
                    }

        return dogList
    }

    suspend fun getDogListUser(): List<String> {

        val user = loginService.getUser()
        val querySnapshot = user?.uid?.let { uid ->
            val db = fireStore.collection("users").document(uid).collection("dogList")
            db.get().await()
        }
        val dogList = mutableListOf<String>()
        for (document in querySnapshot!!.documents) {
            val dog = document.toObject<String>()
            dog?.let {
                dogList.add(it)
            }
        }
        return dogList
    }

    suspend fun addDogDTOToUser(dogId: String): Boolean {
        val user = loginService.getUser()
        user?.run {
            val db = fireStore.collection("users").document(uid).collection("dogList")
            dogId.let { db.add(it).await() }
            return true
        }
        return false
    }

    suspend fun addDog(dog: DogDTO): String {

            val db = fireStore.collection("DogListApp")
            db.add(dog).await()
            return db.id
    }

   suspend fun getDogById(id: String): DogDTO {
       val doc = fireStore.document("DogListApp/$id").get().await()
       val dog = doc.toObject<DogDTO>()
       return dog ?: DogDTO()

    }
}



