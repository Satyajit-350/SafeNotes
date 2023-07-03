package com.example.safenotes.models.notes

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

data class NotesResponse(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val description: String,
    val title: String,
    val updatedAt: String,
    val userId: String
){
    val date: LocalDate
        @RequiresApi(Build.VERSION_CODES.O)
        get() = LocalDate.parse(createdAt.substringBefore("T"))
}