package com.gabriel.ribeiro.cademeupet.repository

import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.model.Post
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.POST_COLLECTION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileRepository(private val firebaseInstances : FirebaseInstances) {

    fun gePostsOfCurrentUser(){
        CoroutineScope(Dispatchers.IO).launch{
            try {
                val posts = firebaseInstances.getFirebaseFirestore().collection(POST_COLLECTION)
                        .whereEqualTo("idUser", FirebaseInstances.getFirebaseAuth().currentUser?.uid)
                        .get().await().toObjects(Post::class.java)

            }catch (e : Exception){

            }

        }

    }
}