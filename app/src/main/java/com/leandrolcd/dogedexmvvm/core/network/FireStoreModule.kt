package com.leandrolcd.dogedexmvvm.core.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.FirestoreClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FireStoreModule {

    @Provides
    @Singleton
    fun ProvideFireSore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}