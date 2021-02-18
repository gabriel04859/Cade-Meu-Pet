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
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.TAG
import com.gabriel.ribeiro.cademeupet.utils.CustomToast
import com.gabriel.ribeiro.cademeupet.utils.OpenGalery
import com.gabriel.ribeiro.cademeupet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPostFragment : Fragment(R.layout.fragment_new_post), View.OnClickListener {
    private var _binding : FragmentNewPostBinding? = null
    private val binding : FragmentNewPostBinding get() = _binding!!

    private var imageUri1 : Uri? = null
    private var imageUri2 : Uri? = null
    private var imageUri3 : Uri? = null
    private var imageUri4 : Uri? = null

    private var maleOrFemale = ""
    private var donateOrLostOrFinder = ""
    private var size = ""
    private var imageUriList = ArrayList<Uri>()

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
            val addressString = "${address.street} - ${address.city}"
            binding.textViewAddressNP.text = addressString

            binding.includeButtonsPickImage.buttonChoiceAnimalImage01.setOnClickListener(this)
            binding.includeButtonsPickImage.buttonChoiceAnimalImage02.setOnClickListener(this)
            binding.includeButtonsPickImage.buttonChoiceAnimalImage03.setOnClickListener(this)
            binding.includeButtonsPickImage.buttonChoiceAnimalImage04.setOnClickListener(this)


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

            addImageUriToList()
            if (imageUriList.isEmpty()){
                CustomToast.showToast(requireContext(),getString(R.string.selecione_uma_foto))
                return@setOnClickListener
            }

            val animal = Animal(name,getAnimalType()!!,maleOrFemale,size,donateOrLostOrFinder)
            Log.d(Constants.TAG, "onViewCreated: Animal: $animal")
            newPostViewModel.createPost(imageUriList,animal,address,date,comment)
            
        }

    }

    private fun addImageUriToList() {
        if (imageUri1 != null){
            imageUriList.add(imageUri1!!)
        }
        if (imageUri2 != null){
            imageUriList.add(imageUri2!!)
        }
        if (imageUri3 != null){
            imageUriList.add(imageUri3!!)
        }
        if (imageUri4 != null){
            imageUriList.add(imageUri4!!)
        }
        Log.d(TAG, "onViewCreated: IMAGE URI: ${imageUriList.toString()}")

    }

    private fun getAnimalType() : String?{
        val sharedPreferences = activity?.getSharedPreferences(Constants.SHARED_ANIMAL_KEY, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(Constants.ANIMAL_KEY,"")

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == AppCompatActivity.RESULT_OK && data?.data != null){
            when(requestCode){
                Constants.PIC_IMAGE_01 -> {
                    imageUri1 = data.data
                    binding.includeButtonsPickImage.imageViewChoiceAnimalImage01.setImageURI(imageUri1)
                    binding.includeButtonsPickImage.buttonChoiceAnimalImage01.alpha = 0F
                }
                Constants.PIC_IMAGE_02 ->{
                    imageUri2 = data.data
                    binding.includeButtonsPickImage.imageViewChoiceAnimalImage02.setImageURI(imageUri2)
                    binding.includeButtonsPickImage.buttonChoiceAnimalImage02.alpha = 0F
                }
                Constants.PIC_IMAGE_03 ->{
                    imageUri3 = data.data
                    binding.includeButtonsPickImage.imageViewChoiceAnimalImage03.setImageURI(imageUri3)
                    binding.includeButtonsPickImage.buttonChoiceAnimalImage03.alpha = 0F
                }
                Constants.PIC_IMAGE_04 ->{
                    imageUri4 = data.data
                    binding.includeButtonsPickImage.imageViewChoiceAnimalImage04.setImageURI(imageUri4)
                    binding.includeButtonsPickImage.buttonChoiceAnimalImage04.alpha = 0F
                }
            }
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonChoiceAnimalImage01 ->  startActivityForResult(OpenGalery.openGalery(), Constants.PIC_IMAGE_01)
            R.id.buttonChoiceAnimalImage02 -> startActivityForResult(OpenGalery.openGalery(), Constants.PIC_IMAGE_02)
            R.id.buttonChoiceAnimalImage03 -> startActivityForResult(OpenGalery.openGalery(), Constants.PIC_IMAGE_03)
            R.id.buttonChoiceAnimalImage04 -> startActivityForResult(OpenGalery.openGalery(), Constants.PIC_IMAGE_04)
        }

    }

}