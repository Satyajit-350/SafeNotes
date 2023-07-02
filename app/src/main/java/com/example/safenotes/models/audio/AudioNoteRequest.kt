package com.example.safenotes.models.audio

data class AudioNoteRequest(

    val title: String,
    val fileName: String,
    val filePath: String,
    val duration: String,
    val ampPath: String,

)
