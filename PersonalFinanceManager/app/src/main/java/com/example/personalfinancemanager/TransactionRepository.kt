package com.example.personalfinancemanager

import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val dao: TransactionDao) {
    suspend fun insert(transaction: Transaction) {
        dao.insert(transaction)
    }

    fun getAllTransactions(): Flow<List<Transaction>> = dao.getAll()
}
