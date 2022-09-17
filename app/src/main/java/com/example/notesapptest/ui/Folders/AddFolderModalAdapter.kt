package com.example.notesapptest.ui.Folders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapptest.R
import com.example.notesapptest.databinding.ChangeFolderModalItemBinding
import com.example.notesapptest.databinding.ColorPalleteItemLayoutBinding

class AddFolderModalAdapter(private var colorlist : List<String> , var button: Button ) : RecyclerView.Adapter<AddFolderModalAdapter.ItemViewHolder>() {
    var selectedcolor =""
    var selectedPosition=-1
    inner class ItemViewHolder(val binding : ColorPalleteItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(color : String){
            binding.colButton.setBackgroundColor(color.toColorInt())
            binding.colButton.setOnClickListener {
                button.setTextColor(color.toColorInt())
                selectedcolor = color
            }
        }
    }

    override fun getItemCount(): Int {
        return colorlist.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ColorPalleteItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var color = colorlist[position]
        holder.bind(color)
        if(selectedPosition==position){
            holder.itemView.isSelected =true
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.itemView.isFocused
        }
        else{
            holder.itemView.isSelected = false
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }



        holder.itemView.setOnClickListener {
            if(selectedPosition>=0) {
                notifyItemChanged(selectedPosition)
                selectedcolor=colorlist[selectedPosition]
                button.setTextColor(colorlist[position].toColorInt())
            }
            selectedPosition = holder.adapterPosition
            selectedcolor=colorlist[selectedPosition]
            button.setTextColor(colorlist[position].toColorInt())
            notifyItemChanged(selectedPosition)
        }
    }
}