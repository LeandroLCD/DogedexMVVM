package com.leandrolcd.dogedexmvvm.core.interfaces

import com.leandrolcd.dogedexmvvm.data.repositoty.ClassifierRepository
import com.leandrolcd.dogedexmvvm.data.repositoty.IClassifierRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ClassifierModule {
    @Binds
    abstract fun bindsClassifier(classifierRepository: ClassifierRepository): IClassifierRepository

}