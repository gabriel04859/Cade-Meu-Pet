package com.gabriel.ribeiro.cademeupet.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.POST_COLLECTION
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class DeletePostDialog(private val activity : Activity) {

    fun showDeleteDialog(postId : String) {
        val alert = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_confirm_delete_post, null)
        alert.setView(view)
        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val textViewNoDeleteDialog = view.findViewById<TextView>(R.id.textViewNoDeleteDialog)
        val buttonConfirmDeleteDialog = view.findViewById<Button>(R.id.buttonConfirmDeleteDialog)

        textViewNoDeleteDialog.setOnClickListener {
            dialog.dismiss()
        }

        buttonConfirmDeleteDialog.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if (deletePost(postId) == null) {
                    dialog.dismiss()

                    Log.i(Constants.TAG, "showDeleteDialog: ${deletePost(postId)}")
                }
            }

        }
    }

    private suspend fun deletePost(postId: String): Exception? {
        var exception : Exception? = null
        try {
            FirebaseInstances
                    .getFirebaseFirestore()
                    .collection(POST_COLLECTION)
                    .document(postId).delete().await()
            /*val intent = Intent(activity.applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)*/


        } catch (e: Exception) {
            Log.i(Constants.TAG, "deletePost: Erro ao deleter post: ${e.message}")
            exception = e
        }
        return exception

    }




}