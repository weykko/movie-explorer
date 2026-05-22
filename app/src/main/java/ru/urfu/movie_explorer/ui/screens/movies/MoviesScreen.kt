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
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.urfu.movie_explorer.R
import ru.urfu.movie_explorer.domain.model.Movie
import ru.urfu.movie_explorer.ui.common.EmptyState
import ru.urfu.movie_explorer.ui.common.MovieCard

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
        onRetry = viewModel::retry,
        modifier = modifier,
    )
}

@Composable
private fun MoviesScreenContent(
    state: MoviesUiState,
    onMovieClick: (Movie) -> Unit,
    onOpenFilters: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        when (state) {
            MoviesUiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary,
            )

            is MoviesUiState.Error -> ErrorContent(
                message = state.message,
                onRetry = onRetry,
                modifier = Modifier.align(Alignment.Center),
            )

            is MoviesUiState.Content -> MoviesList(
                state = state,
                onMovieClick = onMovieClick,
                onOpenFilters = onOpenFilters,
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

@Composable
private fun MoviesList(
    state: MoviesUiState.Content,
    onMovieClick: (Movie) -> Unit,
    onOpenFilters: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ScreenHeader(
                hasActiveFilters = state.hasActiveFilters,
                onOpenFilters = onOpenFilters,
            )
        }

        if (state.movies.isEmpty()) {
            item {
                EmptyState(
                    message = stringResource(R.string.filters_empty_result),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        } else {
            items(items = state.movies, key = Movie::id) { movie ->
                MovieCard(movie = movie, onClick = { onMovieClick(movie) })
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun ScreenHeader(
    hasActiveFilters: Boolean,
    onOpenFilters: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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
        FiltersIconWithBadge(
            hasActiveFilters = hasActiveFilters,
            onClick = onOpenFilters,
        )
    }
}

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
            // Бейдж — маленькая фиолетовая точка в углу иконки.
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 8.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onRetry) {
            Text(text = stringResource(R.string.action_retry))
        }
    }
}
