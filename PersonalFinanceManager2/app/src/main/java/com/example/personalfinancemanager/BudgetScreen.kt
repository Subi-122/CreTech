package com.example.personalfinancemanager

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BudgetScreen(transactionViewModel: TransactionViewModel) {
    var budgetInput by remember { mutableStateOf("") }

    val totalExpenses = transactionViewModel.totalExpenses()
    val remaining = transactionViewModel.remainingBudget()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Set Monthly Budget", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = budgetInput,
            onValueChange = { budgetInput = it },
            label = { Text("Budget Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val amount = budgetInput.toDoubleOrNull()
                if (amount != null) {
                    transactionViewModel.setBudget(amount)
                    budgetInput = ""
                }
            }
        ) {
            Text("Set Budget")
        }

        Divider()

        Text("Monthly Budget: ₹${transactionViewModel.monthlyBudget}")
        Text("Total Expenses: ₹$totalExpenses")
        Text("Remaining Balance: ₹$remaining")
    }
}
