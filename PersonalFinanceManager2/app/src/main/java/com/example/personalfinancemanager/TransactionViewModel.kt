package com.example.personalfinancemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    val transactions = repository.getAllTransactions().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    var monthlyBudget by mutableStateOf(0.0)
        private set

    fun addTransaction(title: String, amount: Double, type: String) {
        viewModelScope.launch {
            repository.insert(Transaction(title = title, amount = amount, type = type))
        }
    }

    fun setBudget(amount: Double) {
        monthlyBudget = amount
    }

    fun totalExpenses(): Double {
        return transactions.value.filter { it.type == "Expense" }.sumOf { it.amount }
    }

    fun remainingBudget(): Double {
        return monthlyBudget - totalExpenses()
    }
}
