package ru.urfu.movie_explorer.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Star
import androidx.compose.foundation.border
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.urfu.movie_explorer.R
import ru.urfu.movie_explorer.domain.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movieId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailsViewModel = koinViewModel { parametersOf(movieId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()
            TopAppBar(
                title = { Text(text = stringResource(R.string.details_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.details_back),
                        )
                    }
                },
                actions = {
                    val isContentLoaded = uiState is MovieDetailsUiState.Content
                    IconButton(
                        onClick = viewModel::onFavoriteToggleClicked,
                        enabled = isContentLoaded,
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Rounded.Favorite
                            else Icons.Outlined.FavoriteBorder,
                            contentDescription = stringResource(
                                if (isFavorite) R.string.favorite_remove
                                else R.string.favorite_add
                            ),
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (val state = uiState) {
                MovieDetailsUiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary,
                )

                is MovieDetailsUiState.Error -> Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(Modifier.height(8.dp))
                    androidx.compose.material3.TextButton(onClick = viewModel::retry) {
                        Text(text = stringResource(R.string.action_retry))
                    }
                }

                is MovieDetailsUiState.Content -> MovieDetailsContent(state.movie)
            }
        }
    }
}

@Composable
private fun MovieDetailsContent(
    movie: Movie,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { MovieDetailsHeader(movie = movie) }
        item {
            if (movie.genres.isNotEmpty()) {
                GenreRow(
                    genres = movie.genres,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (!movie.plot.isNullOrBlank()) {
                    Section(title = stringResource(R.string.details_overview)) {
                        Text(
                            text = movie.plot,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                if (!movie.director.isNullOrBlank()) {
                    Section(title = stringResource(R.string.details_director)) {
                        Text(
                            text = movie.director,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                if (movie.cast.isNotEmpty()) {
                    Section(title = stringResource(R.string.details_cast)) {
                        Text(
                            text = movie.cast.joinToString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieDetailsHeader(
    movie: Movie,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        val (poster, title, originalTitle, ratingChip, metaRow) = createRefs()

        PosterImage(
            posterUrl = movie.posterUrl,
            modifier = Modifier
                .constrainAs(poster) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .width(140.dp)
                // Пропорции стандартного кинопостера (2:3) — изображение не обрезается.
                .aspectRatio(2f / 3f),
        )

        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            maxLines = 3,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(poster.top)
                start.linkTo(poster.end, margin = 16.dp)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
        )

        if (!movie.originalTitle.isNullOrBlank() && movie.originalTitle != movie.title) {
            Text(
                text = movie.originalTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                modifier = Modifier.constrainAs(originalTitle) {
                    top.linkTo(title.bottom, margin = 4.dp)
                    start.linkTo(title.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            )
        } else {
            Box(modifier = Modifier.constrainAs(originalTitle) {
                top.linkTo(title.bottom)
                start.linkTo(title.start)
            })
        }

        val ratingChipModifier = Modifier.constrainAs(ratingChip) {
            top.linkTo(originalTitle.bottom, margin = 12.dp)
            start.linkTo(title.start)
        }
        if (movie.rating != null) {
            RatingPill(
                rating = movie.rating,
                votes = movie.votes,
                modifier = ratingChipModifier,
            )
        } else {
            Box(modifier = ratingChipModifier)
        }

        MetaRow(
            year = movie.year,
            runtimeMinutes = movie.runtimeMinutes,
            modifier = Modifier.constrainAs(metaRow) {
                top.linkTo(ratingChip.bottom, margin = 12.dp)
                start.linkTo(title.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
        )
    }
}

@Composable
private fun PosterImage(
    posterUrl: String?,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(20.dp)
    val placeholderBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.surfaceVariant,
        ),
    )
    Box(
        modifier = modifier
            .clip(shape)
            .background(placeholderBrush),
        contentAlignment = Alignment.Center,
    ) {
        if (posterUrl != null) {
            SubcomposeAsyncImage(
                model = posterUrl,
                contentDescription = stringResource(R.string.a11y_poster),
                contentScale = ContentScale.Fit,
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
                        modifier = Modifier.size(28.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun RatingPill(
    rating: Double,
    votes: Int?,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
            .padding(horizontal = 14.dp, vertical = 6.dp),
    ) {
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp),
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "%.1f".format(rating),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        if (votes != null) {
            Spacer(Modifier.width(8.dp))
            Text(
                text = "(${formatVotes(votes)})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun MetaRow(
    year: Int?,
    runtimeMinutes: Int?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (year != null) {
            MetaLine(
                label = stringResource(R.string.details_year),
                value = year.toString(),
            )
        }
        if (runtimeMinutes != null) {
            MetaLine(
                label = stringResource(R.string.details_runtime),
                value = formatRuntime(runtimeMinutes),
            )
        }
    }
}

@Composable
private fun MetaLine(label: String, value: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(110.dp),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun GenreRow(
    genres: List<String>,
    modifier: Modifier = Modifier,
) {
    FlowLayout(modifier = modifier.fillMaxWidth(), spacing = 8.dp) {
        genres.forEach { GenreChip(text = it) }
    }
}

@Composable
private fun GenreChip(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun FlowLayout(
    modifier: Modifier = Modifier,
    spacing: androidx.compose.ui.unit.Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val gap = spacing.roundToPx()
        val maxWidth = constraints.maxWidth
        val placeables = measurables.map { it.measure(Constraints(maxWidth = maxWidth)) }

        var x = 0
        var y = 0
        var rowHeight = 0
        val positions = placeables.map { placeable ->
            if (x > 0 && x + placeable.width > maxWidth) {
                x = 0
                y += rowHeight + gap
                rowHeight = 0
            }
            val pos = x to y
            x += placeable.width + gap
            rowHeight = maxOf(rowHeight, placeable.height)
            pos
        }

        layout(width = maxWidth, height = (y + rowHeight).coerceAtLeast(constraints.minHeight)) {
            placeables.forEachIndexed { i, p -> p.placeRelative(positions[i].first, positions[i].second) }
        }
    }
}

@Composable
private fun Section(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(6.dp))
        content()
    }
}

private fun formatRuntime(minutes: Int): String {
    val h = minutes / MINUTES_IN_HOUR
    val m = minutes % MINUTES_IN_HOUR
    return when {
        h == 0 -> "$m min"
        m == 0 -> "$h h"
        else -> "$h h $m min"
    }
}

private const val MINUTES_IN_HOUR = 60

private fun formatVotes(votes: Int): String = when {
    votes >= 1_000_000 -> "%.1fM".format(votes / 1_000_000.0)
    votes >= 1_000 -> "%.1fK".format(votes / 1_000.0)
    else -> votes.toString()
}
