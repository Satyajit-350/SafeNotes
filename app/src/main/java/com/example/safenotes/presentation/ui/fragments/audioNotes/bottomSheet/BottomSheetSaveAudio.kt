package com.example.safenotes.presentation.ui.fragments.audioNotes.bottomSheet

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.safenotes.databinding.BottomSheetAudioBinding
import com.example.safenotes.utils.listeners.AudioBottomSheetListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSaveAudio: BottomSheetDialogFragment() {

    private var _binding : BottomSheetAudioBinding?=null
    private val binding get() = _binding!!

    private lateinit var filename: String

    private var listener: AudioBottomSheetListener? = null

    override fun onStart() {
        super.onStart()
        val bottomSheetBehavior = BottomSheetBehavior.from(requireView().parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAudioBinding.inflate(inflater,container,false)
        filename = arguments?.getString("fileName").toString()
        binding.fileNameInput.setText(filename)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelBtn.setOnClickListener {
            listener?.cancel()
            dismiss()
        }

        binding.saveBtn.setOnClickListener {
            val newFileName = binding.fileNameInput.text.toString().trim()
            listener?.save(newFileName)
            dismiss()
        }
    }

    fun setInterface(interfaces: AudioBottomSheetListener) {
        listener = interfaces
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}