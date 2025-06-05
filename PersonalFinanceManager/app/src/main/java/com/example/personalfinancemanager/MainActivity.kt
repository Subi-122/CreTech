package com.example.personalfinancemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.personalfinancemanager.ui.theme.PersonalFinanceManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create your DAO, Repository, and ViewModelFactory here
        val dao = TransactionDatabase.getDatabase(applicationContext).transactionDao()
        val repository = TransactionRepository(dao)
        val factory = TransactionViewModelFactory(repository)

        setContent {
            PersonalFinanceManagerTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier.padding(paddingValues),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Pass the factory to the NavGraph
                        NavGraph(navController = navController, viewModelFactory = factory)
                    }
                }
            }
        }
    }
}
