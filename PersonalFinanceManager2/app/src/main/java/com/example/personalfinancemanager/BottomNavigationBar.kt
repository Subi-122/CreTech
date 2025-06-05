package com.example.personalfinancemanager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(val name: String, val route: String, val icon: ImageVector)

val bottomNavMenuItems = listOf(
    BottomNavItem("Log", Screen.LogTransaction.route, Icons.Default.Add),
    BottomNavItem("Transactions", Screen.TransactionList.route, Icons.Default.List),
    BottomNavItem("Budget", Screen.Budget.route, Icons.Default.AccountBalance),
    BottomNavItem("Goals", Screen.Goals.route, Icons.Default.Star) // New item added here
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    NavigationBar {
        bottomNavMenuItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.name) },
                label = { Text(item.name) }
            )
        }
    }
}
