package com.gabriel.ribeiro.cademeupet.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.gabriel.ribeiro.cademeupet.repository.contracts.LoginAndRegisterRepository
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@Suppress("UNCHECKED_CAST")
class LoginAndRegisterViewModel @Inject constructor(private val loginAndRegisterRepository: LoginAndRegisterRepository)
    : ViewModel() {

    private var _status : MutableLiveData<Resource<FirebaseUser>> = MutableLiveData()
    val status : LiveData<Resource<FirebaseUser>> get() = _status

    private var _createUser : MutableLiveData<Resource<FirebaseUser>> = MutableLiveData()
    val createUser : LiveData<Resource<FirebaseUser>> get() = _createUser

    fun signUser(email : String, password :String) = viewModelScope.launch {
        _status.value = loginAndRegisterRepository.signInUser(email,password).value
    }

    fun createUser (name: String, lastName : String ,email: String, password: String,
                    phone: String, imageUri: Uri) = viewModelScope.launch {

        _createUser.value = loginAndRegisterRepository.createUser(name,lastName,email,password,phone, imageUri).value

    }

    class LoginAndRegisterViewModelFactory(private val loginAndRegisterRepository: LoginAndRegisterRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginAndRegisterViewModel(loginAndRegisterRepository) as T
        }
    }
}
