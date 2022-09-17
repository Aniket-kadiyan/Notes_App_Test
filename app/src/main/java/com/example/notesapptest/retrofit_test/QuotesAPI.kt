package com.example.notesapptest.retrofit_test

import retrofit2.Response
import retrofit2.http.GET

interface QuotesAPI {
    @GET("/quotes")
    suspend fun getQuotes() : Response<QuoteList>
}