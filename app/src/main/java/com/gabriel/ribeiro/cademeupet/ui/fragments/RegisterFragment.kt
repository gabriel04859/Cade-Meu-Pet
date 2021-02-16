package com.gabriel.ribeiro.cademeupet.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.databinding.FragmentRegisterBinding
import com.gabriel.ribeiro.cademeupet.repository.LoginAndRegisterRepositoryImplemented
import com.gabriel.ribeiro.cademeupet.ui.viewmodel.LoginAndRegisterViewModel
import com.gabriel.ribeiro.cademeupet.utils.*
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding : FragmentRegisterBinding get() = _binding!!
    private var imageUri : Uri? = null

    private val loginAndRegisterViewModel : LoginAndRegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonOpenGalery.setOnClickListener {
            startActivityForResult(OpenGalery.openGalery(), Constants.PICK_IMAGE)
        }

        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextNameRegister.text.toString().trim()
            val lastName = binding.editTextLastNameRegister.text.toString().trim()
            val email = binding.editTextEmailRegister.text.toString().trim()
            val password = binding.editTextPasswordRegister.text.toString().trim()
            val passwordConfirm = binding.editTextPasswordConfirmRegister.text.toString().trim()
            val phone = binding.editTextPhoneRegister.text.toString().trim()
            if(name.isEmpty() ||lastName.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() ||phone.isEmpty() ){
                CustomToast.showToast(requireContext(), getString(R.string.preencha_todos_os_campos))
                return@setOnClickListener
            }
            if(password != passwordConfirm){
                CustomToast.showToast(requireContext(), getString(R.string.senhas_diferentes))
                return@setOnClickListener
            }
            loginAndRegisterViewModel.createUser(name,lastName,email,password,phone,imageUri!!)
        }

        loginAndRegisterViewModel.createUser.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.i(Constants.TAG, "onCreate: Loading...")
                }
                is Resource.Success -> {
                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                }
                is Resource.Failure -> {
                    getExceptions(it.exception)
                }
            }
        })
    }

    private fun getExceptions(exception: Exception?) {
        when(exception){
            is FirebaseAuthWeakPasswordException ->{
                CustomToast.showToast(requireContext(),getString(R.string.password_min))
                return
            }
            is FirebaseAuthInvalidCredentialsException -> {
                CustomToast.showToast(requireContext(),getString(R.string.email_invalido))
                return
            }
            is FirebaseAuthUserCollisionException -> {
                CustomToast.showToast(requireContext(),getString(R.string.usuario_cadastrado))
                return
            }
            else -> {
                CustomToast.showToast(requireContext(),getString(R.string.houve_um_erro))
                Log.i("TESTE", "Erro ao cadastrar usuario: $exception")
                return
            }

        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == Constants.PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK && data.data != null){
                imageUri = data.data
                binding.imageViewUserPhotoRegister.setImageURI(imageUri)
                binding.buttonOpenGalery.alpha = 0F

            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}