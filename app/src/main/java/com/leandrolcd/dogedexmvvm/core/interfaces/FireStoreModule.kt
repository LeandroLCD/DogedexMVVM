package com.leandrolcd.dogedexmvvm.core.interfaces

import com.leandrolcd.dogedexmvvm.data.repositoty.FireStoreRepository
import com.leandrolcd.dogedexmvvm.data.repositoty.IFireStoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FireStoreModule {
    @Binds
    abstract fun bindsFireStore(fireStoreRepository : FireStoreRepository): IFireStoreRepository

}
