package com.example.notesapptest.ui.Notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapptest.data_models.Folder
import com.example.notesapptest.data_models.FolderDatabase
import com.example.notesapptest.data_models.Note
import com.example.notesapptest.data_models.NoteDatabase
import com.example.notesapptest.retrofit_test.QuotesAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val repo: QuotesAPI) : ViewModel() {

    private val noteList = MutableLiveData<List<Note>>()
    private val notesDB = MutableLiveData<NoteDatabase>()


    fun updateDB(db: NoteDatabase){
        notesDB.value=db
    }
    fun getDB(): MutableLiveData<NoteDatabase> = notesDB

    fun updateNoteList (list: List<Note>){
        noteList.value=list
    }
    fun getNoteList(): MutableLiveData<List<Note>> = noteList

    private val _text = MutableLiveData<String>().apply {
        value = "Add new Note"
    }

    val text: LiveData<String> = _text
}