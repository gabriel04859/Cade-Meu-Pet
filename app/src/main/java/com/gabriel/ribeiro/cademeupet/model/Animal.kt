package com.gabriel.ribeiro.cademeupet.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Animal(val name : String? = "",
                  val type : String? = "",
                  val sex : String? = "",
                  val size : String? = "",
                  val status :String? = "",
                  var imageAnimal : String? = "") : Parcelable {
}