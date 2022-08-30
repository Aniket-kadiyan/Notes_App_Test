package com.example.notesapptest.data_models

import android.icu.text.CaseMap
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDAO {
    @Insert( onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(folder : Note)

    @Update
    suspend fun updateNote(folder: Note)

    @Delete
    suspend fun deleteNote(folder : Note)

    @Query("SELECT * FROM Notes")
    fun getNotesList(): LiveData<List<Note>>

    //for initial testing only. Not required in actual operation
    @Query("SELECT * FROM Notes WHERE title=:title")
    fun ifNoteExists( title: String) : Boolean
}