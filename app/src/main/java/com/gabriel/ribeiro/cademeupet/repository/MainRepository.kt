package com.gabriel.ribeiro.cademeupet.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import javax.inject.Inject

class MainRepository @Inject constructor(private val firebaseInstance: FirebaseInstances)  {
    val posts : MutableLiveData<Resource<MutableList<Post>>> = MutableLiveData()
    val contactList : MutableLiveData<Resource<MutableList<Contact>>> = MutableLiveData()
    val postsCurrentUser : MutableLiveData<Resource<MutableList<Post>>> = MutableLiveData()
    init {
        gePostsOfCurrentUser()
        getLastMessages()
        getPostList()
    }

    private fun getPostList(){
        posts.postValue(Resource.Loading())
        firebaseInstance.getFirebaseFirestore().collection(POST_COLLECTION)
                .addSnapshotListener { value, error ->
                    if (error != null){
                        posts.postValue(Resource.Failure(error))
                        return@addSnapshotListener
                    }
                    value?.let { posts.postValue(Resource.Success(it.toObjects(Post::class.java)))
                    }
                }

    }

    private fun getLastMessages() {
        contactList.postValue(Resource.Loading())
        firebaseInstance.getFirebaseAuth().currentUser?.let { firebaseUser ->
            firebaseInstance.getFirebaseFirestore().collection(LAST_MESSAGES)
                    .document(firebaseUser.uid).collection(CONTACTS)
                    .addSnapshotListener { value, error ->
                        if (error != null){
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
        firebaseInstance.getFirebaseFirestore()
            .collection(Constants.POST_COLLECTION)
            .whereEqualTo("idUser", firebaseInstance.getFirebaseAuth().currentUser?.uid)
            .addSnapshotListener { value, error ->
                posts.value = Resource.Loading()
                if (error != null){
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