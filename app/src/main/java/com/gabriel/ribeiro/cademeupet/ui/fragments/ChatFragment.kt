package com.gabriel.ribeiro.cademeupet.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.data.GetUser
import com.gabriel.ribeiro.cademeupet.databinding.FragmentChatBinding
import com.gabriel.ribeiro.cademeupet.model.User
import com.gabriel.ribeiro.cademeupet.repository.ChatRepository
import com.gabriel.ribeiro.cademeupet.ui.adatper.ChatAdapter
import com.gabriel.ribeiro.cademeupet.ui.viewmodel.ChatViewModel
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.Resource
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {
    private var _binding : FragmentChatBinding? = null
    private val binding : FragmentChatBinding get() = _binding!!

    private lateinit var userFrom : User
    private lateinit var userTo : User
    private lateinit var uidTo : String
    private val chatAdapter by lazy { ChatAdapter() }

    private val chatViewModel : ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireArguments().let {
            uidTo = it.getString("uidTo")!!
            getUsersToMessage()
        }

        binding.buttonSendMessage.setOnClickListener{
            sendMessage()
        }
    }

    private fun sendMessage() {
        val textMessage = binding.editTextSendMessage.text.toString().trim()
        if (textMessage.isBlank()){
            return
        }

        chatViewModel.sendMessage(userFrom,userTo,textMessage)
        binding.editTextSendMessage.text = null
        chatViewModel.statusSendMessage.observe(viewLifecycleOwner, Observer { resource ->
            when(resource){
                is Resource.Failure ->{
                    Log.i(Constants.TAG,"Erro enviar menssagem: ${resource.exception}")}
                is Resource.Success ->{
                    Log.i(Constants.TAG,"Menssagem enviada: ${resource.data}")



                }
            }

        })


    }

    private fun observerMessages() {
        chatViewModel.fetchMessages(FirebaseInstances.getFirebaseAuth().currentUser?.uid!!,uidTo)
            .observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success ->{
                    chatAdapter.setMessages(it.data!!)
                    chatAdapter.notifyDataSetChanged()
                    binding.recyclerViewChat.adapter = chatAdapter
                    binding.recyclerViewChat.scrollToPosition(chatAdapter.itemCount-1)
                }
            }
        })

    }

    override fun onStart() {
        observerMessages()
        super.onStart()
    }

    private fun getUsersToMessage(){
        GetUser.getCurrentUser(object : GetUser.CurrentUserFirestoreCallBack {
            override fun onGetCurrentUser(user: User) {
                userFrom = user

                Log.i(Constants.TAG,"User from: $userFrom")
            }

        })
        GetUser.getUser(uidTo, object : GetUser.UserFirestoreCallback{
            override fun onGetUser(user: User) {
                userTo = user
                Log.i(Constants.TAG,"User to: $userTo")
                user.apply {
                    Picasso.with(activity).load(imageProfile).into(binding.imageViewUserToChat)
                    binding.textViewTitleNameUserToChat.text = "$name $lastName"
                }
            }

        })

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}