package com.gabriel.ribeiro.cademeupet.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.TAG

class CustomDialog(private val activity: Activity?) {


    fun showLoadingDialog(status : Boolean = true){
        val alert = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_loading,null)
        alert.setView(view)
        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        when {
            status -> {
                dialog.show()
            }
            else -> {
                dialog.cancel()
                Log.d(TAG, "showLoadingDialog: ENTROU NO CANCEL")
            }
        }

    }



}