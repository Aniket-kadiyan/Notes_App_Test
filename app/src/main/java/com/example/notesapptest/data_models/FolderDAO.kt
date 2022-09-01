package com.example.notesapptest.data_models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FolderDAO {

    @Insert ( onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFolder(folder : Folder)

    @Update ( onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFolder(folder: Folder)

    @Delete
    suspend fun deleteFolder(folder : Folder)

    @Query("SELECT * FROM Folders")
    fun getFolderList(): LiveData<List<Folder>>

    @Query("SELECT * FROM Folders")
    fun getCurrrentFolderList(): List<Folder>

    //for initial testing only. Not required in actual operation
    // can also be done using folder title
    @Query("SELECT * FROM Folders WHERE title=:title")
    fun ifFolderExists( title:String) : Boolean

    @Query("UPDATE Folders SET notes_count=notes_count+1 WHERE folderId=:id")
    fun incrementNoteCount(id:Int)

    @Query("UPDATE Folders SET notes_count= :newcount WHERE folderId=:id")
    fun updateNoteCount(newcount :Int , id:Int)

    @Query("Delete from Folders where folderId = :id")
    fun deleteFolderbyID(id: Int)

}