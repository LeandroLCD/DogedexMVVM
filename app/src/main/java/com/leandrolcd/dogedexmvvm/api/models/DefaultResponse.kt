package com.leandrolcd.dogedexmvvm.api.models

import com.google.gson.annotations.SerializedName

class DefaultResponse(
    val message:String,
    @SerializedName("is_success")
    val isSuccess: Boolean
    )