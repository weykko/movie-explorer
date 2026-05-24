package ru.urfu.movie_explorer.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

/**
 * Типизированные назначения (destinations) навигации на Navigation Compose + kotlinx.serialization.
 * Каждый destination — serializable объект или класс, который можно передавать
 * напрямую в `NavHost` и в вызовы `navigate(...)`.
 */
sealed interface Destination {

    @Serializable
    data object Movies : Destination

    @Serializable
    data class MovieDetails(val movieId: String) : Destination

    @Serializable
    data object Search : Destination

    @Serializable
    data object Profile : Destination
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
    Profile(
        destination = Destination.Profile,
        labelResId = ru.urfu.movie_explorer.R.string.nav_profile,
        selectedIcon = Icons.Rounded.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
}
