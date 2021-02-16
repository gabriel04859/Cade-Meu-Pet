package com.gabriel.ribeiro.cademeupet.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.model.Contact
import com.gabriel.ribeiro.cademeupet.model.Message
import com.gabriel.ribeiro.cademeupet.model.Post
import com.gabriel.ribeiro.cademeupet.model.User
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.CONTACTS
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.CONVERSATIONS_COLLECTION
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.LAST_MESSAGES
import com.gabriel.ribeiro.cademeupet.utils.Resource
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepository @Inject constructor(private val firebaseInstances : FirebaseInstances) {
    private val statusSendMessage : MutableLiveData<Resource<Boolean>> = MutableLiveData()
    private val messageList : MutableLiveData<Resource<MutableList<Message>>> = MutableLiveData()

    init {

    }
    suspend fun sendMessage(userFrom : User, userTo : User, text : String) : LiveData<Resource<Boolean>> {
        try {
            statusSendMessage.postValue(Resource.Loading())
            val timestamp = System.currentTimeMillis()
            val message = Message(text,timestamp,userFrom.uid,userTo.uid)
            firebaseInstances.getFirebaseFirestore().collection(CONVERSATIONS_COLLECTION)
                    .document(userFrom.uid!!)
                    .collection(userTo.uid!!)
                    .add(message).await()

            val contact = Contact(userTo.uid,userTo.name!!,text,userTo.imageProfile!!,message.timestamp!!)
            firebaseInstances.getFirebaseFirestore().collection(LAST_MESSAGES).document(userFrom.uid)
                    .collection(CONTACTS).document(userTo.uid).set(contact).await()

            firebaseInstances.getFirebaseFirestore().collection(CONVERSATIONS_COLLECTION)
                    .document(userTo.uid)
                    .collection(userFrom.uid)
                    .add(message).await()

            val contact2 = Contact(userFrom.uid,userFrom.name!!,text,userFrom.imageProfile!!,timestamp)
            firebaseInstances.getFirebaseFirestore().collection(LAST_MESSAGES).document(userTo.uid)
                    .collection(CONTACTS).document(userFrom.uid).set(contact2).await()
            statusSendMessage.postValue(Resource.Success(true))
            return statusSendMessage
        }catch (e : Exception){
            statusSendMessage.postValue(Resource.Failure(e))
            return statusSendMessage
        }
    }

    fun fetchMessages(uidFrom : String, uidTo : String) : LiveData<Resource<MutableList<Message>>>{
        val messages : MutableList<Message> = ArrayList()
        firebaseInstances.getFirebaseFirestore().collection(CONVERSATIONS_COLLECTION)
                .document(uidFrom).collection(uidTo)
                .orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener { value, error ->
                    value?.documentChanges.let {docs->
                        if (docs != null) {
                            for (doc in docs){
                                when(doc.type){
                                    DocumentChange.Type.ADDED->{
                                        val message = doc.document.toObject(Message::class.java)

                                        messages.add(message)
                                        Log.i("TESTE", "messages: $messages")

                                    }
                                }
                            }
                            messageList.value = Resource.Success(messages)
                        }
                    }
                }
        return messageList
    }

}