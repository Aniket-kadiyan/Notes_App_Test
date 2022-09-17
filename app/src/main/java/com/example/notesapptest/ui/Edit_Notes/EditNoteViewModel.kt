package com.example.notesapptest.ui.Edit_Notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapptest.data_models.Note
import com.example.notesapptest.retrofit_test.QuotesAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(private val repo: QuotesAPI): ViewModel() {
    private val note = MutableLiveData<Note>()

    fun getNote():MutableLiveData<Note> = note

    fun setNote(n: Note){
        note.value=n
    }
}
