package com.example.notesapptest.ui.Edit_Notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapptest.data_models.Note

class EditNoteViewModel : ViewModel() {
    private val note = MutableLiveData<Note>()

    fun getNote():MutableLiveData<Note> = note

    fun setNote(n: Note){
        note.value=n
    }
}
