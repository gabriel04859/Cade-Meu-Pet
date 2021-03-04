package com.gabriel.ribeiro.cademeupet.ui.viewmodel

import androidx.lifecycle.*
import com.gabriel.ribeiro.cademeupet.model.Contact
import com.gabriel.ribeiro.cademeupet.model.Post
import com.gabriel.ribeiro.cademeupet.repository.MainRepository
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
@Suppress("UNCHECKED_CAST")
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val postList: MutableLiveData<Resource<MutableList<Post>>> = MutableLiveData()
    val postsOfCurrentUser: LiveData<Resource<MutableList<Post>>> get() = repository.postsCurrentUser
    val contacts: LiveData<Resource<MutableList<Contact>>> get() = repository.contactList

    init {
        getPostList()
    }

    private fun getPostList() {
        postList.postValue(Resource.Loading())
        repository.firebaseFirestore.addSnapshotListener { value, error ->
            if (error != null) {
                postList.postValue(Resource.Failure(error))
                return@addSnapshotListener
            }
            value?.let {
                postList.postValue(Resource.Success(it.toObjects(Post::class.java)))
            }
        }
    }


}