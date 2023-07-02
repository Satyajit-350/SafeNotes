package com.example.safenotes.presentation.ui.home.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safenotes.R
import com.example.safenotes.adapters.AudioAdapter
import com.example.safenotes.databinding.FragmentAudioBinding
import com.example.safenotes.presentation.ui.fragments.audioNotes.AudioNotesViewModel
import com.example.safenotes.utils.NetworkResult

class AudioFragment : Fragment() {

    private var _binding: FragmentAudioBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<AudioNotesViewModel>()

    private lateinit var adapter: AudioAdapter

    private var dataReceived= false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioBinding.inflate(inflater,container,false)
        if (savedInstanceState?.getBoolean("dataPresent")==true){
            dataReceived= true
        }
        adapter = AudioAdapter()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.notifyDataSetChanged()

        binding.audioRv.layoutManager = LinearLayoutManager(context)
        binding.audioRv.adapter = adapter

        viewModel.audioNotesLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Error -> {
                    Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    //TODO
                }
                is NetworkResult.Message -> TODO()
                is NetworkResult.Success -> {
                    dataReceived= true
                    binding.noItemsLy.visibility = View.GONE
                    binding.audioRv.visibility = View.VISIBLE

                    adapter.submitList(it.data)
                }
            }
        })

        if (!dataReceived){
            viewModel.getAudioNotes()
        }

        binding.uploadBtn.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_audioNoteFragment)
        }

    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
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