package com.example.safenotes.retrofit

import com.example.safenotes.models.ResponseMessage
import com.example.safenotes.models.user.UserRequest
import com.example.safenotes.models.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    //user ke related jitne bhi end points honge all will be defined here

    @POST("/users/signup")
    suspend fun signup(@Body userRequest: UserRequest): Response<UserResponse>

    @POST("/users/signin")
    suspend fun signin(@Body userRequest: UserRequest): Response<UserResponse>

    @POST("/users/forget-password")
    suspend fun forgotPassword(@Body userRequest: UserRequest): Response<ResponseMessage>

}