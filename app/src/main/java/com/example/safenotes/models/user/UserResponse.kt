package com.example.safenotes.models.user

import com.example.safenotes.models.user.User

data class UserResponse(
    val token: String,
    val user: User
)