package com.example.safenotes.presentation.activity.ui.fragments.audioNotes

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.safenotes.R
import com.example.safenotes.databinding.FragmentAudioNoteBinding
import com.example.safenotes.presentation.activity.ui.fragments.audioNotes.bottomSheet.BottomSheetSaveAudio
import com.example.safenotes.presentation.bottomSheet.BottomSheetNotes
import com.example.safenotes.utils.Timer
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AudioNoteFragment : Fragment(), Timer.OnTimerListener {

    private var _binding : FragmentAudioNoteBinding?=null
    private val binding get() = _binding!!

    private val audioNotesViewModel by activityViewModels<AudioNotesViewModel>()

    private lateinit var mediaRecorder: MediaRecorder

    private var dirPath: String = ""
    private var fileName: String = ""
    private var isRecording = false
    private var isPaused = false

    private lateinit var timer: Timer
    private lateinit var vibrator: Vibrator
    private lateinit var amplitudes: ArrayList<Float>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioNoteBinding.inflate(inflater,container,false)
        timer = Timer(this)
        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recordingCv.setOnClickListener {

            when{
                isPaused -> resumeRecording()
                isRecording -> pauseRecording()
                else -> startRecording()
            }
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))

        }


        binding.saveRec.setOnClickListener {
            //TODO save it here or send it to the api
            stopRecording()
            Toast.makeText(context,"Recording Saved",Toast.LENGTH_SHORT).show()
        }

        binding.cancelRec.setOnClickListener {
            stopRecording()
            File("$dirPath$fileName.mp3")
            Toast.makeText(context,"Recording deleted",Toast.LENGTH_SHORT).show()
        }
        binding.cancelRec.isClickable = false

        binding.saveRec.setOnClickListener {
            val bottomSheetNotes  = BottomSheetSaveAudio()
            bottomSheetNotes.show(childFragmentManager,null)
        }
    }

    private fun pauseRecording() {
        mediaRecorder.pause()
        isPaused = true
        timer.pause()
        binding.cancelRec.isClickable = true
        binding.recImg.setImageResource(R.drawable.baseline_pause_24)
    }

    private fun resumeRecording() {
        mediaRecorder.resume()
        isPaused = false
        timer.start()
        binding.recImg.setImageResource(R.drawable.ic_rec)
    }

    private fun stopRecording(){
        timer.stop()
        mediaRecorder.apply {
            stop()
            release()
        }
        isPaused = false
        isRecording = false

        binding.saveRec.visibility = View.GONE
        binding.cancelRec.isClickable = false
        binding.recImg.setImageResource(R.drawable.ic_mic)

        binding.timerTextView.text = "00:00:00"

        amplitudes = binding.waveFormView.clear()
    }

    private fun startRecording() {
        if (checkRecordingPermission()) {
            // Perform the record and save the recording
            if (!binding.titleEt.text.isNullOrBlank()) {
//                binding.audioAnimation.visibility = View.VISIBLE

                mediaRecorder = MediaRecorder()
                dirPath = "${requireContext().filesDir}/" // Internal storage directory
                val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd_hh.mm.ss", Locale.getDefault())
                val date = simpleDateFormat.format(Date())
                fileName = "${binding.titleEt.text}_rec_$date"
                val filePath = "$dirPath$fileName.mp4"

                mediaRecorder.apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setOutputFile(filePath)

                    try {
                        prepare()
                        start()
                        isRecording = true
                        isPaused = false

                        timer.start()

                        binding.cancelRec.isEnabled = true
                        binding.saveRec.visibility = View.VISIBLE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                binding.recImg.setImageResource(R.drawable.ic_rec)
            } else {
                Toast.makeText(requireContext(), "Please add a title", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestRecordingPermission()
        }
    }

    private fun requestRecordingPermission() {
        val permission = Manifest.permission.RECORD_AUDIO
        requestPermissions(arrayOf(permission), 200)
    }

    private fun checkRecordingPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.RECORD_AUDIO)
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, you can proceed with audio recording
            Toast.makeText(requireContext(),"Permission Granted",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(),"Permission Denied",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTimerChange(duration: String) {
        binding.timerTextView.text = duration
        binding.waveFormView.addAmplitude(mediaRecorder.maxAmplitude.toFloat())
    }
}