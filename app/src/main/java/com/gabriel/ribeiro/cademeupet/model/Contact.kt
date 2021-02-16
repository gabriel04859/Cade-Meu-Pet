package com.gabriel.ribeiro.cademeupet.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(val uid : String = "", val name : String = "",
                   val lastMessage : String = "", val userPhoto : String = "", val timestamp : Long = 0) :
    Parcelable {
}