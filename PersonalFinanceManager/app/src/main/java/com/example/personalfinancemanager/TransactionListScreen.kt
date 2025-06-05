package com.example.personalfinancemanager

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState


@Composable
fun TransactionListScreen(transactionViewModel: TransactionViewModel) {

    val transactions = transactionViewModel.transactions.collectAsState().value

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Transaction List", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        if (transactions.isEmpty()) {
            Text("No transactions yet.")
        } else {
            transactions.forEach {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (it.type == "Income") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(it.title)
                            Text(it.type, style = MaterialTheme.typography.labelSmall)
                        }
                        Text("₹${it.amount}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
