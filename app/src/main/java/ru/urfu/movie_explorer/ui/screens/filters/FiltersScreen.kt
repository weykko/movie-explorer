package ru.urfu.movie_explorer.ui.screens.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.urfu.movie_explorer.R

private val AVAILABLE_GENRES = listOf(
    "Action", "Adventure", "Animation", "Biography", "Comedy", "Crime",
    "Drama", "Family", "Fantasy", "History", "Horror", "Music", "Mystery",
    "Romance", "Sci-Fi", "Thriller", "War",
)

private val MIN_VOTES_STEPS = listOf(0, 100, 1_000, 10_000, 100_000, 250_000, 500_000, 1_000_000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FiltersViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Закрываем экран после успешного применения настроек.
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBackClick()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.filters_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.details_back),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            GenreSection(
                selected = state.genre,
                onGenreSelected = viewModel::onGenreChanged,
            )
            RatingSection(
                rating = state.minRating,
                onRatingChanged = viewModel::onMinRatingChanged,
            )
            MinVotesSection(
                votes = state.minVoteCount,
                onVotesChanged = viewModel::onMinVoteCountChanged,
            )
            YearRangeSection(
                from = state.minYear,
                to = state.maxYear,
                onFromChanged = viewModel::onMinYearChanged,
                onToChanged = viewModel::onMaxYearChanged,
            )
            Spacer(Modifier.fillMaxWidth())
            ActionsRow(
                hasAnyFilter = state.hasAnyFilter,
                onApply = viewModel::apply,
                onReset = viewModel::reset,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenreSection(
    selected: String?,
    onGenreSelected: (String?) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.filters_genre),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(
                    selected = selected == null,
                    onClick = { onGenreSelected(null) },
                    label = { Text(stringResource(R.string.filters_any)) },
                    shape = RoundedCornerShape(50),
                )
            }
            items(AVAILABLE_GENRES) { genre ->
                FilterChip(
                    selected = selected.equals(genre, ignoreCase = true),
                    onClick = { onGenreSelected(genre) },
                    label = { Text(genre) },
                    shape = RoundedCornerShape(50),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                )
            }
        }
    }
}

@Composable
private fun RatingSection(
    rating: Double?,
    onRatingChanged: (Double?) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeader(
            title = stringResource(R.string.filters_min_rating),
            value = rating?.let { "%.1f".format(it) } ?: stringResource(R.string.filters_any),
        )
        Slider(
            value = rating?.toFloat() ?: 0f,
            onValueChange = { value ->
                onRatingChanged(if (value <= 0f) null else value.toDouble())
            },
            valueRange = 0f..10f,
            steps = 19, // шаг 0.5
        )
    }
}

@Composable
private fun MinVotesSection(
    votes: Int?,
    onVotesChanged: (Int?) -> Unit,
) {
    val currentIndex = votes
        ?.let { v -> MIN_VOTES_STEPS.indexOfFirst { it == v }.takeIf { it >= 0 } }
        ?: 0
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeader(
            title = stringResource(R.string.filters_min_votes),
            value = if (votes == null) stringResource(R.string.filters_any) else formatCompactCount(votes),
        )
        Slider(
            value = currentIndex.toFloat(),
            onValueChange = { value ->
                val idx = value.toInt().coerceIn(0, MIN_VOTES_STEPS.lastIndex)
                onVotesChanged(MIN_VOTES_STEPS[idx].takeIf { it > 0 })
            },
            valueRange = 0f..MIN_VOTES_STEPS.lastIndex.toFloat(),
            steps = MIN_VOTES_STEPS.size - 2, // у Slider'а `steps` — это «промежуточные» точки между концами.
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearRangeSection(
    from: Int?,
    to: Int?,
    onFromChanged: (Int?) -> Unit,
    onToChanged: (Int?) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.filters_year_range),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            YearField(
                value = from,
                placeholderRes = R.string.filters_year_from,
                onValueChange = onFromChanged,
                modifier = Modifier.weight(1f),
            )
            YearField(
                value = to,
                placeholderRes = R.string.filters_year_to,
                onValueChange = onToChanged,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearField(
    value: Int?,
    placeholderRes: Int,
    onValueChange: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = { raw ->
            onValueChange(raw.toIntOrNull())
        },
        placeholder = { Text(stringResource(placeholderRes)) },
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

@Composable
private fun SectionHeader(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun ActionsRow(
    hasAnyFilter: Boolean,
    onApply: () -> Unit,
    onReset: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedButton(
            onClick = onReset,
            modifier = Modifier.weight(1f),
            enabled = hasAnyFilter,
        ) {
            Text(stringResource(R.string.filters_reset))
        }
        Button(
            onClick = onApply,
            modifier = Modifier.weight(1f),
        ) {
            Text(stringResource(R.string.filters_apply))
        }
    }
}

/** Компактное представление количества оценок: 1k / 10k / 1M. */
private fun formatCompactCount(value: Int): String = when {
    value >= 1_000_000 -> "${value / 1_000_000}M+"
    value >= 1_000 -> "${value / 1_000}k+"
    else -> "$value+"
}
