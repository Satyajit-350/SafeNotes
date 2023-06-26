package com.example.safenotes.presentation.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.safenotes.R
import com.example.safenotes.databinding.BottomSheetNoteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetNotes : BottomSheetDialogFragment() {

    private var _binding : BottomSheetNoteBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = BottomSheetNoteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Customize your BottomSheet view and handle interactions here

        binding.addAudioCardView.setOnClickListener{
            findNavController().navigate(R.id.action_nav_home_to_audioNoteFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}