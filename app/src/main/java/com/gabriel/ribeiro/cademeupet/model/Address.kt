package com.gabriel.ribeiro.cademeupet.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(val street : String? = "",
                   val neighborhood : String? = "",
                   val city : String? = "",
                   val cep : String? = "",
                   val latitude : Double? = 0.0,
                   val longitude : Double? = 0.0

) : Parcelable {
}
