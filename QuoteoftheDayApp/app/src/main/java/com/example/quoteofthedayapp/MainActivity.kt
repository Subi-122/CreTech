package com.example.quoteofthedayapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quoteofthedayapp.ui.BookmarksScreen
import com.example.quoteofthedayapp.ui.QuoteofTheDayScreen
import com.example.quoteofthedayapp.ui.theme.QuoteofTheDayAppTheme

class MainActivity : ComponentActivity() {
    private val viewModel: QuoteViewModel by viewModels()

    private fun shareQuote(text: String) {
        val intent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            putExtra(android.content.Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        startActivity(android.content.Intent.createChooser(intent, "Share quote via"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuoteofTheDayAppTheme {
                var selectedScreen by remember { mutableStateOf(0) }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = selectedScreen == 0,
                                onClick = { selectedScreen = 0 },
                                icon = { Icon(Icons.Default.FormatQuote, contentDescription = "Quote of the Day") },
                                label = { Text("Quote") }
                            )
                            NavigationBarItem(
                                selected = selectedScreen == 1,
                                onClick = { selectedScreen = 1 },
                                icon = { Icon(Icons.Default.Bookmark, contentDescription = "Bookmarks") },
                                label = { Text("Bookmarks") }
                            )
                        }
                    }
                ) { padding ->
                    Surface(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        when (selectedScreen) {
                            0 -> QuoteofTheDayScreen(viewModel)
                            1 -> BookmarksScreen(viewModel)
                            else -> QuoteofTheDayScreen(viewModel) // Fallback for safety
                        }
                    }
                }
            }
        }
    }
}
