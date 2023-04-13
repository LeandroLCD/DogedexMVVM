package com.leandrolcd.dogedexmvvm.data.model

import com.google.firebase.firestore.PropertyName


data class DogDTO(
    var id: String?,
    @PropertyName("imageUrl") val imageUrl: String,
    @PropertyName("name") val name: String ,
    @PropertyName("raza") val raza: String ,
    @PropertyName("temperament") val temperament: String,
    @PropertyName("lifeExpectancy") val lifeExpectancy: String,
    @PropertyName("weightMale") val weightMale: String,
    @PropertyName("weightFemale") val weightFemale: String,
    @PropertyName("heightMale") val heightMale: String,
    @PropertyName("heightFemale") val heightFemale: String,
    @PropertyName("curiosities") val curiosities: String,
    @PropertyName("mlId") val mlId: String,
    @PropertyName("index") val index: Int,
){
    constructor() : this("","", "", "", "","","","",
        "","","","",0 )
}
