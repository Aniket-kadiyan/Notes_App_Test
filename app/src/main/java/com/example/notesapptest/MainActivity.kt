package com.example.notesapptest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.notesapptest.data_models.Folder
import com.example.notesapptest.data_models.FolderDatabase
import com.example.notesapptest.data_models.Note
import com.example.notesapptest.data_models.NoteDatabase
import com.example.notesapptest.databinding.ActivityMainBinding
import com.example.notesapptest.ui.Edit_Notes.EditNoteActivity
import com.example.notesapptest.ui.Edit_Notes.EditNoteViewModel
import com.example.notesapptest.ui.Folders.FoldersViewModel
import com.example.notesapptest.ui.Notes.NotesViewModel
import com.example.notesapptest.ui.View_Notes_in_Folder.ViewNotesinFolderActivity
import com.example.notesapptest.ui.View_Notes_in_Folder.ViewNotesinFolderViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding?=null
    val binding
    get()=_binding

    lateinit var foldersDB : FolderDatabase
    lateinit var notesDB : NoteDatabase
    private lateinit var foldersViewModel: FoldersViewModel
    private lateinit var notesViewModel: NotesViewModel
    private var editNoteViewModel : EditNoteViewModel?=null
    private var viewNotesinFolderViewModel : ViewNotesinFolderViewModel?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        notesViewModel=ViewModelProvider(this).get(NotesViewModel::class.java)
        foldersViewModel=ViewModelProvider(this).get(FoldersViewModel::class.java)
        editNoteViewModel=ViewModelProvider(this).get(EditNoteViewModel::class.java)
        viewNotesinFolderViewModel=ViewModelProvider(this).get(ViewNotesinFolderViewModel::class.java)
        notesDB = Room.databaseBuilder(applicationContext, NoteDatabase::class.java, "notesDB").allowMainThreadQueries().build()
        foldersDB=Room.databaseBuilder(applicationContext,FolderDatabase::class.java,"foldersDB").allowMainThreadQueries().build()

        GlobalScope.launch {
            notesDB.apply {
                noteDAO().apply {
                    if(!this.ifNoteExists("test note 1")){
                        var n = Note(0,"test note 1",2,"this is a test note")
                        addNote(n)
                    }
                    if(!this.ifNoteExists("test note 2")){
                        var n = Note(0,"test note 2",2,"this is a test note")
                        addNote(n)
                    }
                    if(!this.ifNoteExists("test note 3")){
                        var n = Note(0,"test note 3",4,"this is a test note")
                        addNote(n)
                    }
                    if(!this.ifNoteExists("test note 4")){
                        var n = Note(0,"test note 4",2,"this is a test note")
                        addNote(n)
                    }
                    if(!this.ifNoteExists("test note 5")){
                        var n = Note(0,"test note 5",3,"this is a test note with a long content to check out the view. programming FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW  FTW ")
                        addNote(n)
                    }
                    runOnUiThread {
                        notesDB.noteDAO().getNotesList().observe(this@MainActivity){
                            if(it!=null)
                                if(it.isNotEmpty()){
                                    Log.d("NOTES LIST DATA::::", "${it}")
                                    notesViewModel.updateNoteList(it)

                                }
                        }
                    }
                }
            }
            foldersDB.apply {
                folderDAO().apply {
                    if(!this.ifFolderExists("All notes")){
                        var f = Folder(0,"All notes" , 5)
                        addFolder(f)
                    }
                    if(!this.ifFolderExists("test folder 2")){
                        var f = Folder(0,"test folder 2" , 3)
                        addFolder(f)
                    }
                    if(!this.ifFolderExists("test folder 3")){
                        var f = Folder(0,"test folder 3" , 1)
                        addFolder(f)
                    }
                    if(!this.ifFolderExists("test folder 4")){
                        var f = Folder(0,"test folder 4" , 1)
                        addFolder(f)
                    }
                    runOnUiThread {
                        foldersDB.folderDAO().getFolderList().observe(this@MainActivity){
                            if(it!=null)
                                if(it.isNotEmpty()){
                                    Log.d("FOLDERS LIST DATA::::", "${it}")
                                    foldersViewModel.updateFolderList(it)
                                }
                        }
                    }
                }
            }


        }
        notesViewModel.updateDB(notesDB)
        foldersViewModel.updateDB(foldersDB)

        setUpToolbar()

        binding?.apply {
//            val navView: BottomNavigationView = navView
//            setSupportActionBar(topAppBar)
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.foldersFragment , R.id.notesFragment
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            addButton.setOnClickListener(View.OnClickListener {
                if(navController.currentDestination?.id  == R.id.notesFragment){
                    var n = Note(0,"New note",2,"...")
                    GlobalScope.launch{notesDB.noteDAO().addNote(n)}
                    editNoteViewModel!!.setNote(n)
                }
                if(navController.currentDestination?.id == R.id.foldersFragment){
                    var f = Folder(0,"new Folder" , 0)
                    GlobalScope.launch {foldersDB.folderDAO().addFolder(f) }
                    viewNotesinFolderViewModel!!.setViewFolder(f)
                }
            })
        }

        editNoteViewModel!!.getNote().observe(this){
            if(it!=null) {
                var intent = Intent(applicationContext, EditNoteActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                var bundle = Bundle()
                bundle.putInt("note_id",it.noteId)
                intent.putExtras(bundle)
                this.applicationContext.startActivity(intent,bundle)
            }
        }
        viewNotesinFolderViewModel!!.getViewFolder().observe(this){
            if(it!=null) {
                Log.d("main  view folder:::", "$it ")
                var intent = Intent(applicationContext, ViewNotesinFolderActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                var bundle = Bundle()
                bundle.putInt("folder_id", it.folderId)
                bundle.putString("folder_title",it.folderTitle)
                intent.putExtras(bundle)
                this.applicationContext.startActivity(intent,bundle)
            }

        }


    }

    override fun onStart() {
        super.onStart()
        runOnUiThread {
            notesDB.noteDAO().getNotesList().observe(this@MainActivity){
                if(it!=null)
                    if(it.isNotEmpty()){
                        Log.d("NOTES LIST DATA::::", "${it}")
                        notesViewModel.updateNoteList(it)

                    }
            }
        }
        runOnUiThread {
            foldersDB.folderDAO().getFolderList().observe(this@MainActivity){
                if(it!=null)
                    if(it.isNotEmpty()){
                        Log.d("FOLDERS LIST DATA::::", "${it}")
                        foldersViewModel.updateFolderList(it)
                    }
            }
        }


    }

    private fun setUpToolbar() {
        setSupportActionBar(binding?.topapptoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
//        supportActionBar?.hide()
    }

}