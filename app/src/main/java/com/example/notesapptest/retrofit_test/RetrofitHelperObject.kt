package com.example.notesapptest.retrofit_test

import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelperObject {
   val baseUrl = "https://quotable.io/"

    fun getInstance() : Retrofit{
        return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
    }


}