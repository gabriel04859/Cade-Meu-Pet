package com.gabriel.ribeiro.cademeupet.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.databinding.FragmentMessagesBinding
import com.gabriel.ribeiro.cademeupet.model.Contact
import com.gabriel.ribeiro.cademeupet.ui.activitys.PrincipalActivity
import com.gabriel.ribeiro.cademeupet.ui.adatper.LastMessageAdapter
import com.gabriel.ribeiro.cademeupet.ui.viewmodel.MainViewModel
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.TAG
import com.gabriel.ribeiro.cademeupet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagesFragment : Fragment(R.layout.fragment_messages), LastMessageAdapter.OnContactClickListener {
    private var _binding : FragmentMessagesBinding? = null
    private val binding : FragmentMessagesBinding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val lastMessageAdapter by lazy{
        LastMessageAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dividerItemDecoration = DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL)
        binding.recyclerViewLastMessages.apply {
           adapter = lastMessageAdapter
            addItemDecoration(dividerItemDecoration)
        }

        mainViewModel = (activity as PrincipalActivity).mainViewModel
        mainViewModel.contacts.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    Log.d(TAG, "onViewCreated: Loaging contacts")
                }
                is Resource.Success -> {
                    Log.i(TAG, "Last Messages: ${resource.data}")
                    if (resource.data?.isEmpty() == true) {
                        binding.textViewDontHaveMessages.visibility = View.VISIBLE
                    }
                    lastMessageAdapter.differ.submitList(resource.data)
                    lastMessageAdapter.notifyDataSetChanged()
                }
                is Resource.Failure -> {
                    Log.i(TAG, "Erro ao obter contatos: ${resource.exception}")
                }
            }
        })

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onContactClick(contact: Contact) {
        val uidToBundle = Bundle().apply {
            putString("uidTo",contact.uid)
        }
        findNavController().navigate(R.id.action_messagesFragment_to_chatFragment, uidToBundle)
    }

}