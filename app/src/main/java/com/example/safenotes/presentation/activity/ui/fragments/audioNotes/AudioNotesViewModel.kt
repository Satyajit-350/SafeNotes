package com.example.safenotes.presentation.activity.ui.fragments.audioNotes

import android.media.MediaRecorder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AudioNotesViewModel : ViewModel(){

    private var mediaRecorder:MediaRecorder?=null

    private var dirPath: String = ""
    private var fileName: String = ""

    fun startRecording(){
        mediaRecorder = MediaRecorder()


        viewModelScope.launch(Dispatchers.IO) {
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            try {
                mediaRecorder?.prepare()
                mediaRecorder?.start()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun stopRecording(){
        viewModelScope.launch(Dispatchers.IO) {
            mediaRecorder?.apply {
                stop()
                reset()
                release()
            }
            mediaRecorder = null
            //TODO send the recording file through api
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaRecorder?.release()
    }

}