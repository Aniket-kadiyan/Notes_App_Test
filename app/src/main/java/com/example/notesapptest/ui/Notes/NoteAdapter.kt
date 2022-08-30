package com.example.notesapptest.ui.Notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapptest.data_models.Note
import com.example.notesapptest.databinding.NoteItemLayoutBinding

class NoteAdapter(private var noteList: List<Note>) : RecyclerView.Adapter<NoteAdapter.NoteItemHolder>() {
    inner class NoteItemHolder ( val binding: NoteItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(note:Note){
            binding.apply {
                noteTitleTV.text=note.noteTitle
                noteContentTV.text=note.content
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemHolder {
        return NoteItemHolder(NoteItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteItemHolder, position: Int) {
        val item = noteList[position]
        holder.bind(item)
    }
}