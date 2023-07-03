package com.example.safenotes.presentation.ui.notes

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import java.time.LocalDate
import java.util.Calendar

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding : FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<HomeViewModel>()

    private lateinit var adapter: NoteAdapter

    private var dataReceived = false

    private lateinit var allNotes : List<NotesResponse>

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

    @RequiresApi(Build.VERSION_CODES.O)
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
//                    highlightDates(binding.calendarView,it.data!!)
                    adapter.submitList(it.data)
                    allNotes = it.data!!
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

        binding.calendarView.setOnDateChangeListener{ _, year, month, dayOfMonth ->
            filterRecyclerView(year, month, dayOfMonth)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun highlightDates(calendarView: CalendarView, notes: List<NotesResponse>) {
        val calendar = Calendar.getInstance()

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)

            val hasNotes = notes.any { note ->
                val noteDate = note.date
                noteDate == selectedDate
            }

            val dayView = calendarView.getChildAt(0).findViewById<ViewGroup>(com.google.android.material.R.id.mtrl_calendar_day_selector_frame)
                .getChildAt(dayOfMonth - 1)

            if (hasNotes) {
                dayView?.setBackgroundColor(Color.RED)
            } else {
                // Reset the background color for other dates
                dayView?.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterRecyclerView(year: Int, month: Int, dayOfMonth: Int) {

        //convert the date
        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)

        val allNotesData = allNotes

        val filteredNotes = allNotesData.filter { note ->
            val noteDate = LocalDate.parse(note.date.toString())
            noteDate.isEqual(selectedDate)
        }

        if (filteredNotes.isEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.emptyView.visibility = View.GONE
        }

        for(item in filteredNotes){
            Log.d("Notes_data", item.title);
        }

        adapter.submitList(filteredNotes)

        adapter.notifyDataSetChanged()

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