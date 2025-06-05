package com.example.personalfinancemanager

import kotlinx.coroutines.flow.Flow

class GoalRepository(private val goalDao: GoalDao) {

    fun getAllGoals(): Flow<List<Goal>> = goalDao.getAllGoals()

    suspend fun insert(goal: Goal) {
        goalDao.insertGoal(goal)
    }

    suspend fun delete(goal: Goal) {
        goalDao.deleteGoal(goal)
    }
}
