package ru.urfu.movie_explorer.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.urfu.movie_explorer.domain.model.MovieError
import ru.urfu.movie_explorer.domain.usecase.SearchMoviesUseCase

/**
 * ViewModel экрана поиска.
 */
@OptIn(FlowPreview::class)
class SearchViewModel(
    private val searchMovies: SearchMoviesUseCase,
) : ViewModel() {

    private val queryFlow = MutableStateFlow("")

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var activeSearchJob: Job? = null

    init {
        queryFlow
            .debounce(SEARCH_DEBOUNCE_MILLIS)
            .distinctUntilChanged()
            .filter { it.trim().length >= MIN_QUERY_LENGTH }
            .onEach { performSearch(it) }
            .launchIn(viewModelScope)
    }

    fun onQueryChanged(newQuery: String) {
        queryFlow.value = newQuery
        _uiState.value = _uiState.value.copy(
            query = newQuery,
            results = if (newQuery.trim().length < MIN_QUERY_LENGTH) emptyList() else _uiState.value.results,
            error = null,
            hasSearched = if (newQuery.isBlank()) false else _uiState.value.hasSearched,
        )
    }

    fun retry() {
        val current = _uiState.value.query
        if (current.trim().length >= MIN_QUERY_LENGTH) performSearch(current)
    }

    private fun performSearch(query: String) {
        activeSearchJob?.cancel()
        _uiState.value = _uiState.value.copy(isLoading = true, error = null, hasSearched = true)
        activeSearchJob = viewModelScope.launch {
            try {
                val movies = searchMovies(query)
                _uiState.value = _uiState.value.copy(
                    results = movies,
                    isLoading = false,
                    error = null,
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: MovieError) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message,
                )
            }
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MILLIS = 350L
        const val MIN_QUERY_LENGTH = 2
    }
}
