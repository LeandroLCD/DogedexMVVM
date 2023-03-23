package com.leandrolcd.dogedexmvvm.api.models

import com.google.gson.annotations.SerializedName

class AddDogToUserDTO (
    @SerializedName("dog_id")
    val dogId:Long
)