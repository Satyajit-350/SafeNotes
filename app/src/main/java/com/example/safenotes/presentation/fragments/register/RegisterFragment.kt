package com.example.safenotes.presentation.fragments.register

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.safenotes.R
import com.example.safenotes.databinding.FragmentRegisterBinding
import com.example.safenotes.models.user.UserRequest
import com.example.safenotes.presentation.activity.register.RegisterViewModel
import com.example.safenotes.utils.NetworkResult
import com.example.safenotes.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding? = null
    //bar bar null safe na karne ke liye ek hi jagah par null safe bana do and use the binding object
    private val binding get() = _binding!!

    private val registerViewModel by viewModels<RegisterViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)

        if(tokenManager.getToken() !=  null){
//            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
//                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }

                is NetworkResult.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is NetworkResult.Message -> {}
            }

        }

        binding.btnSignUp.setOnClickListener {
            validateUserInputAndRegister()
        }

        binding.btnLogin.setOnClickListener {
//            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val username = binding.txtUsername.text.toString()
        return UserRequest(emailAddress,password,username)
    }

    private fun validateUserInputAndRegister(){
        val userRequest = getUserRequest()

        if(TextUtils.isEmpty(userRequest.username) || TextUtils.isEmpty(userRequest.email) || TextUtils.isEmpty(userRequest.password)){
            binding.emailInputLayout.error = if (TextUtils.isEmpty(userRequest.email)) resources.getString(R.string.provide_credentials) else null
            binding.passwordInputLayout.error = if (TextUtils.isEmpty(userRequest.password)) resources.getString(R.string.provide_credentials) else null
            binding.usernameInputLayout.error = if (TextUtils.isEmpty(userRequest.username)) resources.getString(R.string.provide_credentials) else null
            return
        }else if(!Patterns.EMAIL_ADDRESS.matcher(userRequest.email).matches()){
            binding.emailInputLayout.error = resources.getString(R.string.valid_emailAddress)
            return
        }else if(userRequest.password.length<=5){
            binding.passwordInputLayout.error = resources.getString(R.string.valid_password)
            return
        }
        clearError()

        registerViewModel.registerUser(userRequest)
    }

    private fun clearError() {
        binding.emailInputLayout.error = null
        binding.passwordInputLayout.error = null
        binding.usernameInputLayout.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}