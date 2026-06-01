package com.dimmer.app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimmer.app.data.api.Movie
import com.dimmer.app.data.api.MovieDetail
import com.dimmer.app.data.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val movie: MovieDetail) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}

class DetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState

    private val _similarMovies = MutableStateFlow<List<Movie>>(emptyList())
    val similarMovies: StateFlow<List<Movie>> = _similarMovies

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                val response = RetrofitClient.api.getMovieDetails(movieId)
                _uiState.value = DetailUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(e.message ?: "Unknown error")
            }
        }
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getSimilarMovies(movieId)
                _similarMovies.value = response.results
            } catch (e: Exception) {
                _similarMovies.value = emptyList()
            }
        }
    }
}