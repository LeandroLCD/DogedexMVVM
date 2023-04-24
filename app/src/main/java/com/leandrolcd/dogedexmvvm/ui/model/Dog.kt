package com.leandrolcd.dogedexmvvm.ui.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName

import kotlinx.parcelize.Parcelize

@Parcelize
data class Dog(
    @SerializedName("mlId")
    var mlId: String = "",
    @SerializedName("curiosities")
    val curiosities: String = "",
    @SerializedName("curiositiesEs")
    val curiositiesEs: String = "",
    @SerializedName("heightFemale")
    val heightFemale: String = "",
    @SerializedName("heightMale")
    val heightMale: String = "",
    @SerializedName("heightFemaleEs")
    val heightFemaleEs: String = "",
    @SerializedName("heightMaleEs")
    val heightMaleEs: String = "",
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("index")
    val index: Int,
    @SerializedName("lifeExpectancy")
    val lifeExpectancy: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("race")
    val race: String = "",
    @SerializedName("raceEs")
    val raceEs: String = "",
    @SerializedName("temperament")
    val temperament: String = "",
    @SerializedName("temperamentEs")
    val temperamentEs: String = "",
    @SerializedName("weightFemale")
    val weightFemale: String = "",
    @SerializedName("weightMale")
    val weightMale: String = "",
    @SerializedName("weightFemaleEs")
    val weightFemaleEs: String = "",
    @SerializedName("weightMaleEs")
    val weightMaleEs: String = "",
    @Exclude
    var inCollection:Boolean = false,
    @Exclude
    var confidence:Float = 0f

) : Parcelable, Comparable<Dog>{
    override fun compareTo(other: Dog): Int = if (this.mlId != other.mlId) 1 else -1


}
