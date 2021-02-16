package com.gabriel.ribeiro.cademeupet.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.databinding.FragmentNewPostBinding
import com.gabriel.ribeiro.cademeupet.model.Address
import com.gabriel.ribeiro.cademeupet.model.Animal
import com.gabriel.ribeiro.cademeupet.repository.NewPostRepositoryImple
import com.gabriel.ribeiro.cademeupet.ui.activitys.PrincipalActivity
import com.gabriel.ribeiro.cademeupet.ui.viewmodel.NewPostViewModel
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.CustomToast
import com.gabriel.ribeiro.cademeupet.utils.OpenGalery
import com.gabriel.ribeiro.cademeupet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPostFragment : Fragment(R.layout.fragment_new_post) {
    private var _binding : FragmentNewPostBinding? = null
    private val binding : FragmentNewPostBinding get() = _binding!!

    private var imageUri : Uri? = null
    private var maleOrFemale = ""
    private var donateOrLostOrFinder = ""
    private var size = ""

    private lateinit var address: Address

    private val newPostViewModel: NewPostViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewPostBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireArguments().let {

            address = it.getParcelable("latLngAddress")!!
            Log.i("TESTE","$address")
            binding.textViewAddressNP.text = "${address.street} - ${address.city}"


        }

        observerStatusSavePost()

        binding.buttonChoiceAnimalImageNP.setOnClickListener{
            startActivityForResult(OpenGalery.openGalery(), Constants.PICK_IMAGE)
        }

        binding.textViewAddressNP.setOnClickListener {

        }

        binding.radioGroupSex.setOnCheckedChangeListener { _, i ->
            if (i == R.id.radioButtonMale){
                maleOrFemale = getString(R.string.macho)
            }
            if (i == R.id.radioButtonFemale){
                maleOrFemale = getString(R.string.femea)
            }
        }
        binding.radioGroupLostOrDonateOrFind.setOnCheckedChangeListener { radioGroup, i ->
            if (i == R.id.radioButtoDonate){
                donateOrLostOrFinder = getString(R.string.donate)
            }
            if (i == R.id.radioButtonLost){
                donateOrLostOrFinder = getString(R.string.lost)
            }
            if (i == R.id.radioButtonFinder){
                donateOrLostOrFinder = getString(R.string.finder)
            }
        }

        binding.seekBarPorte.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                size = when(p1){
                    1 -> {
                        getString(R.string.pequeno)
                    }
                    2 ->  {
                        getString(R.string.medio)
                    }
                    3 -> getString(R.string.grande)
                    else -> {
                        getString(R.string.indique_o_porte_abaixo)
                    }
                }
                binding.textViewSize.text = size
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}})

        binding.buttonPost.setOnClickListener {
            val name = binding.editTextNameNP.text.toString().trim()
            val date = binding.editTextDataNP.text.toString().trim()
            val comment = binding.editTextCommentNP.text.toString().trim()

            if (name.isEmpty() && date.isEmpty() && comment.isEmpty()){
                CustomToast.showToast(requireContext(),getString(R.string.preencha_todos_os_campos))
                return@setOnClickListener
            }

            if (maleOrFemale == ""){
                CustomToast.showToast(requireContext(),getString(R.string.indique_o_sexo))
                return@setOnClickListener
            }
            if (size == "" && size == getString(R.string.indique_o_porte_abaixo)){
                CustomToast.showToast(requireContext(),getString(R.string.indique_o_porte))
                return@setOnClickListener
            }

            if (donateOrLostOrFinder == ""){
                CustomToast.showToast(requireContext(),getString(R.string.indique_a_situacao_do_animal))
                return@setOnClickListener
            }
            if (imageUri == null){
                CustomToast.showToast(requireContext(),getString(R.string.selecione_uma_foto))
                return@setOnClickListener
            }

            val animal = Animal(name,getAnimalType()!!,maleOrFemale,size,donateOrLostOrFinder)
            Log.d(Constants.TAG, "onViewCreated: Animal: $animal")
            newPostViewModel.createPost(imageUri!!,animal,address,date,comment)

        }
        /*address = intent.extras?.getParcelable(Constants.ADDRESS_KEY)!!
        address.apply {
            binding.textViewAddressNP.text = "$street - $neighborhood - $city"
        }*/


    }

    private fun getAnimalType() : String?{
        val sharedPreferences = activity?.getSharedPreferences(Constants.SHARED_ANIMAL_KEY, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(Constants.ANIMAL_KEY,"")

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK && data?.data != null){
            imageUri = data.data
            binding.imageViewAnimalNP.setImageURI(imageUri)
            binding.buttonChoiceAnimalImageNP.alpha = 0F

        }
    }

    private fun observerStatusSavePost(){
        newPostViewModel.statusSavePost.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBarNewPost.visibility = View.VISIBLE
                    Log.i(Constants.TAG, "observerStatusSavePost: Loading...")

                }
                is Resource.Success -> {
                    findNavController().navigate(R.id.action_newPostFragment_to_homeFragment)
                    Log.i(Constants.TAG, "observerStatusSavePost: Salvo com sucesso")
                }
                is Resource.Failure -> {
                    binding.progressBarNewPost.visibility = View.GONE
                    CustomToast.showToast(requireContext(), getString(R.string.houve_um_erro))
                    Log.i(Constants.TAG, "Failure: ${it.exception}")
                    Log.i(Constants.TAG, "observerStatusSavePost: Erro ao salvar post: ${it.exception?.message}")
                }
            }
        })
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}