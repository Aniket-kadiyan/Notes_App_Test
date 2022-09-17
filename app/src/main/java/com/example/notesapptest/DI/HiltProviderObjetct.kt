package com.example.notesapptest.DI

import android.content.Context
import androidx.room.Room
import com.example.notesapptest.data_models.FolderDatabase
import com.example.notesapptest.data_models.NoteDatabase
import com.example.notesapptest.retrofit_test.QuotesAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HiltProviderObjetct {



    @Provides
    @Singleton
    fun provideNotesDB(@ApplicationContext context: Context) : NoteDatabase{
        return Room.databaseBuilder(context, NoteDatabase::class.java, "notesDB").allowMainThreadQueries().build()
    }


    @Provides
    @Singleton
    fun provideFoldersDB(@ApplicationContext context: Context): FolderDatabase{
        return Room.databaseBuilder(context, FolderDatabase::class.java,"foldersDB").allowMainThreadQueries().build()
    }



    @Provides
    @Singleton
    fun provideRetrofit() :QuotesAPI{
        return Retrofit.Builder().baseUrl("https://quotable.io/").addConverterFactory(
            GsonConverterFactory.create()).build().create(QuotesAPI::class.java)
    }



}