package com.example.safenotes.presentation.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.safenotes.R
import com.example.safenotes.databinding.ActivityRegisterBinding
import com.example.safenotes.models.user.UserRequest
import com.example.safenotes.presentation.MainActivity
import com.example.safenotes.presentation.activity.HomeActivity
import com.example.safenotes.presentation.activity.login.LoginActivity
import com.example.safenotes.utils.NetworkResult
import com.example.safenotes.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding?=null
    //bar bar null safe na karne ke liye ek hi jagah par null safe bana do and use the binding object
    private val binding get() = _binding!!

    private val registerViewModel by viewModels<RegisterViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(tokenManager.getToken() !=  null){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        registerViewModel.userResponseLiveData.observe(this) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }

                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
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
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
