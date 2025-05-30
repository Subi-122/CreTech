package com.example.quoteofthedayapp

import androidx.room.*

@Dao
interface QuoteDao {

    @Query("SELECT * FROM quotes")
    suspend fun getAllQuotes(): List<Quote>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quote: Quote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quotes: List<Quote>)

    @Update
    suspend fun updateQuote(quote: Quote)

    @Query("SELECT * FROM quotes LIMIT 1 OFFSET :index")
    suspend fun getQuoteByIndex(index: Int): Quote

    @Query("SELECT * FROM quotes WHERE isBookmarked = 1")
    suspend fun getBookmarkedQuotes(): List<Quote>
}
