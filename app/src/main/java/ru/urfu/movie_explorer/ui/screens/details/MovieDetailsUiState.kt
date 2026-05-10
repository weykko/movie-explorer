package ru.urfu.movie_explorer.ui.screens.details

import ru.urfu.movie_explorer.domain.model.Movie

sealed interface MovieDetailsUiState {

    data object Loading : MovieDetailsUiState

    data class Content(val movie: Movie) : MovieDetailsUiState

    data class Error(val message: String) : MovieDetailsUiState
}
