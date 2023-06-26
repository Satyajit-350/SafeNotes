package com.example.safenotes.presentation.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.safenotes.R
import com.example.safenotes.databinding.ActivityLoginBinding
import com.example.safenotes.models.user.UserRequest
import com.example.safenotes.presentation.activity.HomeActivity
import com.example.safenotes.presentation.activity.forgotPassword.ForgotPasswordActivity
import com.example.safenotes.presentation.activity.register.RegisterActivity
import com.example.safenotes.utils.NetworkResult
import com.example.safenotes.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel by viewModels<LoginViewModel>()

    private var pressedTime: Long = 0

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.userResponseLiveData.observe(this) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
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

        binding.btnLogin.setOnClickListener {
            validateAndLogin()
        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }

        binding.txtForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()

    }
}