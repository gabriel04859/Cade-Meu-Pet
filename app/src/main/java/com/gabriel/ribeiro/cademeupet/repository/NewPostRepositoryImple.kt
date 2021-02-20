package com.gabriel.ribeiro.cademeupet.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.model.Address
import com.gabriel.ribeiro.cademeupet.model.Animal
import com.gabriel.ribeiro.cademeupet.model.Post
import com.gabriel.ribeiro.cademeupet.repository.contracts.NewPostRepository
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.TAG
import com.gabriel.ribeiro.cademeupet.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class NewPostRepositoryImple @Inject constructor(private val firebaseInstance: FirebaseInstances) : NewPostRepository {
    private var _statusSavePost = MutableLiveData<Resource<Boolean>>()
    private val statusSavePost : LiveData<Resource<Boolean>> get() = _statusSavePost

    private val fileName = UUID.randomUUID().toString()
    private val reference = firebaseInstance.getStorageReference(Constants.STORAGE_REFERENCE_ANIMAL).child(fileName)

    override suspend fun createPost(imageUriList: ArrayList<Uri>, animal: Animal, address: Address, date: String, comment: String)
    : LiveData<Resource<Boolean>> {
        _statusSavePost.value = Resource.Loading()
        try{
            val db = firebaseInstance.getFirebaseFirestore().collection(Constants.POST_COLLECTION).document()
            animal.images = handlerImage(imageUriList)
            val post = Post(db.id,FirebaseInstances.getFirebaseAuth().currentUser!!.uid,address,animal,date,comment)
            db.set(post).await()

            _statusSavePost.value = Resource.Success(true)
            Log.i(TAG, "createPost: valor ${statusSavePost.value} ")


        }catch (e : Exception){
            _statusSavePost.value = Resource.Failure(e)
        }
        Log.i(TAG, "createPost: retorno ${statusSavePost.value} ")
        return statusSavePost

    }

    private suspend fun handlerImage(imageUriList: ArrayList<Uri>) : ArrayList<String>{
        return withContext(Dispatchers.Main){
            val imageUrls = ArrayList<String>()
            _statusSavePost.value = Resource.Loading()
            Log.d(TAG, "handlerImage: ImagesUri: $imageUriList")

            for(imageUri in imageUriList){
                val imagePost = reference.putFile(imageUri).await().storage.downloadUrl.await().toString()
                imageUrls.add(imagePost)
                Log.d(TAG, "createPost: imageURl: $imagePost")
            }
            Log.d(TAG, "createPost: List images: $imageUrls")
            imageUrls

        }
    }

}
