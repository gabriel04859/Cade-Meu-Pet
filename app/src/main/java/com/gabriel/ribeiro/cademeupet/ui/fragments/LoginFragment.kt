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
import androidx.navigation.fragment.findNavController
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.databinding.FragmentLoginBinding
import com.gabriel.ribeiro.cademeupet.repository.LoginAndRegisterRepositoryImplemented
import com.gabriel.ribeiro.cademeupet.ui.activitys.PrincipalActivity
import com.gabriel.ribeiro.cademeupet.ui.viewmodel.LoginAndRegisterViewModel
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.CustomDialog
import com.gabriel.ribeiro.cademeupet.utils.CustomToast
import com.gabriel.ribeiro.cademeupet.utils.Resource
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding : FragmentLoginBinding? = null
    private val binding : FragmentLoginBinding get() = _binding!!
    lateinit var mainActivity : PrincipalActivity

    private val loginAndRegisterViewModel : LoginAndRegisterViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = (activity as PrincipalActivity)

        binding.buttonRegisterHere.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.buttonLogin.setOnClickListener{
            val email = binding.editTextEmailLogin.text.toString().trim()
            val password = binding.editTextPasswordLogin.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()){
                CustomToast.showToast(requireContext(),getString(R.string.preencha_todos_os_campos))
                return@setOnClickListener
            }
            mainActivity.showProgressBar()
            loginAndRegisterViewModel.signUser(email,password)

        }

        loginAndRegisterViewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {

                    Log.i(Constants.TAG, "onCreate: Loading... ")
                    mainActivity.showProgressBar()

                }
                is Resource.Failure -> {
                    Log.i(Constants.TAG, "onCreate: Erro ao logar: ${it.exception?.message}")
                    mainActivity.dismissProgressBar()
                    CustomDialog(activity).showLoadingDialog(false)
                    getExceptionsSignUser(it.exception)
                }

                is Resource.Success -> {
                    mainActivity.dismissProgressBar()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        })
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        authenticationUser()
    }

    private fun getExceptionsSignUser(exception: Exception?){
        when (exception) {
            is FirebaseAuthInvalidUserException -> {
                CustomToast.showToast(requireContext(),getString(R.string.email_nao_cadastrado))
            }
            is FirebaseAuthInvalidCredentialsException -> {
                CustomToast.showToast(requireContext(), getString(R.string.senha_incorreta))

            }
            else -> {
                CustomToast.showToast(requireContext(), getString(R.string.houve_um_erro))

            }
        }
    }


    private fun authenticationUser() {
        val userLogin = FirebaseInstances.getFirebaseAuth().currentUser
        if (userLogin != null){
           findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            return
        }


    }


}