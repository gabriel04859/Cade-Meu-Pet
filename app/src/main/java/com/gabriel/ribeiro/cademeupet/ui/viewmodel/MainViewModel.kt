package com.gabriel.ribeiro.cademeupet.ui.viewmodel

import androidx.lifecycle.*
import com.gabriel.ribeiro.cademeupet.model.Contact
import com.gabriel.ribeiro.cademeupet.model.Post
import com.gabriel.ribeiro.cademeupet.repository.MainRepository
import com.gabriel.ribeiro.cademeupet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
@Suppress("UNCHECKED_CAST")
class MainViewModel @Inject constructor(private val repository : MainRepository) : ViewModel() {


    val postList : LiveData<Resource<MutableList<Post>>> get() = repository.posts
    val postsOfCurrentUser : LiveData<Resource<MutableList<Post>>> get() =   repository.postsCurrentUser
    val contacts :LiveData<Resource<MutableList<Contact>>> get()= repository.contactList


}