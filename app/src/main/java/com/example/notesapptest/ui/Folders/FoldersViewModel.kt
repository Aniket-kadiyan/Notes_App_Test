package com.example.notesapptest.ui.Folders

import android.icu.text.CaseMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapptest.data_models.Folder
import com.example.notesapptest.data_models.FolderDatabase
import com.example.notesapptest.retrofit_test.QuotesAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FoldersViewModel @Inject constructor(private val repo: QuotesAPI)  : ViewModel() {

    private val folderList = MutableLiveData<List<Folder>>()
    private val foldersDB = MutableLiveData<FolderDatabase>()

    fun updateDB(db: FolderDatabase){
        foldersDB.value=db
    }
    fun getDB(): MutableLiveData<FolderDatabase> = foldersDB

    fun updateFolderList (list: List<Folder>){
        folderList.value=list
    }
    fun getFolderList(): MutableLiveData<List<Folder>> = folderList

    private val _text = MutableLiveData<String>().apply {
        value = "This is folder Fragment"
    }
    val text: LiveData<String> = _text
}