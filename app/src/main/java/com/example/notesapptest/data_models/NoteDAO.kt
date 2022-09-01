package com.example.notesapptest.data_models

import android.icu.text.CaseMap
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDAO {
    @Insert( onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(folder : Note)

    @Update( onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(folder: Note)

    @Delete
    suspend fun deleteNote(folder : Note)

    @Query("SELECT * FROM Notes")
    fun getNotesList(): LiveData<List<Note>>

    @Query("SELECT * FROM Notes")
    fun getCurrentNotesList(): List<Note>

    //for initial testing only. Not required in actual operation
    @Query("SELECT * FROM Notes WHERE title=:title")
    fun ifNoteExists( title: String) : Boolean

    @Query("SELECT * FROM Notes WHERE folder=:id")
    fun getNotesinFolder(id :Int) : LiveData<List<Note>>

    @Query("SELECT * FROM Notes WHERE folder=:id")
    fun getCurrentNotesinFolder(id :Int) : List<Note>

    @Query("SELECT * FROM Notes WHERE noteId=:id")
    fun getNotebyID(id: Int) : LiveData<List<Note>>

    @Query("UPDATE Notes SET title = :title WHERE noteId = :id")
    fun updateNoteTitle(title : String,  id : Int)

    @Query("UPDATE Notes SET content = :content WHERE noteId = :id")
    fun updateNoteContent(content : String,  id : Int)

    @Query("Delete from Notes where noteId = :id")
    fun deleteNotebyID(id: Int)
}