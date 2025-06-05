package com.example.personalfinancemanager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItems(
    val name: String,
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("Log", Screen.LogTransaction.route, Icons.Default.Add),
    BottomNavItem("Transactions", Screen.TransactionList.route, Icons.Default.List),
    BottomNavItem("Budget", Screen.Budget.route, Icons.Default.AccountBalance) // ✅ correct icon
)

