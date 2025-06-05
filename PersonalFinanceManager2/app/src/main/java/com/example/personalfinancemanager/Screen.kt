package com.example.personalfinancemanager

sealed class Screen(val route: String) {
    object LogTransaction : Screen("log_transaction")
    object TransactionList : Screen("transaction_list")
    object Budget : Screen("budget")
    object Goals : Screen("goals")
}
