package com.gabriel.ribeiro.cademeupet.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.data.GetUser
import com.gabriel.ribeiro.cademeupet.databinding.FragmentDetailBinding
import com.gabriel.ribeiro.cademeupet.model.Post
import com.gabriel.ribeiro.cademeupet.model.User
import com.gabriel.ribeiro.cademeupet.ui.adatper.SlidePagerAdapter
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.POST_KEY
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.TAG
import com.gabriel.ribeiro.cademeupet.utils.DeletePostDialog
import com.squareup.picasso.Picasso

class DetailFragment : Fragment(R.layout.fragment_detail) {
    private var _binding : FragmentDetailBinding? = null
    private val binding : FragmentDetailBinding get()= _binding!!

    private val oberservPost = MutableLiveData<Post>()

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
            setValuesToWidgets(post)
        }
        binding.buttonOpenChat.setOnClickListener {
            val uidToBundle = Bundle().apply {
                putString("uidTo",uidTo)
            }
            findNavController().navigate(R.id.action_detailFragment_to_chatFragment,uidToBundle)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setValuesToWidgets(post: Post) {
        binding.progressBarDetails.visibility = View.VISIBLE
        post.apply {
            binding.textViewNameAnimalDetail.text = animal?.name
            binding.textViewSexDetail.text = "Sexo: ${animal?.sex}"
            binding.textViewSizeDetail.text = "Tamanho: ${animal?.size}"
            binding.textViewStatusAndDateDetail.text = "${animal?.status} em ${post.date}"
            binding.textViewAddressDetail.text = "${address?.street} - ${address?.city} " +
                    "- ${address?.neighborhood} - ${address?.cep}"
            binding.textViewCommentDetail.text = comment
            val imagesList : MutableList<String> = ArrayList()
            imagesList.addAll(post.animal?.images!!)
            Log.d(Constants.TAG, "setValuesToWidgets: Listade images ; $imagesList")
            val adapterPager = SlidePagerAdapter(imagesList)


            binding.viewPagerDetatail.adapter = adapterPager
            binding.circleIndicator.setViewPager(binding.viewPagerDetatail)
            if (idUser != FirebaseInstances.getFirebaseAuth().currentUser?.uid) {
                binding.buttonOpenChat.visibility = View.VISIBLE
                binding.buttonDeletePost.visibility = View.GONE
            }
            binding.progressBarDetails.visibility = View.GONE
            binding.buttonDeletePost.setOnClickListener {
                deletePostDialog?.showDeleteDialog(idPost!!)
            }
        }

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