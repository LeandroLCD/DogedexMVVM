package com.leandrolcd.dogedexmvvm.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dog(
    var id: String = "",
    val index: Int,
    val raza           : String = "",
    val heightFemale   : String = "",
    val heightMale     : String = "",
    val imageUrl       : String = "",
    val lifeExpectancy : String = "",
    val name           : String = "",
    val curiosities    : String = "",
    val temperament    : String = "",
    val weightFemale   : String = "",
    val weightMale     : String = "",
    val mlId           : String = "",
    var inCollection:Boolean = false

) : Parcelable, Comparable<Dog>{
    override fun compareTo(other: Dog): Int = if (this.mlId != other.mlId) 1 else -1


}
