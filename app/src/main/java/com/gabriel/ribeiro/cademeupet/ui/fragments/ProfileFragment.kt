package com.gabriel.ribeiro.cademeupet.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.databinding.FragmentProfileBinding
import com.gabriel.ribeiro.cademeupet.model.Post
import com.gabriel.ribeiro.cademeupet.model.User
import com.gabriel.ribeiro.cademeupet.ui.activitys.PrincipalActivity
import com.gabriel.ribeiro.cademeupet.ui.adatper.ProfileAdapter
import com.gabriel.ribeiro.cademeupet.ui.viewmodel.MainViewModel
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.POST_KEY
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.TAG
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.USER_COLLECTION
import com.gabriel.ribeiro.cademeupet.utils.CustomToast
import com.gabriel.ribeiro.cademeupet.utils.Resource
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile), ProfileAdapter.OnPostProfileClickListener {
    private var _binding : FragmentProfileBinding? = null
    private val binding : FragmentProfileBinding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    private val profileAdapter = ProfileAdapter(this)

    private var menu : Menu? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButtonSettings.setOnClickListener {
            showPopUpUpdateOrSignOut(it)
        }
        CoroutineScope(Dispatchers.IO).launch{
            val user = FirebaseInstances.getFirebaseFirestore().collection(USER_COLLECTION).
            document(FirebaseInstances.getFirebaseAuth().currentUser!!.uid).get().await().toObject(
                User::class.java)
            withContext(Dispatchers.Main){
                user?.let{
                    Picasso.with(requireContext()).load(it.imageProfile).placeholder(R.drawable.ic_profile).into(binding.imageViewUserPhotoProfile)
                    val completeName = "${it.name} ${it.lastName}"
                    binding.textViewNameProfile.text = completeName
                    binding.textViewEmailProfile.text = it.email
                    binding.textViewPhoneProfile.text = it.phone
                    observerPosts()

                }

            }

        }
    }

    private fun showPopUpUpdateOrSignOut(view : View) {
        val popUp = PopupMenu(activity,view)
        popUp.inflate(R.menu.popup_profile)
        popUp.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.popupUpdateProfile -> {
                    CustomToast.showToast(requireContext(),"A fazer")
                    true
                }
                R.id.popupSignOutProfile -> {signOut()
                    true
                }

                else -> true
            }

        }
        popUp.show()
    }

    private fun signOut(){
        FirebaseInstances.getFirebaseAuth().signOut()
        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun observerPosts(){
        mainViewModel =  (activity as PrincipalActivity).mainViewModel
        mainViewModel.postsOfCurrentUser.observe(viewLifecycleOwner, Observer { resource ->
            when(resource){
                is Resource.Loading ->{Log.i(TAG, "Loading...")}
                is Resource.Failure ->{Log.i(TAG, "Erro profile: ${resource.exception}")}
                is Resource.Success ->{
                    resource.data?.let { setListToAdapter(it) }
                }
            }

        })

    }
    private fun setListToAdapter(posts : MutableList<Post>){
        if (posts.isEmpty()){
            binding.textViewDontHavePosts.visibility = View.VISIBLE
        }
        profileAdapter.getPosts(posts)
        binding.recyclerProfile.adapter = profileAdapter

    }

    override fun onPostClick(post: Post) {
        val postBundle = Bundle().apply {
            putParcelable(POST_KEY,post)
        }
        findNavController().navigate(R.id.action_profileFragment_to_detailFragment,postBundle)
    }


}