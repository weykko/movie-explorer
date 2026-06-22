package ru.urfu.movie_explorer.feature.movies.details

import ru.urfu.movie_explorer.core.domain.model.Movie

sealed interface MovieDetailsUiState {

    data object Loading : MovieDetailsUiState

    data class Content(val movie: Movie) : MovieDetailsUiState

    data class Error(val message: String) : MovieDetailsUiState
}
