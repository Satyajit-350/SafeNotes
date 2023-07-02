package com.example.safenotes.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.safenotes.databinding.FragmentEditNotesBinding
import com.example.safenotes.models.notes.NotesRequest
import com.example.safenotes.models.notes.NotesResponse
import com.example.safenotes.presentation.fragments.main_fragment.MainFragmentViewModel
import com.example.safenotes.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNotesFragment : Fragment() {

    private var _binding : FragmentEditNotesBinding? = null
    private val binding get() =  _binding!!
    private var note: NotesResponse? = null

    private val mainViewModel by viewModels<MainFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNotesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()

        bindHandlers()

        bindObservers()
    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener {
            note?.let { mainViewModel.deleteNote(it!!._id) }
        }
        binding.apply {
            submitBtn.setOnClickListener {
                val title = textTitle.text.toString()
                val description = textDescription.text.toString()
                val noteRequest = NotesRequest(description, title)
                if (note == null) {
                    mainViewModel.createNote(noteRequest)
                } else {
                    mainViewModel.updateNote(note!!._id, noteRequest)
                }
            }
        }
    }

    private fun bindObservers() {
        mainViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }

                is NetworkResult.Message -> {}
            }
        })
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("note")
        if(jsonNote!=null){
            note = Gson().fromJson(jsonNote, NotesResponse::class.java)
            //note ke corresponding properties set karwa do
            note?.let {
                binding.textTitle.setText(it.title)
                binding.textDescription.setText(it.description)
                binding.btnDelete.isVisible = true
            }
        }
        else{
            findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
                destination.label = "Add Notes"
            }
            binding.btnDelete.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}