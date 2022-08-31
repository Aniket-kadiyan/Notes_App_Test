package com.example.notesapptest.ui.Notes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapptest.data_models.NoteDatabase
import com.example.notesapptest.databinding.FragmentNotesBinding
import com.example.notesapptest.ui.Edit_Notes.EditNoteViewModel
import kotlin.math.absoluteValue

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    val binding
    get() = _binding!!

    private var viewModelInstance : NotesViewModel?=null
    private var editNoteViewModel : EditNoteViewModel?=null
    lateinit var notesDB : NoteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelInstance = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)
        editNoteViewModel = ViewModelProvider(requireActivity()).get(EditNoteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        _binding = FragmentNotesBinding.inflate(layoutInflater)

        viewModelInstance!!.text.observe(viewLifecycleOwner) {
            binding.textHome.text=it
        }
        viewModelInstance!!.getNoteList().observe(viewLifecycleOwner){
            if(it!=null)
                if(it.isNotEmpty()){
                    var adapter = NoteAdapter(it,editNoteViewModel!!)
                    binding.notesRV.visibility= View.VISIBLE
                    binding.notesRV.isVisible = true
                    binding.textHome.visibility=View.INVISIBLE
                    binding.textHome.isVisible=false
                    binding.notesRV.adapter=adapter
                }
                else {
                    Log.d("ERROR:::", "Data is null now")
                }
        }
        editNoteViewModel!!.getNote().observe(viewLifecycleOwner){
            Log.d("edit Note", "onCreateView: $it")

        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}