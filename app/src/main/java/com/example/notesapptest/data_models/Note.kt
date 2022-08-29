package com.example.notesapptest.data_models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes")
data class Note (
    @PrimaryKey(autoGenerate = true)  val noteId :Int,
    @ColumnInfo (name = "title")val noteTitle : String,
    @ColumnInfo (name = "folder") val folderId : Int,
    @ColumnInfo (name = "content") val content : String
)