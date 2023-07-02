package com.example.safenotes.presentation.ui.fragments.audioNotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safenotes.models.audio.AudioNoteRequest
import com.example.safenotes.retrofit.repository.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioNotesViewModel @Inject constructor(private val audioRepository: AudioRepository): ViewModel(){

    val audioNotesLiveData get() = audioRepository.audioNotesLiveData
    val statusLiveData get() = audioRepository.statusLiveData

    fun getAudioNotes(){
        viewModelScope.launch {
            audioRepository.getAudioNotes()
        }
    }

    fun createAudioNote(audioNoteRequest: AudioNoteRequest){
        viewModelScope.launch {
            audioRepository.createAudioNote(audioNoteRequest)
        }
    }

    fun deleteAudioNote(audioNoteId: String){
        viewModelScope.launch {
            audioRepository.deleteAudioNote(audioNoteId)
        }
    }

}