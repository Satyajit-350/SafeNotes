package com.example.safenotes.presentation.activity.forgotPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safenotes.models.user.UserRequest
import com.example.safenotes.models.user.UserResponse
import com.example.safenotes.retrofit.repository.UserRepository
import com.example.safenotes.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPassViewModel @Inject constructor(private val  userRepository: UserRepository): ViewModel() {

    val userResponseLiveData : LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    fun forgetPassword(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.forgotPassword(userRequest)
        }
    }

}