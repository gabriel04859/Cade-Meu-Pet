package com.gabriel.ribeiro.cademeupet.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.model.Contact
import com.gabriel.ribeiro.cademeupet.model.Post
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.CONTACTS
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.LAST_MESSAGES
import com.gabriel.ribeiro.cademeupet.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MainRepository @Inject constructor(private val firebaseInstance: FirebaseInstances)  {
    val posts : MutableLiveData<Resource<MutableList<Post>>> = MutableLiveData()
    private val contactList : MutableLiveData<Resource<MutableList<Contact>>> = MutableLiveData()
    private val postsCurrentUser : MutableLiveData<Resource<MutableList<Post>>> = MutableLiveData()
    init {
        getAllPosts()
    }
    private fun getAllPosts() {
        firebaseInstance.getFirebaseFirestore()
                .collection(Constants.POST_COLLECTION)
                .addSnapshotListener { value, error ->
                    posts.value = Resource.Loading()
                    if (error != null){
                        posts.value = Resource.Failure(error)
                        return@addSnapshotListener
                    }
                    value?.let {
                        posts.value = Resource.Success(it.toObjects(Post::class.java))
                    }
                }
    }

    fun gePostsOfCurrentUser() : MutableLiveData<Resource<MutableList<Post>>>{
        CoroutineScope(Dispatchers.IO).launch{
            try {
                postsCurrentUser.postValue(Resource.Loading())
                val posts = firebaseInstance.getFirebaseFirestore().collection(Constants.POST_COLLECTION)
                        .whereEqualTo("idUser", firebaseInstance.getFirebaseAuth().currentUser?.uid)
                        .get().await().toObjects(Post::class.java)
                postsCurrentUser.postValue(Resource.Success(posts))

            }catch (e : Exception){
                postsCurrentUser.postValue(Resource.Failure(e))
            }
        }
        return postsCurrentUser
    }

     fun getLastMessages() : LiveData<Resource<MutableList<Contact>>>{
        CoroutineScope(Dispatchers.Main).launch {
            try {
                contactList.postValue(Resource.Loading())
                firebaseInstance.getFirebaseAuth().currentUser?.let { firebaseUser ->
                    val contacts = firebaseInstance.getFirebaseFirestore().collection(LAST_MESSAGES)
                            .document(firebaseUser.uid).collection(CONTACTS).get().await().toObjects(Contact::class.java)
                    contactList.postValue(Resource.Success(contacts))
                }
            }catch (e : Exception){
                contactList.postValue(Resource.Failure(e))
            }
        }
         return contactList
     }
}