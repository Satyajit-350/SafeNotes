package com.example.safenotes.presentation.fragments.forgotPassword

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
import androidx.lifecycle.Observer
import com.example.safenotes.R
import com.example.safenotes.databinding.FragmentForgotPasswordBinding
import com.example.safenotes.models.user.UserRequest
import com.example.safenotes.presentation.activity.forgotPassword.ForgotPassViewModel
import com.example.safenotes.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private var _binding : FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private val forgotPassViewModel by viewModels<ForgotPassViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forgotPassViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                //for conflict of data class i have used here the message class which act similar to success
                is NetworkResult.Message -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success -> {}
            }
        })

        binding.btnSend.setOnClickListener {
            validateInput()
        }
    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString().trim()
        return UserRequest(emailAddress,"","")
    }

    private fun validateInput() {

//        activity?.currentFocus?.let { view ->
//            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            imm?.hideSoftInputFromWindow(view.windowToken, 0)
//        }

        val userRequest = getUserRequest()

        if(TextUtils.isEmpty(userRequest.email)){
            binding.emailInputLayout.error = if (TextUtils.isEmpty(userRequest.email)) resources.getString(
                R.string.provide_credentials) else null
            return
        }else if(!Patterns.EMAIL_ADDRESS.matcher(userRequest.email).matches()){
            binding.emailInputLayout.error = resources.getString(R.string.valid_emailAddress)
            return
        }
        clearError()

        forgotPassViewModel.forgetPassword(userRequest)
    }

    private fun clearError() {
        binding.emailInputLayout.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}