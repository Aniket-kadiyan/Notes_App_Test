package com.example.notesapptest.ui.Folders

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapptest.DeletionViewModel
import com.example.notesapptest.R
import com.example.notesapptest.data_models.Folder
import com.example.notesapptest.data_models.Note
import com.example.notesapptest.databinding.FolderItemLayoutBinding
import com.example.notesapptest.ui.Notes.NotesViewModel
import com.example.notesapptest.ui.View_Notes_in_Folder.ViewNotesinFolderViewModel

class FolderAdatper(private var folderList: List<Folder>, viewModel : ViewNotesinFolderViewModel, deletemodel : DeletionViewModel, selflag : Int): RecyclerView.Adapter<FolderAdatper.FolderItemHolder>() {
    val viewmd = viewModel
    val delmodel = deletemodel
    var delList = ArrayList<Folder>()
    var delselection =0
    var sflag = selflag
    inner class FolderItemHolder (val binding: FolderItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(folder:Folder){
            binding.apply {
                folderTitleTV.text=folder.folderTitle
                folderCountTV.text=folder.notesCount.toString()
                folderTitleTV.setBackgroundColor(folder.folderColor.toColorInt())
                folderCountTV.setBackgroundColor(folder.folderColor.toColorInt())
                Log.d("Folders adapter:::", " fun bind ${folder.folderTitle} ")

                root.setOnClickListener(View.OnClickListener {
                    Log.d("Folders adapter:::", " on click ${folder.folderTitle} ")
                    if(delselection==1){
                        if(delList.contains(folder)){
                            delList.remove(folder)
                            if(delList.isEmpty())
                                delselection=0
                            notifyItemChanged(adapterPosition)
                        }
                        else{
                            if (delList.isEmpty()) {
                                // do nothing
                                delselection = 0
                            } else {
                                delList.add(folder)
                            }
                            notifyDataSetChanged()
                        }
                    }
                    else{
                        Log.d("Folders adapter:::", " setting view folder")
                        viewmd.setViewFolder(folder)
                    }
                    notifyDataSetChanged()
                    delmodel.setfolderdeleteList(delList)
                    Log.d("note selection:::", "$delselection\n$delList\n\n ")
                })

                root.setOnLongClickListener {
                    if(sflag==1) {
                        if (delselection == 0) {
                            delselection = 1
                            delList.add(folder)
                            notifyItemChanged(adapterPosition)
                        } else {
                            delList.clear()
                            delselection = 0
                            notifyDataSetChanged()
                        }
                        notifyDataSetChanged()
                        Log.d("note selection:::", "$delselection\n$delList\n\n ")
                    }
                    delmodel.setfolderdeleteList(delList)
                    return@setOnLongClickListener true
                }

                if(delselection==1){
                    folderselectedradioButton.visibility=ImageButton.VISIBLE
                    folderselectedradioButton.isVisible=true
                    if (delList.contains(folder)) {
                        folderselectedradioButton.setBackgroundResource(R.drawable.ic_selected_radio)
                    } else {
                       folderselectedradioButton.setBackgroundResource(R.drawable.ic_selected)
                    }
                }
                else{
                    folderselectedradioButton.visibility=ImageButton.INVISIBLE
                    folderselectedradioButton.isVisible=false
                }


            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderItemHolder {
        Log.d("Folders adapter:::", "oncreate ")
        return FolderItemHolder(FolderItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        //Log.d("Folders adapter:::", "item count = ${folderList.size} ")
        return folderList.size
    }

    override fun onBindViewHolder(holder: FolderItemHolder, position: Int) {
        val item = folderList[position]
        Log.d("Folders adapter:::", "on bind:::${item} ")
        holder.bind(item)
    }
}