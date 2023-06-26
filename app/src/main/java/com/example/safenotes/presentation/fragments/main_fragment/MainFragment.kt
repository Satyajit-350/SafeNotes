package com.example.safenotes.presentation.fragments.main_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.safenotes.adapters.NoteAdapter
import com.example.safenotes.databinding.FragmentMainBinding
import com.example.safenotes.models.notes.NotesResponse
import com.example.safenotes.utils.NetworkResult
import com.example.safenotes.utils.TokenManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? =null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainFragmentViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        adapter = NoteAdapter(::onNoteClicked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.getNotes()
        binding.noteList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = adapter

        mainViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading ->{
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    adapter.submitList(it.data)
                }

                is NetworkResult.Message -> {}
            }
        })

        binding.addNote.setOnClickListener {
//            findNavController().navigate(R.id.action_mainFragment_to_notesFragment)
        }

        binding.logout.setOnClickListener {
            tokenManager.logout()
//            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }
    }

    private fun onNoteClicked(noteResponse: NotesResponse){
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse))//gson helps us to convert object into string
//        findNavController().navigate(R.id.action_mainFragment_to_notesFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}