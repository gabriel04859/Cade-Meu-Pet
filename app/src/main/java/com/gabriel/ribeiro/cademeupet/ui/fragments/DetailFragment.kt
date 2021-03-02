package com.gabriel.ribeiro.cademeupet.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.data.GetUser
import com.gabriel.ribeiro.cademeupet.databinding.FragmentDetailBinding
import com.gabriel.ribeiro.cademeupet.model.Address
import com.gabriel.ribeiro.cademeupet.model.Animal
import com.gabriel.ribeiro.cademeupet.model.Post
import com.gabriel.ribeiro.cademeupet.model.User
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.POST_KEY
import com.gabriel.ribeiro.cademeupet.utils.DeletePostDialog
import com.squareup.picasso.Picasso

class DetailFragment : Fragment(R.layout.fragment_detail) {
    private var _binding : FragmentDetailBinding? = null
    private val binding : FragmentDetailBinding get()= _binding!!

    private lateinit var post : Post
    private lateinit var uidTo : String
    private val deletePostDialog by lazy {
        activity?.let {
            DeletePostDialog(it)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireArguments().let {
            post = it.getParcelable(POST_KEY)!!
            val statusAndDate = "${post.animal?.status} em ${post.date}"
            binding.textViewCommentDetail.text = post.comment
            binding.textViewStatusAndDateDetail.text = statusAndDate
            post.animal?.let { animal ->  getValuesFromAnimal(animal)}
            post.address?.let{ address -> getValuesFromAddress(address)}
            showButtons(post)
            getValuesFromUser(post)

        }
        binding.buttonOpenChat.setOnClickListener {
            val uidToBundle = Bundle().apply {
                putString("uidTo",uidTo)
            }
            findNavController().navigate(R.id.action_detailFragment_to_chatFragment,uidToBundle)
        }

    }

    private fun showButtons(post: Post) {
        post.apply {
            if (idUser != FirebaseInstances.getFirebaseAuth().currentUser?.uid) {
                binding.buttonOpenChat.visibility = View.VISIBLE
                binding.buttonDeletePost.visibility = View.GONE
            }
            binding.buttonDeletePost.setOnClickListener {
                deletePostDialog?.showDeleteDialog(idPost!!)
            }
        }

    }

    private fun getValuesFromAddress(address: Address) {
            val addressText = "${address.street} - ${address.city} " + "- ${address.neighborhood} - ${address.cep}"
            binding.textViewAddressDetail.text = addressText
    }

    private fun getValuesFromAnimal(animal: Animal) {
        binding.textViewNameAnimalDetail.text = animal.name
        val sex = "Sexo: ${animal.sex}"
        binding.textViewSexDetail.text = sex
        val size = "Tamanho ${animal.size}"
        binding.textViewSizeDetail.text = size
        Picasso.with(requireContext()).load(animal.imageUrl)
                .placeholder(R.drawable.d_placeholder).into(binding.imageViewPostDetail)

    }


    private fun getValuesFromUser(post: Post) {
        GetUser.getUser(post.idUser!!, object : GetUser.UserFirestoreCallback {
            override fun onGetUser(user: User) {
                user.apply {
                    if (uid != null) {
                        uidTo = uid
                    }
                    Picasso.with(activity).load(imageProfile).into(binding.imageViewUserPhotoDetail)
                    binding.textViewNameUserDetail.text = name
                    binding.textViewPhoneDetail.text = phone
                    binding.textViewEmailUserDetail.text = email
                }
            }
        })
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}