package com.example.safenotes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.safenotes.databinding.ItemNotes2Binding
import com.example.safenotes.models.notes.NotesResponse

class NoteAdapter2(private val onNoteClicked: (NotesResponse) -> Unit) :
    ListAdapter<NotesResponse, NoteAdapter2.NoteViewHolder>(ComparatorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNotes2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
    }

    inner class NoteViewHolder(private val binding: ItemNotes2Binding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NotesResponse) {
            binding.title.text = note.title
            binding.desc.text = note.description
            binding.root.setOnClickListener {
                onNoteClicked(note)
            }
        }

    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<NotesResponse>() {
        override fun areItemsTheSame(oldItem: NotesResponse, newItem: NotesResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: NotesResponse, newItem: NotesResponse): Boolean {
            return oldItem == newItem
        }
    }
}