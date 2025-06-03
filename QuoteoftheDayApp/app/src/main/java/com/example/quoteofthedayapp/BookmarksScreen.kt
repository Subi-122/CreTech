package com.example.quoteofthedayapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quoteofthedayapp.QuoteViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import com.example.quoteofthedayapp.Quote


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(viewModel: QuoteViewModel) {
    val bookmarks = viewModel.bookmarkedQuotes.collectAsState().value
    val context = LocalContext.current // Get context once here

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bookmarked Quotes") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(bookmarks) { quote ->
                QuoteCard(
                    quote = quote,
                    onBookmarkClick = { viewModel.toggleBookmarkFor(quote) },
                    onShareClick = { text ->
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, text)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share quote via"))
                    }
                )
            }
        }
    }
}
