package com.example.quoteofthedayapp.ui

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.quoteofthedayapp.QuoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteofTheDayScreen(viewModel: QuoteViewModel) {
    val quote = viewModel.quote.collectAsState().value
    val context = LocalContext.current  // get context once here, inside composable

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quote of the Day") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (quote != null) {
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.loadRandomQuote() },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Refresh")
            }

        }
    }
}
