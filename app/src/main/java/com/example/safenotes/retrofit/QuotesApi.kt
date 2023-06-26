package com.example.safenotes.retrofit

import com.example.safenotes.models.quotes.QuotesResponse
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi {

    @GET("/quote")
    suspend fun getQuote() : Response<QuotesResponse>
}