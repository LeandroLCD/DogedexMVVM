package com.leandrolcd.dogedexmvvm.api.models

import com.google.gson.annotations.SerializedName

class DogMlResponse (
    @SerializedName("data")
    val data: DogMl,
    @SerializedName("is_success")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String
)
