package com.example.notesapptest.ui.View_Notes_in_Folder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapptest.data_models.Folder
import com.example.notesapptest.retrofit_test.QuotesAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewNotesinFolderViewModel @Inject constructor(private val repo: QuotesAPI): ViewModel() {

    private val _view_folder = MutableLiveData<Folder>()

    val view_folder
        get() = _view_folder

    fun getViewFolder() : MutableLiveData<Folder> = _view_folder
     fun setViewFolder(folder : Folder){
         _view_folder.value =folder
     }



}