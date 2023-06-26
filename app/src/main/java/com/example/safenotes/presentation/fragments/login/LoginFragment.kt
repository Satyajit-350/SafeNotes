package com.example.safenotes.presentation.fragments.login

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
import androidx.navigation.fragment.findNavController
import com.example.safenotes.R
import com.example.safenotes.databinding.FragmentLoginBinding
import com.example.safenotes.models.user.UserRequest
import com.example.safenotes.presentation.activity.login.LoginViewModel
import com.example.safenotes.utils.NetworkResult
import com.example.safenotes.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
//                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
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

        binding.btnLogin.setOnClickListener {
            validateAndLogin()
        }

        binding.btnSignUp.setOnClickListener {
//            findNavController().navigate(R.id.action_loginFragment_to_registerFragment) //wrong approach
            findNavController().popBackStack()
        }

        binding.txtForgotPassword.setOnClickListener {
//            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString().trim()
        val password = binding.txtPassword.text.toString().trim()
        return UserRequest(emailAddress,password,"")
    }

    private fun validateAndLogin() {
        val userRequest = getUserRequest()

        if(TextUtils.isEmpty(userRequest.email) || TextUtils.isEmpty(userRequest.password)){
            binding.emailInputLayout.error = if (TextUtils.isEmpty(userRequest.email)) resources.getString(R.string.provide_credentials) else null
            binding.passwordInputLayout.error = if (TextUtils.isEmpty(userRequest.password)) resources.getString(R.string.provide_credentials) else null
            return
        }else if(!Patterns.EMAIL_ADDRESS.matcher(userRequest.email).matches()){
            binding.emailInputLayout.error = resources.getString(R.string.valid_emailAddress)
            return
        }else if(userRequest.password.length<=5){
            binding.passwordInputLayout.error = resources.getString(R.string.valid_password)
            return
        }
        clearError()

        loginViewModel.loginUser(userRequest)
    }

    private fun clearError() {
        binding.emailInputLayout.error = null
        binding.passwordInputLayout.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}