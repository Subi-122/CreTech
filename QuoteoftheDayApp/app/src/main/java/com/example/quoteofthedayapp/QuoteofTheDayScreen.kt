package com.example.quoteofthedayapp.ui

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.quoteofthedayapp.QuoteViewModel

@Composable
fun QuoteOfTheDayScreen(viewModel: QuoteViewModel) {
    val quote = viewModel.quote.collectAsState().value
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        quote?.let {
            QuoteCard(
                quote = it,
                onBookmarkClick = { viewModel.toggleBookmark() },
                onShareClick = { text ->
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, text)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share quote via"))
                }
            )
        } ?: Text("No quote available.")
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { viewModel.loadRandomQuote() }) {
            Text("Refresh")
        }
    }
}
