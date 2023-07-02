package com.example.safenotes.retrofit

import com.example.safenotes.models.notes.NotesRequest
import com.example.safenotes.models.notes.NotesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotesApi {

    @GET("/note")
    suspend fun getNotes() : Response<List<NotesResponse>>

    @POST("/note")
    suspend fun createNote(@Body notesRequest: NotesRequest): Response<NotesResponse>

    @PUT("/note/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId: String, @Body notesRequest: NotesRequest): Response<NotesResponse>

    @DELETE("/note/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId: String): Response<NotesResponse>

}