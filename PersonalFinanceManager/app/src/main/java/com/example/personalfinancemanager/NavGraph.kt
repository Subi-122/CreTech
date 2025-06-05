package com.example.personalfinancemanager

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController, viewModelFactory: TransactionViewModelFactory) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Screen.LogTransaction.route) {

        composable(Screen.LogTransaction.route) {
            val transactionViewModel: TransactionViewModel = viewModel(factory = viewModelFactory)
            LogTransactionScreen(transactionViewModel)
        }

        composable(Screen.TransactionList.route) {
            val transactionViewModel: TransactionViewModel = viewModel(factory = viewModelFactory)
            TransactionListScreen(transactionViewModel)
        }

        composable(Screen.Budget.route) {
            val transactionViewModel: TransactionViewModel = viewModel(factory = viewModelFactory)
            BudgetScreen(transactionViewModel)
        }

        composable(Screen.Goals.route) {
            val goalViewModel: GoalViewModel = viewModel(
                factory = GoalViewModelFactory(
                    GoalRepository(GoalDatabase.getDatabase(context).goalDao())
                )
            )
            GoalScreen(goalViewModel)
        }
    }
}
