package ru.urfu.movie_explorer.ui.navigation

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

/**
 * Типизированные назначения (destinations) навигации на Navigation Compose + kotlinx.serialization.
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
 * Вкладки нижней навигации.
 */
enum class TopLevelTab(
    val destination: Destination,
    val labelResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    Movies(
        destination = Destination.Movies,
        labelResId = ru.urfu.movie_explorer.R.string.nav_movies,
        selectedIcon = Icons.Rounded.Movie,
        unselectedIcon = Icons.Outlined.Movie,
    ),
    Search(
        destination = Destination.Search,
        labelResId = ru.urfu.movie_explorer.R.string.nav_search,
        selectedIcon = Icons.Rounded.Search,
        unselectedIcon = Icons.Outlined.Search,
    ),
    Favorites(
        destination = Destination.Favorites,
        labelResId = ru.urfu.movie_explorer.R.string.nav_favorites,
        selectedIcon = Icons.Rounded.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
    ),
    Profile(
        destination = Destination.Profile,
        labelResId = ru.urfu.movie_explorer.R.string.nav_profile,
        selectedIcon = Icons.Rounded.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
}
