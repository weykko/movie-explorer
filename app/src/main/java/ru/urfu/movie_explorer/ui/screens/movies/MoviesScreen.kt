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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import org.koin.androidx.compose.koinViewModel
import ru.urfu.movie_explorer.R
import ru.urfu.movie_explorer.domain.model.Movie

/**
 * Экран списка фильмов. Подписывается на [MoviesViewModel] и рисует одно
 * из трёх состояний, описанных в [MoviesUiState].
 */
@Composable
fun MoviesScreen(
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MoviesScreenContent(
        state = uiState,
        onMovieClick = onMovieClick,
        onRetry = viewModel::retry,
        modifier = modifier,
    )
}

@Composable
private fun MoviesScreenContent(
    state: MoviesUiState,
    onMovieClick: (Movie) -> Unit,
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
                movies = state.movies,
                isRefreshing = state.isRefreshing,
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

@Composable
private fun MoviesList(
    movies: List<Movie>,
    isRefreshing: Boolean,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
) {
    @Suppress("unused")
    val refreshing = isRefreshing
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { ScreenHeader() }

        items(items = movies, key = Movie::id) { movie ->
            MovieListItem(
                movie = movie,
                onClick = { onMovieClick(movie) },
            )
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun ScreenHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
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
}

@Composable
private fun MovieListItem(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PosterThumbnail(posterUrl = movie.posterUrl)
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = buildString {
                        if (movie.year != null) append(movie.year)
                        if (movie.genres.isNotEmpty()) {
                            if (isNotEmpty()) append(" • ")
                            append(movie.genres.joinToString())
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(8.dp))
                RatingChip(rating = movie.rating)
            }
        }
    }
}

@Composable
private fun PosterThumbnail(
    posterUrl: String?,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .size(width = 80.dp, height = 110.dp)
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        MaterialTheme.colorScheme.surfaceVariant,
                    ),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (posterUrl != null) {
            SubcomposeAsyncImage(
                model = posterUrl,
                contentDescription = stringResource(R.string.a11y_poster),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
            )
        }
    }
}

@Composable
private fun RatingChip(rating: Double?, modifier: Modifier = Modifier) {
    if (rating == null) return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(14.dp),
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = "%.1f".format(rating),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
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

