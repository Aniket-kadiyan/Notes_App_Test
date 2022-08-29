package com.example.notesapptest.data_models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FolderDAO {

    @Insert ( onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFolder(folder : Folder)

    @Update
    suspend fun updateFolder(folder: Folder)

    @Delete
    suspend fun deleteFolder(folder : Folder)

    @Query("SELECT * FROM Folders")
    fun getFolderList(): LiveData<List<Folder>>

    //for initial testing only. Not required in actual operation
    // can also be done using folder title
    @Query("SELECT * FROM Folders WHERE folderId = :id")
    fun ifFolderExists( id : Int) : Boolean

}