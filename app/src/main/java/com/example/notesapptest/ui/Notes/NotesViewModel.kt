package com.example.notesapptest.ui.Notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapptest.data_models.Folder
import com.example.notesapptest.data_models.FolderDatabase
import com.example.notesapptest.data_models.Note
import com.example.notesapptest.data_models.NoteDatabase

class NotesViewModel : ViewModel() {

    private val noteList = MutableLiveData<List<Note>>()
    private val notesDB = MutableLiveData<NoteDatabase>()

    fun updateDB(db: NoteDatabase){
        notesDB.value=db
    }
    fun getDB(): MutableLiveData<NoteDatabase> = notesDB

    fun updateFolderList (list: List<Note>){
        noteList.value=list
    }
    fun getFolderList(): MutableLiveData<List<Note>> = noteList

    private val _text = MutableLiveData<String>().apply {
        value = "This is notes Fragment"
    }
    val text: LiveData<String> = _text
}