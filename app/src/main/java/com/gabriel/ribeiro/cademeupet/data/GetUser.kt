package com.gabriel.ribeiro.cademeupet.data

import com.gabriel.ribeiro.cademeupet.model.User
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.USER_COLLECTION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object GetUser {
    fun getUser(uid : String, userFirestoreCallback: UserFirestoreCallback) = CoroutineScope(Dispatchers.Main).launch {
        val documentSnapshot = FirebaseInstances.getFirebaseFirestore().collection(USER_COLLECTION).document(uid).get().await()
        val user = documentSnapshot.toObject(User::class.java)
        user?.let {
            userFirestoreCallback.onGetUser(user)
        }

    }



    fun getCurrentUser(currentUserFirestoreCallBack : CurrentUserFirestoreCallBack) = CoroutineScope(Dispatchers.Main).launch {
        FirebaseInstances.getFirebaseAuth().currentUser?.uid?.let {uid ->
            val documentSnapshot = FirebaseInstances.getFirebaseFirestore().collection(USER_COLLECTION)
                .document(uid).get().await()
            val user = documentSnapshot.toObject(User::class.java)
            user?.let { currentUser ->
                currentUserFirestoreCallBack.onGetCurrentUser(currentUser)

            }

        }
    }

    interface CurrentUserFirestoreCallBack{
        fun onGetCurrentUser(user : User)
    }

    interface UserFirestoreCallback{
        fun onGetUser(user : User)
    }
}