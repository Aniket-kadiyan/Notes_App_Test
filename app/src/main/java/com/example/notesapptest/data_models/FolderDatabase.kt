package com.example.notesapptest.data_models

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (entities = [Folder::class], version = 1)
abstract class FolderDatabase : RoomDatabase() {
    abstract fun folderDAO() : FolderDAO
}