package com.gabriel.ribeiro.cademeupet.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.gabriel.ribeiro.cademeupet.model.Address
import com.gabriel.ribeiro.cademeupet.model.Animal
import com.gabriel.ribeiro.cademeupet.repository.contracts.NewPostRepository
import com.gabriel.ribeiro.cademeupet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(private val newPostRepository: NewPostRepository) : ViewModel() {
    private var _statusSavePost = MutableLiveData<Resource<Boolean>>()
    val statusSavePost : LiveData<Resource<Boolean>> get() = _statusSavePost

    fun createPost(imageUriList: ArrayList<Uri>, animal: Animal, address: Address, date: String, comment: String){
        viewModelScope.launch {

            _statusSavePost.value = newPostRepository.createPost(imageUriList,animal,address,date,comment).value


        }

    }
}