package com.example.notesapptest.ui.View_Notes_in_Folder


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.notesapptest.DeletionViewModel
import com.example.notesapptest.MainActivity
import com.example.notesapptest.R
import com.example.notesapptest.data_models.FolderDatabase
import com.example.notesapptest.data_models.Note
import com.example.notesapptest.data_models.NoteDatabase
import com.example.notesapptest.databinding.ViewNotesInFolderLayoutBinding
import com.example.notesapptest.observeOnce
import com.example.notesapptest.ui.Edit_Notes.EditNoteActivity
import com.example.notesapptest.ui.Edit_Notes.EditNoteViewModel
import com.example.notesapptest.ui.Folders.FoldersViewModel
import com.example.notesapptest.ui.Notes.NoteAdapter
import com.example.notesapptest.ui.Notes.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ViewNotesinFolderActivity : AppCompatActivity() {
    private var _binding : ViewNotesInFolderLayoutBinding?=null
    val binding
    get() =_binding!!

    private val notesViewModel : NotesViewModel by viewModels()
    private val editNoteViewModel : EditNoteViewModel by viewModels()
    private val foldersViewModel : FoldersViewModel by viewModels()
    private val viewNotesinFolderViewModel : ViewNotesinFolderViewModel by viewModels()
    private val deletionViewModel : DeletionViewModel by viewModels()
    @Inject
    lateinit var notesDB : NoteDatabase
    @Inject
    lateinit var foldersDB : FolderDatabase
    var id =0
    var title = "New Folder"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ViewNotesInFolderLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        notesViewModel=ViewModelProvider(this).get(NotesViewModel::class.java)
//        foldersViewModel=ViewModelProvider(this).get(FoldersViewModel::class.java)
//        editNoteViewModel = ViewModelProvider(this).get(EditNoteViewModel::class.java)
//        viewNotesinFolderViewModel=ViewModelProvider(this).get(ViewNotesinFolderViewModel::class.java)
//        deletionViewModel = ViewModelProvider(this).get(DeletionViewModel::class.java)
//        notesDB = Room.databaseBuilder(applicationContext, NoteDatabase::class.java, "notesDB").allowMainThreadQueries().build()
//        foldersDB= Room.databaseBuilder(applicationContext, FolderDatabase::class.java,"foldersDB").allowMainThreadQueries().build()
        val folderid =-1
        val bundle = intent.extras
        id = bundle?.getInt("folder_id")!!
        title = bundle?.getString("folder_title")!!
        var dellist = notesDB.noteDAO().getCurrentNotesList()
        binding?.topapptoolbarviewNoteinFolder?.title = title

        setUpToolbar()
        Log.d("viewfolder:::", ":::$id ")
        fun updateView(){
            if(id==1){
                runOnUiThread {
                    notesDB.noteDAO().getNotesList().observe(this){
                        if(it!=null)
                            if(it.isNotEmpty()){
                                Log.d("NOTES LIST DATA::::", "in view notes in folder:::${it}")
                                var adapter = NoteAdapter(it , editNoteViewModel!!,deletionViewModel!!,1)
                                binding?.viewNotesinFolderRV?.adapter =adapter
                                dellist=adapter.delList
                                if(dellist!=null)
                                    if(dellist.isNotEmpty()){
                                        binding.noteinfoldertopDelete.visibility=ImageButton.VISIBLE
                                        binding.noteinfoldertopDelete.isVisible=true
                                    }
                            }
                    }
                }
            }
            else{
                runOnUiThread {
                    notesDB.noteDAO().getNotesinFolder(id!!).observe(this){
                        if(it!=null)
                            if(it.isNotEmpty()){
                                Log.d("NOTES LIST DATA::::", " in view notes in folder::: ${it}")
                                var adapter = NoteAdapter(it , editNoteViewModel!!,deletionViewModel!!,1)

                                binding?.viewNotesinFolderRV?.adapter =adapter
                                dellist=adapter.delList
                                if(dellist!=null)
                                    if(dellist.isNotEmpty()){
                                        binding.noteinfoldertopDelete.visibility=ImageButton.VISIBLE
                                        binding.noteinfoldertopDelete.isVisible=true
                                    }
                            }
                    }
                }

            }
        }
        if(id==1){
            runOnUiThread {
                notesDB.noteDAO().getNotesList().observe(this){
                    if(it!=null)
                        if(it.isNotEmpty()){
                            Log.d("NOTES LIST DATA::::", "in view notes in folder:::${it}")
                            var adapter = NoteAdapter(it , editNoteViewModel!!,deletionViewModel!!,1)
                            binding?.viewNotesinFolderRV?.adapter =adapter
                            dellist=adapter.delList
                            if(dellist!=null)
                                if(dellist.isNotEmpty()){
                                    binding.noteinfoldertopDelete.visibility=ImageButton.VISIBLE
                                    binding.noteinfoldertopDelete.isVisible=true
                                }
                        }
                }
            }
        }
        else{
            runOnUiThread {
                notesDB.noteDAO().getNotesinFolder(id!!).observe(this){
                    if(it!=null)
                        if(it.isNotEmpty()){
                            Log.d("NOTES LIST DATA::::", " in view notes in folder::: ${it}")
                            var adapter = NoteAdapter(it , editNoteViewModel!!,deletionViewModel!!,1)

                            binding?.viewNotesinFolderRV?.adapter =adapter
                            dellist=adapter.delList
                            if(dellist!=null)
                                if(dellist.isNotEmpty()){
                                    binding.noteinfoldertopDelete.visibility=ImageButton.VISIBLE
                                    binding.noteinfoldertopDelete.isVisible=true
                                }
                        }
                }
            }

        }
        binding?.apply {
            topapptoolbarviewNoteinFolder.title=title
            topapptoolbarviewNoteinFolder.setBackgroundColor(foldersDB.folderDAO().getFolderbyID(id)[0].folderColor.toColorInt())
            noteinfoldertopDelete.setOnClickListener{
                deletionViewModel!!.getnotedeleteList().observeOnce(this@ViewNotesinFolderActivity){
                        for(note in it){
//                            GlobalScope.launch { notesDB.noteDAO().deleteNote(note) }
//                            runOnUiThread {
//                                notesDB.noteDAO().getNotesList().observe(this@ViewNotesinFolderActivity){
//                                    if(it!=null)
//                                        if(it.isNotEmpty()){
//                                        Log.d("NOTES LIST DATA::::", "${it}")
//                                    notesViewModel.updateNoteList(it)
//                              }
//                                }
//                            }
                            updateView()
                        }
                    }
                    noteinfoldertopDelete.visibility=ImageButton.INVISIBLE
                    noteinfoldertopDelete.isVisible=false

            }

            deletionViewModel!!.getnotedeleteList().observe(this@ViewNotesinFolderActivity){
                if(it !=null)
                    if(it.isNotEmpty()) {
                        noteinfoldertopDelete.visibility=ImageButton.VISIBLE
                        noteinfoldertopDelete.isVisible=true
                    }
                    else{
                        noteinfoldertopDelete.visibility=ImageButton.INVISIBLE
                        noteinfoldertopDelete.isVisible=false
                    }
            }

            showOptionsButton.setOnClickListener(){
                if( addNoteinFolderButton.isVisible == false && deleteFolderButton.isVisible == false ){
                    addNoteinFolderButton.visibility = Button.VISIBLE
                    addNoteinFolderButton.isVisible = true
                    deleteFolderButton.visibility = Button.VISIBLE
                    deleteFolderButton.isVisible = true
                }
                else{
                    addNoteinFolderButton.visibility = Button.INVISIBLE
                    addNoteinFolderButton.isVisible = false
                    deleteFolderButton.visibility = Button.INVISIBLE
                    deleteFolderButton.isVisible = false
                }
            }
            addNoteinFolderButton.setOnClickListener(){
                var n = Note(0,"New note",id,"...")
                GlobalScope.launch{
                    notesDB.noteDAO().addNote(n)
                    foldersDB.folderDAO().incrementNoteCount(1)
                }
                notesDB.noteDAO().getNotesList().observe(this@ViewNotesinFolderActivity){
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            Log.d("note being created 2", "  $it \n::::: ${it.last()} ")
                            editNoteViewModel!!.setNote(it.last())
                        }
                    }
                }
            }
            deleteFolderButton.setOnClickListener(){
                var notelist = notesDB.noteDAO().getCurrentNotesinFolder(id)
                for(item in notelist){
                    GlobalScope.launch {
                        notesDB.noteDAO().deleteNote(item)

                    }
                }
                foldersDB.folderDAO().deleteFolderbyID(id)
                var intent = Intent(applicationContext , MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this@ViewNotesinFolderActivity.applicationContext.startActivity(intent)
            }
        }
        editNoteViewModel!!.getNote().observe(this){
            if(it!=null) {
                var intent = Intent(applicationContext, EditNoteActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                var bundle = Bundle()
                bundle.putInt("note_id",it.noteId)
                intent.putExtras(bundle)
                this.applicationContext.startActivity(intent)
            }
        }




    }

    override fun onResume() {
        super.onResume()
        if(id==1){
            runOnUiThread {
                notesDB.noteDAO().getNotesList().observe(this){
                    if(it!=null)
                        if(it.isNotEmpty()){
                            Log.d("NOTES LIST DATA::::", "in view notes in folder:::${it}")
                            var adapter = NoteAdapter(it , editNoteViewModel!!,deletionViewModel!!,1)
                            binding?.viewNotesinFolderRV?.adapter =adapter
                        }
                }
            }
        }
        else{
            runOnUiThread {
                notesDB.noteDAO().getNotesinFolder(id!!).observe(this){
                    if(it!=null)
                        if(it.isNotEmpty()){
                            Log.d("NOTES LIST DATA::::", " in view notes in folder::: ${it}")
                            var adapter = NoteAdapter(it , editNoteViewModel!!,deletionViewModel!!,1)

                            binding?.viewNotesinFolderRV?.adapter =adapter
                        }
                }
            }

        }

    }

    override fun onStart() {
        super.onStart()
        if(id==1){
            runOnUiThread {
                notesDB.noteDAO().getNotesList().observe(this){
                    if(it!=null)
                        if(it.isNotEmpty()){
                            Log.d("NOTES LIST DATA::::", "in view notes in folder:::${it}")
                            var adapter = NoteAdapter(it , editNoteViewModel!!,deletionViewModel!!,1)
                            binding?.viewNotesinFolderRV?.adapter =adapter
                        }
                }
            }
        }
        else{
            runOnUiThread {
                notesDB.noteDAO().getNotesinFolder(id!!).observe(this){
                    if(it!=null)
                        if(it.isNotEmpty()){
                            Log.d("NOTES LIST DATA::::", " in view notes in folder::: ${it}")
                            var adapter = NoteAdapter(it , editNoteViewModel!!,deletionViewModel!!,1)

                            binding?.viewNotesinFolderRV?.adapter =adapter
                        }
                }
            }

        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.delete_item ,menu)
//        return true
//    }


    private fun setUpToolbar() {
        setSupportActionBar(binding?.topapptoolbarviewNoteinFolder)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setTitle(title)

//        supportActionBar?.hide()
    }
}