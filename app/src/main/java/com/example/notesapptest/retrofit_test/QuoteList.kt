package com.example.notesapptest.retrofit_test

data class QuoteList (
    val count: Int,
    val lastItemIndex: Int,
    val page: Int,
    val results: List<Result>,
    val totalCount: Int,
    val totalPages: Int
        )