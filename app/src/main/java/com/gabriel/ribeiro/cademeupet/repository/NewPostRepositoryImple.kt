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
    override suspend fun createPost(imageUriList: ArrayList<Uri>, animal: Animal, address: Address, date: String, comment: String)
    : LiveData<Resource<Boolean>> {
        val imageUrls = ArrayList<String>()
        val fileName = UUID.randomUUID().toString()
        val reference = firebaseInstance.getStorageReference(Constants.STORAGE_REFERENCE_ANIMAL).child(fileName)
        try{
            val db = firebaseInstance.getFirebaseFirestore().collection(Constants.POST_COLLECTION).document()
            for(imageUri in imageUriList){
                val imagePost = reference.putFile(imageUri).await().storage.downloadUrl.await().toString()
                imageUrls.add(imagePost)
                Log.d(TAG, "createPost: imageURl: $imagePost")
            }
            Log.d(TAG, "createPost: List images: $imageUrls")
            animal.images = imageUrls
            val post = Post(db.id,FirebaseInstances.getFirebaseAuth().currentUser!!.uid,address,animal,date,comment)
            db.set(post).await()
            Log.d(TAG, "createPost: Post salvo: $post")

            _statusSavePost.value = Resource.Success(true)
            Log.i(Constants.TAG, "createPost: valor ${statusSavePost.value} ")


        }catch (e : Exception){
            _statusSavePost.postValue(Resource.Failure(e))
        }
        Log.i(Constants.TAG, "createPost: retorno ${statusSavePost.value} ")
        return statusSavePost

    }

}
