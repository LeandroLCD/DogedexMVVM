package com.leandrolcd.dogedexmvvm.api.models


import com.google.gson.annotations.SerializedName
import com.leandrolcd.dogedexmvvm.data.model.DogDTO

data class Data(
    @SerializedName("dogs")
    val dogs: List<DogDTO>
)