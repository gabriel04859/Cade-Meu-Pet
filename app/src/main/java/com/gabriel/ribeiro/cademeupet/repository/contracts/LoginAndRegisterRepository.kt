package com.gabriel.ribeiro.cademeupet.repository.contracts

import android.net.Uri
import androidx.lifecycle.LiveData
import com.gabriel.ribeiro.cademeupet.utils.Resource
import com.google.firebase.auth.FirebaseUser

interface LoginAndRegisterRepository {

    suspend fun signInUser(email : String, password : String) : LiveData<Resource<FirebaseUser>>

    suspend fun createUser(name: String, lastName : String ,email: String, password: String,
                           phone: String, imageUri: Uri) : LiveData<Resource<FirebaseUser>>

}