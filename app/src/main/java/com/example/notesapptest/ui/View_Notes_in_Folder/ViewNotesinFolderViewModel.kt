package com.example.notesapptest.ui.View_Notes_in_Folder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapptest.data_models.Folder
import com.example.notesapptest.retrofit_test.QuotesAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewNotesinFolderViewModel @Inject constructor(private val repo: QuotesAPI): ViewModel() {
    private val viewFolder = MutableLiveData<Folder>()

    fun getViewFolder() : MutableLiveData<Folder> = viewFolder
     fun setViewFolder(folder : Folder){
         viewFolder.value =folder
     }



}