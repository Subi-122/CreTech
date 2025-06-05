package com.example.personalfinancemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GoalViewModel(private val repository: GoalRepository) : ViewModel() {

    val goals = repository.getAllGoals().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addGoal(title: String, amount: Double) {
        viewModelScope.launch {
            repository.insert(Goal(title = title, targetAmount = amount))
        }
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            repository.delete(goal)
        }
    }
}
