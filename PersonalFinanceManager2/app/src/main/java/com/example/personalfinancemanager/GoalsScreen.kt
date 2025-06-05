package com.example.personalfinancemanager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun GoalScreen(goalViewModel: GoalViewModel) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    val goals = goalViewModel.goals.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Set a Financial Goal", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Goal Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val amt = amount.toDoubleOrNull()
                if (title.isNotBlank() && amt != null) {
                    goalViewModel.addGoal(title, amt)
                    title = ""
                    amount = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Goal")
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Text("Your Goals:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))

        LazyColumn {
            items(goals) { goal ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(goal.title, style = MaterialTheme.typography.bodyLarge)
                            Text("Target: ₹${goal.targetAmount}", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { goalViewModel.deleteGoal(goal) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Goal"
                            )
                        }
                    }
                }
            }
        }
    }
}
