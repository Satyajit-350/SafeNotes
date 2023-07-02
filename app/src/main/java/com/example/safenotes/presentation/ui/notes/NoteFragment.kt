package com.example.safenotes.presentation.ui.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.safenotes.R
import com.example.safenotes.adapters.NoteAdapter
import com.example.safenotes.databinding.FragmentNoteBinding
import com.example.safenotes.models.notes.NotesResponse
import com.example.safenotes.presentation.ui.home.HomeViewModel
import com.example.safenotes.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding : FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<HomeViewModel>()

    private lateinit var adapter: NoteAdapter

    private var dataReceived= false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater,container,false)

        if (savedInstanceState?.getBoolean("dataPresent")==true){
            dataReceived= true
        }

        adapter = NoteAdapter(::onNoteClicked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.noteList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = adapter

        viewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading ->{
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    dataReceived= true
                    adapter.submitList(it.data)
                }

                is NetworkResult.Message -> {}
            }
        })

        if (!dataReceived){
            viewModel.getNotes()
        }

        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_nav_notes_to_editNote)
        }

    }

    private fun onNoteClicked(noteResponse: NotesResponse){
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse))//gson helps us to convert object into string
        findNavController().navigate(R.id.action_nav_notes_to_editNote)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (dataReceived){
            outState.putBoolean("dataPresent", true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}