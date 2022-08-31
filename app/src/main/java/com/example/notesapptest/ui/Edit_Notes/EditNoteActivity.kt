package com.example.notesapptest.ui.Edit_Notes

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.notesapptest.MainActivity
import com.example.notesapptest.data_models.FolderDatabase
import com.example.notesapptest.data_models.NoteDatabase
import com.example.notesapptest.databinding.EditnotelayoutBinding
import com.example.notesapptest.ui.Folders.FoldersViewModel
import com.example.notesapptest.ui.Notes.NotesViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
       binding?.apply {
//            noteTitleInput.addTextChangedListener(object : TextWatcher{
//                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
//                override fun afterTextChanged(p0: Editable?) {
//                    GlobalScope.launch {
//                        notesDB.noteDAO().updateNote(p0.toString() , noteContentInput.text.toString() , id )
//                    }
//                }
//                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//                }
//            })
//           noteContentInput.addTextChangedListener(object: TextWatcher{
//               override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
//               override fun afterTextChanged(p0: Editable?) {
//                   GlobalScope.launch {
//                       notesDB.noteDAO().updateNote(noteTitleInput.text.toString() ,p0.toString() ,  id )
//                   }
//               }
//               override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//               }
//           })

           saveNoteButton.setOnClickListener(){
               GlobalScope.launch {
                   notesDB.noteDAO().updateNoteTitle(noteTitleInput.text.toString() ,id)
                   Log.d("note title update:::", "id = $id  title = ${noteTitleInput.text.toString()}")
                   notesDB.noteDAO().updateNoteContent(noteContentInput.text.toString(),id)

                   }
               var intent = Intent(applicationContext , MainActivity::class.java)
               this@EditNoteActivity.applicationContext.startActivity(intent)
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