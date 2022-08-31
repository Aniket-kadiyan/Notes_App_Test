package com.example.notesapptest.ui.Folders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapptest.data_models.Folder
import com.example.notesapptest.databinding.FolderItemLayoutBinding
import com.example.notesapptest.ui.Notes.NotesViewModel
import com.example.notesapptest.ui.View_Notes_in_Folder.ViewNotesinFolderViewModel

class FolderAdatper(private var folderList: List<Folder> , viewModel : ViewNotesinFolderViewModel): RecyclerView.Adapter<FolderAdatper.FolderItemHolder>() {
    val viewmd = viewModel
    inner class FolderItemHolder (val binding: FolderItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(folder:Folder){
            binding.apply {
                folderTitleTV.text=folder.folderTitle
                folderCountTV.text=folder.notesCount.toString()
                folderTitleTV.setOnClickListener(View.OnClickListener {
                    viewmd.setViewFolder(folder)
                })
                folderCountTV.setOnClickListener(View.OnClickListener{
                    viewmd.setViewFolder(folder)
                })
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderItemHolder {
        return FolderItemHolder(FolderItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    override fun onBindViewHolder(holder: FolderItemHolder, position: Int) {
        val item = folderList[position]
        holder.bind(item)
    }
}