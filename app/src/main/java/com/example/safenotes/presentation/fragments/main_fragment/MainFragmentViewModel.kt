package com.example.safenotes.presentation.fragments.main_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safenotes.models.notes.NotesRequest
import com.example.safenotes.retrofit.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(private val notesRepository: NotesRepository):ViewModel() {

    //notes live data
    val notesLiveData get() = notesRepository.notesLiveData
    //status live data
    val statusLiveData get() = notesRepository.statusLiveData

    fun getNotes(){
        viewModelScope.launch {
            notesRepository.getNotes()
        }
    }

    fun createNote(notesRequest: NotesRequest){
        viewModelScope.launch {
            notesRepository.createNote(notesRequest)
        }
    }

    fun deleteNote(noteId: String){
        viewModelScope.launch {
            notesRepository.deleteNote(noteId)
        }
    }

    fun updateNote(noteId: String, notesRequest: NotesRequest){
        viewModelScope.launch {
            notesRepository.updateNote(noteId,notesRequest)
        }
    }

}