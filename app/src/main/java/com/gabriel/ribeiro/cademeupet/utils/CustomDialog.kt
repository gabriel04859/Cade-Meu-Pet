package com.gabriel.ribeiro.cademeupet.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.TAG

class CustomDialog(private val mActivity: Activity) {


     fun showDialogLoading(status : Boolean = true ) {
        val alert = AlertDialog.Builder(mActivity)
        val inflater = mActivity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_loading, null)
        alert.setView(view)
        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
         if (!status) {

             dialog.cancel()

         } else {
             dialog.show()
         }

    }

}