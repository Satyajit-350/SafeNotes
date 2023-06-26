package com.example.safenotes.presentation.activity.ui.fragments.audioNotes.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.safenotes.databinding.BottomSheetAudioBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSaveAudio: BottomSheetDialogFragment() {

    private var _binding : BottomSheetAudioBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAudioBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}