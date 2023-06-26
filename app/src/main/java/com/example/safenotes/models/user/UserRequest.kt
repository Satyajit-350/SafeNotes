package com.example.safenotes.models.user

data class UserRequest(
    val email: String,
    val password: String,
    val username: String
)