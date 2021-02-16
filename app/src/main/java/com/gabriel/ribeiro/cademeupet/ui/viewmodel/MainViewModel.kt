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
    val postList : LiveData<Resource<MutableList<Post>>>  = repository.posts
    val postsOfCurrentUser : LiveData<Resource<MutableList<Post>>>  = repository.gePostsOfCurrentUser()
    val contacts :LiveData<Resource<MutableList<Contact>>> = repository.getLastMessages()

    class MainViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }
}