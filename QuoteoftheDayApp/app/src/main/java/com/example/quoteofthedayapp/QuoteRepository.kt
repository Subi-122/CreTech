package com.example.quoteofthedayapp.data

import com.example.quoteofthedayapp.Quote
import com.example.quoteofthedayapp.QuoteDao

class QuoteRepository(private val dao: QuoteDao) {

    suspend fun getAllQuotes(): List<Quote> = dao.getAllQuotes()

    suspend fun getBookmarkedQuotes(): List<Quote> = dao.getBookmarkedQuotes()

    suspend fun insertQuote(quote: Quote) {
        dao.insert(quote)
    }

    suspend fun toggleBookmark(quote: Quote) {
        val updatedQuote = quote.copy(isBookmarked = !quote.isBookmarked)
        dao.updateQuote(updatedQuote)
    }
}
