package com.gabriel.ribeiro.cademeupet.model

import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
data class Post(val idPost : String? = "",
                val idUser : String? = "",
                val address: Address? = null,
                val animal: Animal? = null,
                val date : String? = "",
                val comment : String? = "",
                val timestamp: Long? =  System.currentTimeMillis()


) : Parcelable {

}
