package com.example.notesapptest.ui.Notes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapptest.MainActivity
import com.example.notesapptest.data_models.Note
import com.example.notesapptest.databinding.NoteItemLayoutBinding
import com.example.notesapptest.ui.Edit_Notes.EditNoteViewModel

class NoteAdapter(private var noteList: List<Note> , viewmodel: EditNoteViewModel) : RecyclerView.Adapter<NoteAdapter.NoteItemHolder>() {
    val viewmd = viewmodel
    inner class NoteItemHolder ( val binding: NoteItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(note:Note){
            binding.apply {
                noteTitleTV.text=note.noteTitle
                noteContentTV.text=note.content
                noteTitleTV.setOnClickListener(View.OnClickListener {
                    viewmd.setNote(note)
                })
                noteContentTV.setOnClickListener(View.OnClickListener {
                    viewmd.setNote(note)
                })

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