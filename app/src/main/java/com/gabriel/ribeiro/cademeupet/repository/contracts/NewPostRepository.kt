package com.gabriel.ribeiro.cademeupet.repository.contracts

import android.net.Uri
import androidx.lifecycle.LiveData
import com.gabriel.ribeiro.cademeupet.model.Address
import com.gabriel.ribeiro.cademeupet.model.Animal
import com.gabriel.ribeiro.cademeupet.utils.Resource
import java.lang.Exception

interface NewPostRepository {
    suspend fun createPost(imageUri : Uri, animal: Animal, address: Address, date: String, comment: String ) : LiveData<Resource<Boolean>>
}