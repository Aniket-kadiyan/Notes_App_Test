package com.example.notesapptest.ui.Notes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapptest.DeletionViewModel
import com.example.notesapptest.MainActivity
import com.example.notesapptest.data_models.Note
import com.example.notesapptest.databinding.NoteItemLayoutBinding
import com.example.notesapptest.ui.Edit_Notes.EditNoteViewModel

class NoteAdapter(private var noteList: List<Note> , viewmodel: EditNoteViewModel ,deletemodel : DeletionViewModel, selflag : Int) : RecyclerView.Adapter<NoteAdapter.NoteItemHolder>() {
    val viewmd = viewmodel
    val delmodel = deletemodel
    var delList = ArrayList<Note>()
    var delselection =0
    var sflag = selflag
    inner class NoteItemHolder ( val binding: NoteItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(note:Note){
            binding.apply {
                noteTitleTV.text=note.noteTitle
                noteContentTV.text=note.content
                root.setOnClickListener(View.OnClickListener {
                    if(delselection==1){
                        if(delList.contains(note)){
                            delList.remove(note)
                            if(delList.isEmpty())
                                delselection=0
                            notifyItemChanged(adapterPosition)
                        }
                        else{
                            if (delList.isEmpty()) {
                                // do nothing
                                delselection = 0
                            } else {
                                delList.add(note)
                            }
                            notifyDataSetChanged()
                        }
                    }
                    else{
                        viewmd.setNote(note)
                    }
                    delmodel.setnotedeleteList(delList)
                    Log.d("note selection:::", "$delselection\n$delList\n\n ")
                })
                root.setOnLongClickListener {
                if(sflag==1) {
                    if (delselection == 0) {
                        delselection = 1
                        delList.add(note)
                        notifyItemChanged(adapterPosition)
                    } else {
                        delList.clear()
                        delselection = 0
                        notifyDataSetChanged()
                    }
                    Log.d("note selection:::", "$delselection\n$delList\n\n ")
                }
                    delmodel.setnotedeleteList(delList)
                    return@setOnLongClickListener true
                }

                if (delList.contains(note)) {
                    noteContentTV.setBackgroundColor(("#ff0000").toColorInt())
                } else {
                    noteContentTV.setBackgroundColor(("#ffffff").toColorInt())
                }


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