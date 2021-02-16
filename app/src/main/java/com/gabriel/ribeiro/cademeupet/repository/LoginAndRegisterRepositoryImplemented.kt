package com.gabriel.ribeiro.cademeupet.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.model.User
import com.gabriel.ribeiro.cademeupet.repository.contracts.LoginAndRegisterRepository
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.Resource
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class LoginAndRegisterRepositoryImplemented @Inject constructor(private val firebaseInstances : FirebaseInstances)
    : LoginAndRegisterRepository {
    private var _userAuthenticate : MutableLiveData<Resource<FirebaseUser>> = MutableLiveData()

    private var _createUser : MutableLiveData<Resource<FirebaseUser>> = MutableLiveData()
    private val fileName = UUID.randomUUID().toString()
    private val reference = firebaseInstances.getStorageReference(Constants.STORAGE_REFERENCE_USER).child(fileName)
    private val auth = firebaseInstances.getFirebaseAuth()

    override suspend fun signInUser(email: String, password: String): LiveData<Resource<FirebaseUser>> {
        try {
            _userAuthenticate.value = Resource.Loading()

            firebaseInstances.getFirebaseAuth().signInWithEmailAndPassword(email,password).await()
            _userAuthenticate.value = firebaseInstances.getFirebaseAuth().currentUser?.let {
                Log.d(Constants.TAG, "signInUser: $it")
                Resource.Success(it) }

        }catch (e : FirebaseAuthException){
            _userAuthenticate.value = Resource.Failure(e)
        }
        return _userAuthenticate

    }

    override suspend fun createUser(name: String, lastName: String, email: String, password: String,
                                    phone: String, imageUri: Uri): LiveData<Resource<FirebaseUser>> {

        _createUser.value = Resource.Loading()
        val imageUrl = reference.putFile(imageUri).await().storage.downloadUrl.await().toString()

        Log.i(Constants.TAG, "Image Url: $imageUrl")
        try {

            auth.createUserWithEmailAndPassword(email, password).await()
            val user = User(auth.uid, name,lastName,email, password, phone, imageUrl)

            firebaseInstances.getFirebaseFirestore().collection(Constants.USER_COLLECTION)
                    .document(FirebaseInstances.getCurrentUid()!!).set(user).await()

            _createUser.value = auth.currentUser?.let { Resource.Success(it) }!!


        } catch (e: FirebaseAuthException) {
            Log.i(Constants.TAG, "Erro: $e")
            _createUser.value = Resource.Failure(e)

        } catch (e: FirebaseAuthException) {
            Log.i(Constants.TAG, "Erro: $e")

            _createUser.value = Resource.Failure(e)

        } catch (e: Exception) {
            Log.i(Constants.TAG, "Erro: $e")

            _createUser.value = Resource.Failure(e)
        }

        return _createUser
    }
}