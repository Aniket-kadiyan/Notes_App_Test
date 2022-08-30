package com.example.notesapptest

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
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
import com.example.notesapptest.ui.Folders.FoldersViewModel
import com.example.notesapptest.ui.Notes.NotesViewModel
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        notesViewModel=ViewModelProvider(this).get(NotesViewModel::class.java)
        foldersViewModel=ViewModelProvider(this).get(FoldersViewModel::class.java)
        notesDB = Room.databaseBuilder(applicationContext, NoteDatabase::class.java, "notesDB").allowMainThreadQueries().build()
        foldersDB=Room.databaseBuilder(applicationContext,FolderDatabase::class.java,"foldersDB").allowMainThreadQueries().build()

        GlobalScope.launch {
            notesDB.apply {
                noteDAO().apply {
                    if(!this.ifNoteExists("test note 1")){
                        var n = Note(0,"test note 1",1,"this is a test note")
                        addNote(n)
                    }
                    if(!this.ifNoteExists("test note 2")){
                        var n = Note(0,"test note 2",1,"this is a test note")
                        addNote(n)
                    }
                    if(!this.ifNoteExists("test note 3")){
                        var n = Note(0,"test note 3",1,"this is a test note")
                        addNote(n)
                    }
                    if(!this.ifNoteExists("test note 4")){
                        var n = Note(0,"test note 4",2,"this is a test note")
                        addNote(n)
                    }
                    //
                    // Update Notes list to viewmodel here
                    //
                }
            }
            foldersDB.apply {
                folderDAO().apply {
                    if(!this.ifFolderExists("test folder 1")){
                        var f = Folder(0,"test folder 1" , 3)
                        addFolder(f)
                    }
                    if(!this.ifFolderExists("test folder 2")){
                        var f = Folder(0,"test folder 2" , 1)
                        addFolder(f)
                    }
                    if(!this.ifFolderExists("test folder 3")){
                        var f = Folder(0,"test folder 3" , 0)
                        addFolder(f)
                    }
                    if(!this.ifFolderExists("test folder 4")){
                        var f = Folder(0,"test folder 4" , 0)
                        addFolder(f)
                    }
                    //
                    //update folder list to viewmodel here
                    //
                }
            }

        }
        binding?.apply {
//            val navView: BottomNavigationView = navView

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
        }


    }

}