package com.example.safenotes.retrofit.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.safenotes.models.audio.AudioNoteRequest
import com.example.safenotes.models.audio.AudioNoteResponse
import com.example.safenotes.retrofit.AudioApi
import com.example.safenotes.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class AudioRepository @Inject constructor(private val audioApi: AudioApi) {

    private val _audioNotesLiveData = MutableLiveData<NetworkResult<List<AudioNoteResponse>>>()
    val audioNotesLiveData: LiveData<NetworkResult<List<AudioNoteResponse>>>
        get() = _audioNotesLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getAudioNotes(){
        _audioNotesLiveData.postValue(NetworkResult.Loading())
        try {
            val response = audioApi.getAudioNotes()
            if(response.isSuccessful && response.body()!=null){
                _audioNotesLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errObj = JSONObject(response.errorBody()!!.charStream().readText())
                _audioNotesLiveData.postValue(NetworkResult.Error(errObj.getString("message")))
            } else {
                _audioNotesLiveData.postValue(NetworkResult.Error("Something went wrong"))
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    suspend fun createAudioNote(audioNoteRequest: AudioNoteRequest){
        _audioNotesLiveData.postValue(NetworkResult.Loading())
        try {
            val response = audioApi.createAudioNote(audioNoteRequest)
            handleResponse(response,"Audio Notes Created")
        }catch (e:Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteAudioNote(audioNoteId: String){
        _statusLiveData.postValue(NetworkResult.Loading())
        try {
            val response = audioApi.deleteAudioNote(audioNoteId)
            handleResponse(response,"Audio Note Deleted")
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun handleResponse(response: Response<AudioNoteResponse>, message:String) {
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