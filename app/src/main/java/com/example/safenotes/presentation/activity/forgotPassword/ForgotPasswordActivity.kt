package com.example.safenotes.presentation.activity.forgotPassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.safenotes.R
import com.example.safenotes.databinding.ActivityForgotPasswordBinding
import com.example.safenotes.models.user.UserRequest
import com.example.safenotes.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {

    private var _binding: ActivityForgotPasswordBinding?=null
    private val binding get() = _binding!!

    private val forgotPassViewModel by viewModels<ForgotPassViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        forgotPassViewModel.userResponseLiveData.observe(this, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                //for conflict of data class i have used here the message class which act similar to success
                is NetworkResult.Message -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}