package com.gabriel.ribeiro.cademeupet.utils

import android.content.Context
import com.gabriel.ribeiro.cademeupet.R
import com.muddzdev.styleabletoast.StyleableToast

object CustomToast {
    fun showToast(context : Context,message : String){
        StyleableToast.makeText(context,message, R.style.customToast).show()
    }
}