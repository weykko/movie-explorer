package ru.urfu.movie_explorer.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.urfu.movie_explorer.domain.usecase.ObserveFavoriteMoviesUseCase

/**
 * ViewModel экрана «Избранное». Подписан на Room через UseCase, поэтому работает
 * без интернета и автоматически обновляется при изменении содержимого БД.
 */
class FavoritesViewModel(
    observeFavorites: ObserveFavoriteMoviesUseCase,
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = observeFavorites()
        .map { FavoritesUiState(isLoading = false, favorites = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = FavoritesUiState(isLoading = true),
        )

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
