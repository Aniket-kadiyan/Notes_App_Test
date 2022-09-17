package com.example.notesapptest.ui.Edit_Notes

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.notesapptest.MainActivity
import com.example.notesapptest.R
import com.example.notesapptest.data_models.FolderDatabase
import com.example.notesapptest.data_models.NoteDatabase
import com.example.notesapptest.databinding.EditnotelayoutBinding
import com.example.notesapptest.observeOnce
import com.example.notesapptest.ui.Folders.FoldersViewModel
import com.example.notesapptest.ui.Notes.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditNoteActivity : AppCompatActivity() {
    private var _binding : EditnotelayoutBinding?=null
    val binding
    get() =_binding
    @Inject
    lateinit var foldersDB : FolderDatabase
    @Inject
    lateinit var notesDB : NoteDatabase
    private lateinit var foldersViewModel: FoldersViewModel
    private lateinit var notesViewModel: NotesViewModel
    private var editNoteViewModel : EditNoteViewModel?=null
    var id =0
    var folder_id = 0
    var note_title="hello"
    var note_content="world"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= EditnotelayoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        notesViewModel= ViewModelProvider(this).get(NotesViewModel::class.java)
        foldersViewModel= ViewModelProvider(this).get(FoldersViewModel::class.java)
        editNoteViewModel = ViewModelProvider(this).get(EditNoteViewModel::class.java)
//        notesDB = Room.databaseBuilder(applicationContext, NoteDatabase::class.java, "notesDB").allowMainThreadQueries().build()
//        foldersDB= Room.databaseBuilder(applicationContext,FolderDatabase::class.java,"foldersDB").allowMainThreadQueries().build()
        setUpToolbar()


        var bundle = intent.extras!!
        id = bundle.getInt("note_id")
       notesDB.noteDAO().getNotebyID(id).observeOnce(this){
           if(!it.isEmpty()){
               if(it.get(0).noteTitle!="")
                   binding?.noteTitleInput?.setText( it.get(0).noteTitle, TextView.BufferType.EDITABLE)
               if(it.get(0).content!="")
                   binding?.noteContentInput?.setText(it.get(0).content,TextView.BufferType.EDITABLE)
               folder_id = it.get(0).folderId
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
            editnotetopSave.setOnClickListener {
                GlobalScope.launch {
                    if (noteContentInput.text.toString() == "" && noteTitleInput.text.toString() == "")
                        notesDB.noteDAO().deleteNotebyID(id)
                    else {
                        notesDB.noteDAO().updateNoteTitle(noteTitleInput.text.toString(), id)
                        Log.d(
                            "note title update:::",
                            "id = $id  title = ${noteTitleInput.text.toString()}"
                        )
                        notesDB.noteDAO().updateNoteContent(noteContentInput.text.toString(), id)
                    }
                    var intent = Intent(applicationContext, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    this@EditNoteActivity.applicationContext.startActivity(intent)
                }
            }
           saveNoteButton.setOnClickListener(){
               GlobalScope.launch {
                   notesDB.noteDAO().updateNoteTitle(noteTitleInput.text.toString() ,id)
                   Log.d("note title update:::", "id = $id  title = ${noteTitleInput.text.toString()}")
                   notesDB.noteDAO().updateNoteContent(noteContentInput.text.toString(),id)
                   }
               var intent = Intent(applicationContext , MainActivity::class.java)
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
               this@EditNoteActivity.applicationContext.startActivity(intent)
           }
           deleteNoteButton.setOnClickListener(){
               notesDB.noteDAO().deleteNotebyID(id)
               var intent = Intent(applicationContext , MainActivity::class.java)
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
               this@EditNoteActivity.applicationContext.startActivity(intent)
           }
           changeFolderButtoneditNote.setOnClickListener {
               val dialog = BottomSheetDialog(this@EditNoteActivity)
               dialog.setCancelable(true)
               val modalview = layoutInflater.inflate(R.layout.change_folder_bottommodal_layout,null)
               val changefolderRV = modalview.findViewById<RecyclerView>(R.id.changeFolderModalRV)
               val currentFolderTV = modalview.findViewById<TextView>(R.id.currentFoldertextView)
               val nextFolderTV = modalview.findViewById<TextView>(R.id.newFoldertextView)
               val changeButton = modalview.findViewById<Button>(R.id.changeFolderButton)
                currentFolderTV.text = foldersDB.folderDAO().getFolderbyID(folder_id).get(0).folderTitle
               var folderlist = foldersDB.folderDAO().getCurrrentFolderList()
               val adapter = ChangeFolderModalAdapter(folderlist , nextFolderTV)
               changefolderRV.adapter = adapter
               changeButton.setOnClickListener {
                   notesDB.noteDAO().updateFolderID(adapter.selectedfolderid , id)
                   dialog.dismiss()
               }

               dialog.setContentView(modalview)
               dialog.show()
           }
           showOptionsButtoneditNote.setOnClickListener(){
               if( deleteNoteButton.isVisible == false && saveNoteButton.isVisible == false && changeFolderButtoneditNote.isVisible == false ){
                   deleteNoteButton.visibility = Button.VISIBLE
                   deleteNoteButton.isVisible = true
                   saveNoteButton.visibility = Button.VISIBLE
                   saveNoteButton.isVisible = true
                   changeFolderButtoneditNote.visibility = Button.VISIBLE
                   changeFolderButtoneditNote.isVisible = true
               }
               else{
                   deleteNoteButton.visibility = Button.INVISIBLE
                   deleteNoteButton.isVisible = false
                   saveNoteButton.visibility = Button.INVISIBLE
                   saveNoteButton.isVisible = false
                   changeFolderButtoneditNote.visibility = Button.INVISIBLE
                   changeFolderButtoneditNote.isVisible = false
               }
           }
       }

//        binding?.noteTitleInput?.setText(title , TextView.BufferType.EDITABLE)
//        binding?.noteContentInput?.setText(content,TextView.BufferType.EDITABLE)
    }

    override fun onBackPressed() {
        note_title = binding?.noteTitleInput!!.text.toString()
        note_content = binding?.noteContentInput!!.text.toString()

            if(note_content=="" && note_title=="")
                binding?.deleteNoteButton!!.callOnClick()
            else{
                binding?.editnotetopSave?.performClick()
            }
            super.onBackPressed()


    }

    private fun setUpToolbar() {
        setSupportActionBar(binding?.topapptoolbareditnote)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(true)
//        supportActionBar?.hide()
    }
}