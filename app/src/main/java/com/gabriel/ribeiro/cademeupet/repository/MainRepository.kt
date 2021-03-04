package com.gabriel.ribeiro.cademeupet.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.model.Contact
import com.gabriel.ribeiro.cademeupet.model.Post
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.CONTACTS
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.LAST_MESSAGES
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.POST_COLLECTION
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.TAG
import com.gabriel.ribeiro.cademeupet.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepository @Inject constructor(private val firebaseInstance: FirebaseInstances) {

    val contactList: MutableLiveData<Resource<MutableList<Contact>>> = MutableLiveData()
    val postsCurrentUser: MutableLiveData<Resource<MutableList<Post>>> = MutableLiveData()
    val firebaseFirestore = firebaseInstance.getFirebaseFirestore().collection(POST_COLLECTION)
    private val firebaseUser = firebaseInstance.getFirebaseAuth().currentUser

    init {
        gePostsOfCurrentUser()
        getLastMessages()
    }


    private fun getLastMessages() {
        contactList.postValue(Resource.Loading())
        firebaseInstance.getFirebaseAuth().currentUser?.let { firebaseUser ->
            firebaseInstance.getFirebaseFirestore().collection(LAST_MESSAGES)
                    .document(firebaseUser.uid).collection(CONTACTS)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            contactList.postValue(Resource.Failure(error))
                            return@addSnapshotListener
                        }
                        value?.let {
                            contactList.postValue(Resource.Success(it.toObjects(Contact::class.java)))
                        }

                    }
        }

    }

    private fun gePostsOfCurrentUser() {
        firebaseFirestore.whereEqualTo("idUser", firebaseUser?.uid)
                .addSnapshotListener { value, error ->
                    postsCurrentUser.value = Resource.Loading()
                    if (error != null) {
                        postsCurrentUser.value = Resource.Failure(error)
                        return@addSnapshotListener
                    }
                    value?.let {
                        postsCurrentUser.postValue(Resource.Success(it.toObjects(Post::class.java)))
                        Log.d(TAG, "gePostsOfCurrentUser: ${postsCurrentUser.value}")
                    }
                }
    }

}