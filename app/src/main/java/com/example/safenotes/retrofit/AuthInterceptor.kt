package com.example.safenotes.retrofit

import com.example.safenotes.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    //interceptor kya karta hai basically humara jo bhi request hai usko send karne se pehele agar kuch modification dalni hai
    //to wo hum kar sakte hain jaise ki humare case me hum har notes ke end points par authentication token dalenge send krne
    //se pehele

    @Inject
    lateinit var tokenManager: TokenManager

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = tokenManager.getToken()
        request.addHeader("Authorization","Bearer $token")

        return chain.proceed(request.build())
    }


}