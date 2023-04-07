package com.leandrolcd.dogedexmvvm.api.models

import com.google.gson.annotations.SerializedName
import com.leandrolcd.dogedexmvvm.data.model.DogDTO

class DogMl (
    @SerializedName("dog")
    val dog: DogDTO
    )
