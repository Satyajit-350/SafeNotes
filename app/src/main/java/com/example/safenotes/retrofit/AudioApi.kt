package com.example.safenotes.retrofit

import com.example.safenotes.models.audio.AudioNoteRequest
import com.example.safenotes.models.audio.AudioNoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AudioApi {

    @GET("/audioNote")
    suspend fun getAudioNotes() : Response<List<AudioNoteResponse>>

    @POST("/audioNote")
    suspend fun createAudioNote(@Body audioNoteRequest: AudioNoteRequest): Response<AudioNoteResponse>

    @DELETE("/audioNote/{audioNoteId}")
    suspend fun deleteAudioNote(@Path("audioNoteId") audioNoteId: String): Response<AudioNoteResponse>

}