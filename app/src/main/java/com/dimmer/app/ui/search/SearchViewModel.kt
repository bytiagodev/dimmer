package com.dimmer.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimmer.app.data.api.Movie
import com.dimmer.app.data.api.RetrofitClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val movies: List<Movie>) : SearchUiState()
    object Empty : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isBlank()) {
            _uiState.value = SearchUiState.Idle
            return
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            search(newQuery)
        }
    }

    private suspend fun search(query: String) {
        _uiState.value = SearchUiState.Loading
        try {
            val response = RetrofitClient.api.searchMovies(query)
            _uiState.value = if (response.results.isEmpty()) {
                SearchUiState.Empty
            } else {
                SearchUiState.Success(response.results)
            }
        } catch (e: Exception) {
            _uiState.value = SearchUiState.Error(e.message ?: "Unknown error")
        }
    }
}