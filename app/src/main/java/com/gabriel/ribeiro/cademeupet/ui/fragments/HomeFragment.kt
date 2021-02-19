package com.gabriel.ribeiro.cademeupet.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.databinding.FragmentHomeBinding
import com.gabriel.ribeiro.cademeupet.model.Post
import com.gabriel.ribeiro.cademeupet.ui.activitys.PrincipalActivity
import com.gabriel.ribeiro.cademeupet.ui.adatper.FeedAdapter
import com.gabriel.ribeiro.cademeupet.ui.viewmodel.MainViewModel
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.ANIMAL_KEY
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.POST_KEY
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.SHARED_ANIMAL_KEY
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.TAG
import com.gabriel.ribeiro.cademeupet.utils.CustomDialog
import com.gabriel.ribeiro.cademeupet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), View.OnClickListener, FeedAdapter.OnPostClickListener {
    private var _binding : FragmentHomeBinding? = null
    private val binding : FragmentHomeBinding get() = _binding!!

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.roteta_open_anim) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_closed_anim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim) }
    private var clicked = false

    private lateinit var mainViewModel: MainViewModel
    private val feedAdapter by lazy {
        FeedAdapter(this)
    }
    private val customDialog by lazy{
        CustomDialog(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.floatingActionButtonDog.setOnClickListener(this)
        binding.floatingActionButtonCat.setOnClickListener(this)
        binding.floatingActionButtonAdd.setOnClickListener(this)
        binding.recyclerViewHome.adapter = feedAdapter

        mainViewModel = (activity as PrincipalActivity).mainViewModel


        mainViewModel.postList.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    Log.d(TAG, "onViewCreated: loading...")
                }
                is Resource.Failure -> {
                    Log.i(TAG, "Erro ao obter os posts: ${resource.exception}")
                }
                is Resource.Success -> {
                    Log.d(TAG, "onViewCreated: Seccess")
                    setDataToAdapter(resource.data)
                }

            }
        })
    }

    private fun setDataToAdapter(data: MutableList<Post>?) {
        try {
            Log.i(TAG,"List fragment: ${data}")
            feedAdapter.differ.submitList(data)
            feedAdapter.notifyDataSetChanged()

        }catch (e : Exception){
            Log.i(TAG, "Erro : ${e.message}" )
        }
    }

    private fun addButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        isClickable(clicked)
        clicked = !clicked
    }

    private fun isClickable(clicked: Boolean){
        if (!clicked){
            binding.floatingActionButtonCat.isClickable = true
            binding.floatingActionButtonDog.isClickable = true
        }else{
            binding.floatingActionButtonCat.isClickable = false
            binding.floatingActionButtonDog.isClickable = false
        }
    }
    private fun setVisibility(clicked : Boolean) {
        if (!clicked){
            binding.floatingActionButtonCat.visibility = View.VISIBLE
            binding.floatingActionButtonDog.visibility = View.VISIBLE
        }else{
            binding.floatingActionButtonCat.visibility = View.INVISIBLE
            binding.floatingActionButtonDog.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {

        if (!clicked){
            binding.floatingActionButtonCat.startAnimation(fromBottom)
            binding.floatingActionButtonDog.startAnimation(fromBottom)
            binding.floatingActionButtonAdd.startAnimation(rotateOpen)
        }else{
            binding.floatingActionButtonCat.startAnimation(toBottom)
            binding.floatingActionButtonDog.startAnimation(toBottom)
            binding.floatingActionButtonAdd.startAnimation(rotateClose)
        }
    }

    private fun choiceAnimalType(v: View) {
        val sharedPreferences = requireContext().getSharedPreferences(SHARED_ANIMAL_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        when(v.id){
            R.id.floatingActionButtonDog -> {
                editor.putString(ANIMAL_KEY,getString(R.string.dog))
                findNavController().navigate(R.id.action_homeFragment_to_pickAddressMapsFragment)

            }
            R.id.floatingActionButtonCat -> {
                editor.putString(ANIMAL_KEY,getString(R.string.cat))
                findNavController().navigate(R.id.action_homeFragment_to_pickAddressMapsFragment)
            }
        }
        editor.apply()
    }
    override fun onClick(v: View?) {
        v?.let {
            choiceAnimalType(it)
            if(it.id == R.id.floatingActionButtonAdd){
                addButtonClicked()

            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onPostClick(post: Post) {
        val postBundle = Bundle().apply {
            putParcelable(POST_KEY,post)
        }
        findNavController().navigate(R.id.action_homeFragment_to_detailFragment,postBundle)

    }



}
