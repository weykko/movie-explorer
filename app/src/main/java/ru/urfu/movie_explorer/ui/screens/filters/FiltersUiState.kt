package ru.urfu.movie_explorer.ui.screens.filters

/**
 * UI-состояние экрана настроек фильтрации.
 *
 * Здесь хранится «черновик» — то, что выбрал пользователь, но ещё не нажал «Готово».
 * При нажатии «Готово» значения сохраняются в DataStore и применяются к списку.
 */
data class FiltersUiState(
    val genre: String? = null,
    val minRating: Double? = null,
    val minYear: Int? = null,
    val isSaved: Boolean = false,
) {

    val hasAnyFilter: Boolean
        get() = genre != null || minRating != null || minYear != null
}
