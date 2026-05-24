package ru.urfu.movie_explorer.core.common.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import ru.urfu.movie_explorer.core.common.R

/**
 * Типизированные назначения навигации (Navigation Compose + kotlinx.serialization).
 *
 * Описаны в core:common, чтобы app мог собрать nav-graph, а feature-модули могли
 * ссылаться на destinations соседних фич без cross-feature зависимостей.
 */
sealed interface Destination {

    @Serializable
    data object Movies : Destination

    @Serializable
    data class MovieDetails(val movieId: String) : Destination

    @Serializable
    data object Search : Destination

    @Serializable
    data object Favorites : Destination

    @Serializable
    data object Filters : Destination

    @Serializable
    data object Profile : Destination

    @Serializable
    data object EditProfile : Destination
}

/**
 * Вкладки нижней навигации. Лейблы берутся из общих ресурсов core:common.
 */
enum class TopLevelTab(
    val destination: Destination,
    val labelResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    Movies(
        destination = Destination.Movies,
        labelResId = R.string.nav_movies,
        selectedIcon = Icons.Rounded.Movie,
        unselectedIcon = Icons.Outlined.Movie,
    ),
    Search(
        destination = Destination.Search,
        labelResId = R.string.nav_search,
        selectedIcon = Icons.Rounded.Search,
        unselectedIcon = Icons.Outlined.Search,
    ),
    Favorites(
        destination = Destination.Favorites,
        labelResId = R.string.nav_favorites,
        selectedIcon = Icons.Rounded.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
    ),
    Profile(
        destination = Destination.Profile,
        labelResId = R.string.nav_profile,
        selectedIcon = Icons.Rounded.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
}
