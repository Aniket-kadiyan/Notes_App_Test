package com.example.notesapptest.ui.Edit_Notes

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.notesapptest.data_models.FolderDatabase
import com.example.notesapptest.data_models.NoteDatabase
import com.example.notesapptest.databinding.EditnotelayoutBinding
import com.example.notesapptest.ui.Folders.FoldersViewModel
import com.example.notesapptest.ui.Notes.NotesViewModel

class EditNoteActivity : AppCompatActivity() {
    private var _binding : EditnotelayoutBinding?=null
    val binding
    get() =_binding
    lateinit var foldersDB : FolderDatabase
    lateinit var notesDB : NoteDatabase
    private lateinit var foldersViewModel: FoldersViewModel
    private lateinit var notesViewModel: NotesViewModel
    private var editNoteViewModel : EditNoteViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= EditnotelayoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        notesViewModel= ViewModelProvider(this).get(NotesViewModel::class.java)
        foldersViewModel= ViewModelProvider(this).get(FoldersViewModel::class.java)
        editNoteViewModel = ViewModelProvider(this).get(EditNoteViewModel::class.java)
        notesDB = Room.databaseBuilder(applicationContext, NoteDatabase::class.java, "notesDB").allowMainThreadQueries().build()
        foldersDB= Room.databaseBuilder(applicationContext,FolderDatabase::class.java,"foldersDB").allowMainThreadQueries().build()
        setUpToolbar()
        var title="hello"
        var content="world"

        var bundle = intent.extras!!
        var id = bundle.getInt("note_id")
       notesDB.noteDAO().getNotebyID(id).observe(this){
           if(!it.isEmpty()){

               binding?.noteTitleInput?.setText( it.get(0).noteTitle, TextView.BufferType.EDITABLE)
               binding?.noteContentInput?.setText(it.get(0).content,TextView.BufferType.EDITABLE)
           }
       }

//        binding?.noteTitleInput?.setText(title , TextView.BufferType.EDITABLE)
//        binding?.noteContentInput?.setText(content,TextView.BufferType.EDITABLE)
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding?.topapptoolbareditnote)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
//        supportActionBar?.hide()
    }
}