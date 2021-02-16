package com.gabriel.ribeiro.cademeupet.utils

import android.content.Intent

class OpenGalery {
    companion object{
        fun openGalery() : Intent{
            val intentOpen = Intent(Intent.ACTION_PICK)
            intentOpen.type = "image/*"
            return intentOpen
        }
    }

}