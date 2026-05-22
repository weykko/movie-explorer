package ru.urfu.movie_explorer.ui.screens.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.SwapVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.urfu.movie_explorer.R
import ru.urfu.movie_explorer.domain.model.Movie
import ru.urfu.movie_explorer.domain.model.MovieSortOption
import ru.urfu.movie_explorer.ui.common.EmptyState
import ru.urfu.movie_explorer.ui.common.ErrorContent
import ru.urfu.movie_explorer.ui.common.FavoriteAwareMovieCard

/**
 * Экран списка фильмов. Подписывается на [MoviesViewModel] и рисует одно
 * из трёх состояний, описанных в [MoviesUiState].
 */
@Composable
fun MoviesScreen(
    onMovieClick: (Movie) -> Unit,
    onOpenFilters: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MoviesScreenContent(
        state = uiState,
        onMovieClick = onMovieClick,
        onOpenFilters = onOpenFilters,
        onSortSelected = viewModel::onSortSelected,
        onRetry = viewModel::retry,
        modifier = modifier,
    )
}

@Composable
private fun MoviesScreenContent(
    state: MoviesUiState,
    onMovieClick: (Movie) -> Unit,
    onOpenFilters: () -> Unit,
    onSortSelected: (MovieSortOption?) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        MoviesHeader(
            hasActiveFilters = state is MoviesUiState.Content && state.hasActiveFilters,
            sortBy = (state as? MoviesUiState.Content)?.sortBy,
            onOpenFilters = onOpenFilters,
            onSortSelected = onSortSelected,
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 20.dp,
                bottom = 12.dp,
            ),
        )
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state is MoviesUiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary,
                )

                state is MoviesUiState.Content && state.error != null && state.movies.isEmpty() ->
                    ErrorContent(
                        message = stringResource(state.error.messageRes()),
                        onRetry = onRetry,
                        modifier = Modifier.align(Alignment.Center),
                    )

                state is MoviesUiState.Content && state.movies.isEmpty() -> EmptyState(
                    message = stringResource(R.string.filters_empty_result),
                    modifier = Modifier.align(Alignment.Center),
                )

                state is MoviesUiState.Content -> MoviesList(
                    movies = state.movies,
                    onMovieClick = onMovieClick,
                )
            }
            if (state is MoviesUiState.Content && state.isRefreshing && state.movies.isNotEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun MoviesList(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = movies, key = Movie::id) { movie ->
            FavoriteAwareMovieCard(movie = movie, onClick = { onMovieClick(movie) })
        }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun MoviesErrorReason.messageRes(): Int = when (this) {
    MoviesErrorReason.ServerUnavailable -> R.string.error_server_unavailable
    MoviesErrorReason.RequestFailed -> R.string.error_request_failed
    MoviesErrorReason.Network -> R.string.error_network
}



@Composable
private fun MoviesHeader(
    hasActiveFilters: Boolean,
    sortBy: MovieSortOption?,
    onOpenFilters: () -> Unit,
    onSortSelected: (MovieSortOption?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.movies_title),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.movies_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        SortIconWithMenu(
            selected = sortBy,
            onSortSelected = onSortSelected,
        )
        FiltersIconWithBadge(
            hasActiveFilters = hasActiveFilters,
            onClick = onOpenFilters,
        )
    }
}

@Composable
private fun SortIconWithMenu(
    selected: MovieSortOption?,
    onSortSelected: (MovieSortOption?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Rounded.SwapVert,
                contentDescription = stringResource(R.string.sort_open),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        if (selected != null) {
            ActiveBadge(modifier = Modifier.align(Alignment.TopEnd))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.sort_none)) },
                onClick = {
                    expanded = false
                    onSortSelected(null)
                },
                trailingIcon = {
                    if (selected == null) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            )
            MovieSortOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.displayName()) },
                    onClick = {
                        expanded = false
                        onSortSelected(option)
                    },
                    trailingIcon = {
                        if (option == selected) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun MovieSortOption.displayName(): String = stringResource(
    id = when (this) {
        MovieSortOption.ReleaseDate -> R.string.sort_release_date
        MovieSortOption.UserRating -> R.string.sort_user_rating
        MovieSortOption.RatingCount -> R.string.sort_rating_count
    },
)

@Composable
private fun FiltersIconWithBadge(
    hasActiveFilters: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Rounded.FilterList,
                contentDescription = stringResource(R.string.filters_open),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        if (hasActiveFilters) {
            ActiveBadge(modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}


@Composable
private fun ActiveBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .offset(x = (-8).dp, y = 8.dp)
            .size(10.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
    )
}


