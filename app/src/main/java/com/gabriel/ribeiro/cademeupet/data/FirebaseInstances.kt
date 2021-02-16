package com.gabriel.ribeiro.cademeupet.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FirebaseInstances {
    private var firebaseAuth : FirebaseAuth? = null
    private var firebaseFirestore : FirebaseFirestore? = null
    private var storageReference : StorageReference? = null
    private val auth = getFirebaseAuth()

    fun getFirebaseAuth() : FirebaseAuth{
        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance()

        }
        return firebaseAuth as FirebaseAuth
    }

    fun getFirebaseFirestore() : FirebaseFirestore{
        if (firebaseFirestore == null){
            firebaseFirestore = FirebaseFirestore.getInstance()
        }
        return firebaseFirestore as FirebaseFirestore
    }

    fun getStorageReference(reference : String) : StorageReference{
        if(storageReference == null){
            storageReference = FirebaseStorage.getInstance().getReference(reference)
        }
        return storageReference as StorageReference
    }

    fun getCurrentUid() : String?{
        return auth.currentUser?.uid
    }

}