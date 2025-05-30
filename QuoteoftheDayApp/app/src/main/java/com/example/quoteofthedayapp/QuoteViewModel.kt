package com.example.quoteofthedayapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteofthedayapp.data.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class QuoteViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = QuoteDatabase.getInstance(application).quoteDao()
    private val repository = QuoteRepository(dao)

    private val _quote = MutableStateFlow<Quote?>(null)
    val quote: StateFlow<Quote?> = _quote

    private val _bookmarkedQuotes = MutableStateFlow<List<Quote>>(emptyList())
    val bookmarkedQuotes: StateFlow<List<Quote>> = _bookmarkedQuotes

    init {
        viewModelScope.launch {
            preloadQuotes()
            loadQuoteOfTheDay()
            loadBookmarkedQuotes()
        }
    }

    private suspend fun preloadQuotes() {
        val existingQuotes = repository.getAllQuotes()
        val defaultQuotes = listOf(
            Quote(text = "Believe in yourself!", author = "Anonymous"),
            Quote(text = "Stay hungry, stay foolish.", author = "Steve Jobs"),
            Quote(text = "Dream big. Work hard.", author = "Anonymous"),
            Quote(text = "Success is not final, failure is not fatal: It is the courage to continue that counts.", author = "Winston Churchill"),
            Quote(text = "Your time is limited, so don’t waste it living someone else’s life.", author = "Steve Jobs"),
            Quote(text = "Happiness depends upon ourselves.", author = "Aristotle"),
            Quote(text = "In the middle of every difficulty lies opportunity.", author = "Albert Einstein"),
            Quote(text = "Do what you can, with what you have, where you are.", author = "Theodore Roosevelt"),
            Quote(text = "The best way to predict the future is to invent it.", author = "Alan Kay"),
            Quote(text = "It always seems impossible until it's done.", author = "Nelson Mandela"),
            Quote(text = "Don't watch the clock; do what it does. Keep going.", author = "Sam Levenson"),
            Quote(text = "The harder you work for something, the greater you'll feel when you achieve it.", author = "Anonymous"),
            Quote(text = "Little by little, one travels far.", author = "J.R.R. Tolkien"),
            Quote(text = "The only limit to our realization of tomorrow is our doubts of today.", author = "Franklin D. Roosevelt"),
            Quote(text = "Start where you are. Use what you have. Do what you can.", author = "Arthur Ashe"),
            Quote(text = "Everything you’ve ever wanted is on the other side of fear.", author = "George Addair"),
            Quote(text = "Turn your wounds into wisdom.", author = "Oprah Winfrey"),
            Quote(text = "You are never too old to set another goal or to dream a new dream.", author = "C.S. Lewis"),
            Quote(text = "If opportunity doesn’t knock, build a door.", author = "Milton Berle"),
            Quote(text = "If you want to lift yourself up, lift up someone else.", author = "Booker T. Washington"),
            Quote(text = "Be the change that you wish to see in the world.", author = "Mahatma Gandhi"),
            Quote(text = "The best way out is always through.", author = "Robert Frost"),
            Quote(text = "What lies behind us and what lies before us are tiny matters compared to what lies within us.", author = "Ralph Waldo Emerson"),
            Quote(text = "The purpose of our lives is to be happy.", author = "Dalai Lama"),
            Quote(text = "I have not failed. I've just found 10,000 ways that won't work.", author = "Thomas Edison"),
            Quote(text = "The mind is everything. What you think you become.", author = "Buddha"),
            Quote(text = "Do not go where the path may lead, go instead where there is no path and leave a trail.", author = "Ralph Waldo Emerson"),
            Quote(text = "Whether you think you can or you think you can't, you're right.", author = "Henry Ford"),
            Quote(text = "Opportunities don't happen. You create them.", author = "Chris Grosser"),
            Quote(text = "I would rather die of passion than of boredom.", author = "Vincent van Gogh"),
            Quote(text = "Success is how high you bounce when you hit bottom.", author = "George S. Patton"),
            Quote(text = "There are no secrets to success. It is the result of preparation, hard work, and learning from failure.", author = "Colin Powell"),
            Quote(text = "Strive not to be a success, but rather to be of value.", author = "Albert Einstein"),
            Quote(text = "It does not matter how slowly you go as long as you do not stop.", author = "Confucius"),
            Quote(text = "Success usually comes to those who are too busy to be looking for it.", author = "Henry David Thoreau"),
            Quote(text = "The only way to do great work is to love what you do.", author = "Steve Jobs"),
            Quote(text = "The best revenge is massive success.", author = "Frank Sinatra"),
            Quote(text = "Don’t be afraid to give up the good to go for the great.", author = "John D. Rockefeller"),
            Quote(text = "Life is what happens when you're busy making other plans.", author = "John Lennon"),
            Quote(text = "Life is either a daring adventure or nothing at all.", author = "Helen Keller"),
            Quote(text = "The journey of a thousand miles begins with one step.", author = "Lao Tzu"),
            Quote(text = "Life is short, and it’s up to you to make it sweet.", author = "Sarah Louise Delany"),
            Quote(text = "Life itself is the most wonderful fairy tale.", author = "Hans Christian Andersen"),
            Quote(text = "Keep your face always toward the sunshine—and shadows will fall behind you.", author = "Walt Whitman"),
            Quote(text = "You only live once, but if you do it right, once is enough.", author = "Mae West"),
            Quote(text = "The biggest adventure you can take is to live the life of your dreams.", author = "Oprah Winfrey"),
            Quote(text = "To live is the rarest thing in the world. Most people exist, that is all.", author = "Oscar Wilde"),
            Quote(text = "Go confidently in the direction of your dreams. Live the life you have imagined.", author = "Henry David Thoreau"),
            Quote(text = "It is never too late to be what you might have been.", author = "George Eliot"),
            Quote(text = "The power of imagination makes us infinite.", author = "John Muir")
        )

        val newQuotes = defaultQuotes.filter { default ->
            existingQuotes.none { it.text == default.text && it.author == default.author }
        }

        if (newQuotes.isNotEmpty()) {
            newQuotes.forEach { repository.insertQuote(it) }
        }
    }

    private suspend fun loadQuoteOfTheDay() {
        val allQuotes = repository.getAllQuotes()
        if (allQuotes.isNotEmpty()) {
            val dayOfYear = LocalDate.now().dayOfYear
            val index = dayOfYear % allQuotes.size
            _quote.value = allQuotes[index]
        }
    }

    fun toggleBookmark() {
        viewModelScope.launch {
            _quote.value?.let {
                repository.toggleBookmark(it)
                _quote.value = it.copy(isBookmarked = !it.isBookmarked)
                loadBookmarkedQuotes()
            }
        }
    }

    fun toggleBookmarkFor(quote: Quote) {
        viewModelScope.launch {
            repository.toggleBookmark(quote)
            loadBookmarkedQuotes()
            if (_quote.value?.id == quote.id) {
                _quote.value = quote.copy(isBookmarked = !quote.isBookmarked)
            }
        }
    }

    fun loadRandomQuote() {
        viewModelScope.launch {
            val allQuotes = repository.getAllQuotes()
            if (allQuotes.isNotEmpty()) {
                val randomIndex = (allQuotes.indices).random()
                _quote.value = allQuotes[randomIndex]
            }
        }
    }

    private fun loadBookmarkedQuotes() {
        viewModelScope.launch {
            _bookmarkedQuotes.value = repository.getBookmarkedQuotes()
        }
    }
}
