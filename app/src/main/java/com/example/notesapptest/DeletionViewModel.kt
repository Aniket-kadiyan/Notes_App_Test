package com.example.notesapptest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapptest.data_models.Folder
import com.example.notesapptest.data_models.Note
import com.example.notesapptest.retrofit_test.QuotesAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeletionViewModel @Inject constructor(private val repo: QuotesAPI): ViewModel() {
    private val notedeleteList = MutableLiveData<List<Note>>()
    private val folderdeleteList = MutableLiveData<List<Folder>>()

    fun getfolderdeleteList() : MutableLiveData<List<Folder>> = folderdeleteList
    fun getnotedeleteList() : MutableLiveData<List<Note>> = notedeleteList
    fun setfolderdeleteList(list : List<Folder>){
        folderdeleteList.value=list
    }
    fun setnotedeleteList(list : List<Note>){
        notedeleteList.value=list
    }
}