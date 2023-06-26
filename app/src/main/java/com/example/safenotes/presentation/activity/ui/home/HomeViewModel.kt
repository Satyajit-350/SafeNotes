package com.example.safenotes.presentation.activity.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safenotes.models.notes.NotesRequest
import com.example.safenotes.models.notes.NotesResponse
import com.example.safenotes.retrofit.repository.NotesRepository
import com.example.safenotes.retrofit.repository.QuotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val notesRepository: NotesRepository,
private val quotesRepository: QuotesRepository): ViewModel() {

    private val _category = MutableLiveData<List<String>>().apply {
        val category = mutableListOf("All", "Notes", "Audio","Documents","Links","Others")
        value = category
    }
    val category: LiveData<List<String>> = _category

    //quotes live data
    val quotesLiveData get() = quotesRepository.quotesLiveData

    //notes live data
    val notesLiveData get() = notesRepository.notesLiveData
    //status live data
    val statusLiveData get() = notesRepository.statusLiveData

    fun getNotes() = viewModelScope.launch {
        notesRepository.getNotes()
    }

    fun getQuote(){
        viewModelScope.launch {
            quotesRepository.getQuote()
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