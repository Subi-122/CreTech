package com.example.quoteofthedayapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import com.example.quoteofthedayapp.Quote
import androidx.compose.material.icons.filled.Share

@Composable
fun QuoteCard(
    quote: Quote,
    onBookmarkClick: (Quote) -> Unit,
    onShareClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "\"${quote.text}\"",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "- ${quote.author}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row {
                    IconButton(onClick = { onShareClick("${quote.text} — ${quote.author}") }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Quote"
                        )
                    }
                    IconButton(onClick = { onBookmarkClick(quote) }) {
                        Icon(
                            imageVector = if (quote.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (quote.isBookmarked) "Remove Bookmark" else "Add Bookmark"
                        )
                    }
                }
            }
        }
    }
}