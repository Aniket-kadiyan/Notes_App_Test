package com.example.notesapptest.ui.View_Notes_in_Folder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapptest.data_models.Folder

class ViewNotesinFolderViewModel : ViewModel() {
    private val viewFolder = MutableLiveData<Folder>()

    fun getViewFolder() : MutableLiveData<Folder> = viewFolder
     fun setViewFolder(folder : Folder){
         viewFolder.value =folder
     }
}