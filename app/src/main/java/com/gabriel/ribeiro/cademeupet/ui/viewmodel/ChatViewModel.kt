package com.gabriel.ribeiro.cademeupet.ui.viewmodel

import androidx.lifecycle.*
import com.gabriel.ribeiro.cademeupet.model.Message
import com.gabriel.ribeiro.cademeupet.model.User
import com.gabriel.ribeiro.cademeupet.repository.ChatRepository
import com.gabriel.ribeiro.cademeupet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@Suppress("UNCHECKED_CAST")
class ChatViewModel @Inject constructor(private val chatRepository: ChatRepository) : ViewModel() {
    private var _statusSendMessage : MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val statusSendMessage : LiveData<Resource<Boolean>> get() = _statusSendMessage

     fun sendMessage(userFrom : User, userTo : User, text : String) = viewModelScope.launch {
         _statusSendMessage.value = chatRepository.sendMessage(userFrom, userTo, text).value
     }

    fun fetchMessages(uidFrom : String, uidTo : String) : LiveData<Resource<MutableList<Message>>>{
       return chatRepository.fetchMessages(uidFrom, uidTo)
    }



    class ChatViewModelFactory(private val chatRepository: ChatRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ChatViewModel(chatRepository) as T
        }
    }

}