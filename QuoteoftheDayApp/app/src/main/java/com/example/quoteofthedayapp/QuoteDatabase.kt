package com.example.quoteofthedayapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Quote::class], version = 1)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao

    companion object {
        @Volatile private var INSTANCE: QuoteDatabase? = null

        fun getInstance(context: Context): QuoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuoteDatabase::class.java,
                    "quote_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
