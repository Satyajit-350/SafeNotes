package com.example.safenotes.retrofit.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.safenotes.models.notes.NotesRequest
import com.example.safenotes.models.notes.NotesResponse
import com.example.safenotes.retrofit.NotesApi
import com.example.safenotes.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NotesRepository @Inject constructor(private val notesApi: NotesApi){

    private val _notesLiveData = MutableLiveData<NetworkResult<List<NotesResponse>>>()
    val notesLiveData: LiveData<NetworkResult<List<NotesResponse>>>
        get() = _notesLiveData

    //live data for status of the notes
    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getNotes(){
        _notesLiveData.postValue(NetworkResult.Loading())
        try{
            val response = notesApi.getNotes()
            if (response.isSuccessful && response.body() != null) {
                _notesLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errObj = JSONObject(response.errorBody()!!.charStream().readText())
                _notesLiveData.postValue(NetworkResult.Error(errObj.getString("message")))
            } else {
                _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    suspend fun createNote(notesRequest: NotesRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        try {
            val response = notesApi.createNote(notesRequest)
            handleResponse(response,"Notes Created")
        }catch (e:Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteNote(noteId: String){
        _statusLiveData.postValue(NetworkResult.Loading())
        try {
            val response = notesApi.deleteNote(noteId)
            handleResponse(response,"Note Deleted")
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    suspend fun updateNote(noteId: String, notesRequest: NotesRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        try{
            val response = notesApi.updateNote(noteId,notesRequest)
            handleResponse(response,"Note Updated")
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun handleResponse(response: Response<NotesResponse>, message:String) {
        try {
            if (response.isSuccessful && response.body() != null) {
                _statusLiveData.postValue(NetworkResult.Success(message))
            } else {
                _statusLiveData.postValue(NetworkResult.Error("Something Went wrong"))
            }
        }catch (e:Exception) {
            e.printStackTrace()
        }
    }
}