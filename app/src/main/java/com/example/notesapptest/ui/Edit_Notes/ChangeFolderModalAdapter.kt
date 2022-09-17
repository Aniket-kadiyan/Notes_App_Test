package com.example.notesapptest.ui.Edit_Notes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapptest.R
import com.example.notesapptest.data_models.Folder
import com.example.notesapptest.databinding.ChangeFolderModalItemBinding
import com.example.notesapptest.databinding.FolderItemLayoutBinding

class ChangeFolderModalAdapter(private var folderList : List<Folder> , private var textview : TextView): RecyclerView.Adapter<ChangeFolderModalAdapter.FolderItemHolder>() {
    var selectedPosition = -1
    var selectedfolderid =-1
    inner class FolderItemHolder ( val binding : ChangeFolderModalItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(folder : Folder){
            binding.apply {
                folderItemTV.setText(folder.folderTitle)
                folderItemTV.setBackgroundColor(folder.folderColor.toColorInt())
            }
        }
    }

    override fun getItemCount(): Int {
        return folderList.size
    }
    fun getSelectedFolder() : Int{
        return selectedfolderid
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderItemHolder {
        return FolderItemHolder(ChangeFolderModalItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: FolderItemHolder, position: Int) {
        val item = folderList[position]
        holder.bind(item)
        if(selectedPosition==position){
            holder.itemView.isSelected =true
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.itemView.isFocused
            textview.text = item.folderTitle
        }
        else{
            holder.itemView.isSelected = false
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }
        holder.itemView.setOnClickListener {
            if(selectedPosition>=0) {
                notifyItemChanged(selectedPosition)
                selectedfolderid = folderList[selectedPosition].folderId
            }
            selectedPosition = holder.adapterPosition
            selectedfolderid = folderList[selectedPosition].folderId
            notifyItemChanged(selectedPosition)
        }


    }
}