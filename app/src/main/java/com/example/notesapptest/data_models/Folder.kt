package com.example.notesapptest.data_models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Folders" )
data class Folder(
    @PrimaryKey (autoGenerate = true) val folderId : Int,
    @ColumnInfo (name = "title") val folderTitle : String,
    @ColumnInfo (name = "notes_count") val notesCount : Int,
    @ColumnInfo (name = "folder_color") val folderColor : String
)