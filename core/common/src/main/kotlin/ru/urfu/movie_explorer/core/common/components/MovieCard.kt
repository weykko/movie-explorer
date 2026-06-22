package ru.urfu.movie_explorer.core.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import org.koin.compose.koinInject
import ru.urfu.movie_explorer.core.common.R
import ru.urfu.movie_explorer.core.domain.model.Movie
import ru.urfu.movie_explorer.core.domain.usecase.ObserveIsFavoriteUseCase

/**
 * Унифицированная карточка фильма для всех списковых экранов.
 */
@Composable
fun MovieCard(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PosterThumbnail(posterUrl = movie.posterUrl, isFavorite = isFavorite)
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
                val rating = movie.rating
                if (rating != null) {
                    Spacer(Modifier.height(8.dp))
                    RatingChip(rating = rating)
                }
            }
        }
    }
}

/**
 * Обёртка над [MovieCard], которая сама подписывается на статус «в избранном» для
 * конкретного [Movie.id]. Полезна на экранах, где элементы рендерятся пачкой и нет
 * смысла тянуть избранное через ViewModel-список.
 */
@Composable
fun FavoriteAwareMovieCard(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    observeIsFavorite: ObserveIsFavoriteUseCase = koinInject(),
) {
    val isFavorite by remember(movie.id) { observeIsFavorite(movie.id) }
        .collectAsStateWithLifecycle(initialValue = false)
    MovieCard(movie = movie, onClick = onClick, modifier = modifier, isFavorite = isFavorite)
}

@Composable
private fun PosterThumbnail(
    posterUrl: String?,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
) {
    val shape = RoundedCornerShape(14.dp)
    Box(modifier = modifier.size(width = 80.dp, height = 110.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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
        if (isFavorite) {
            FavoriteOverlay(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp),
            )
        }
    }
}

@Composable
private fun RatingChip(rating: Double, modifier: Modifier = Modifier) {
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
