package com.example.safenotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.safenotes.R
import com.example.safenotes.databinding.ItemAudioBinding
import com.example.safenotes.models.audio.AudioNoteResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AudioAdapter : ListAdapter<AudioNoteResponse, AudioAdapter.AudioViewHolder>(ComparatorDiffUtil()){

    private var isPlaying = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val binding = ItemAudioBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AudioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audioNote = getItem(position)
        audioNote?.let {
            holder.bind(it)
        }
    }


    inner class AudioViewHolder(private val binding: ItemAudioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(audioNote: AudioNoteResponse) {
            binding.titleTv.text = audioNote.title

            val date = extractDateFromTimestamp(audioNote.createdAt)

            binding.dateTv.text = date

            binding.fileNameTv.text = audioNote.fileName

            binding.materialCardView.setOnClickListener {
                if(!isPlaying){
                    isPlaying = true;
                    binding.playImg.setImageResource(R.drawable.baseline_pause_24)
                    binding.seekBar.visibility = View.VISIBLE
                    binding.titleTv.visibility = View.GONE
                    binding.dateTv.visibility = View.GONE
                    binding.fileNameTv.visibility = View.GONE
                }else{
                    isPlaying = false
                    binding.playImg.setImageResource(R.drawable.baseline_play_arrow_24)
                    binding.seekBar.visibility = View.GONE
                    binding.titleTv.visibility = View.VISIBLE
                    binding.dateTv.visibility =View.VISIBLE
                    binding.fileNameTv.visibility = View.VISIBLE
                }
            }

        }
    }

    fun extractDateFromTimestamp(timestamp: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return try {
            val date = inputFormat.parse(timestamp)
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<AudioNoteResponse>(){
        override fun areItemsTheSame(
            oldItem: AudioNoteResponse,
            newItem: AudioNoteResponse
        ): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: AudioNoteResponse,
            newItem: AudioNoteResponse
        ): Boolean {
            return oldItem == newItem
        }

    }



}