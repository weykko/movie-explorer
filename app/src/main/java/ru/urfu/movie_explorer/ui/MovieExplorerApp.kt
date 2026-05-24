package ru.urfu.movie_explorer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.urfu.movie_explorer.ui.navigation.Destination
import ru.urfu.movie_explorer.ui.navigation.TopLevelTab
import ru.urfu.movie_explorer.ui.screens.details.MovieDetailsScreen
import ru.urfu.movie_explorer.ui.screens.movies.MoviesScreen
import ru.urfu.movie_explorer.ui.screens.placeholder.PlaceholderScreen

/**
 * Корневой composable: содержит нижнюю навигацию и граф экранов.
 */
@Composable
fun MovieExplorerApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar(currentDestination)) {
                MovieExplorerBottomBar(
                    currentDestination = currentDestination,
                    onTabSelected = { tab -> navController.navigateToTab(tab) },
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Destination.Movies,
            ) {
                composable<Destination.Movies> {
                    MoviesScreen(
                        onMovieClick = { movie ->
                            navController.navigate(Destination.MovieDetails(movie.id))
                        },
                    )
                }

                composable<Destination.MovieDetails> { entry ->
                    val args: Destination.MovieDetails = entry.toRoute()
                    MovieDetailsScreen(
                        movieId = args.movieId,
                        onBackClick = { navController.popBackStack() },
                    )
                }

                composable<Destination.Search> {
                    PlaceholderScreen(
                        title = stringResource(TopLevelTab.Search.labelResId),
                        message = stringResource(ru.urfu.movie_explorer.R.string.search_placeholder),
                    )
                }

                composable<Destination.Profile> {
                    PlaceholderScreen(
                        title = stringResource(TopLevelTab.Profile.labelResId),
                        message = stringResource(ru.urfu.movie_explorer.R.string.profile_placeholder),
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieExplorerBottomBar(
    currentDestination: NavDestination?,
    onTabSelected: (TopLevelTab) -> Unit,
) {
    NavigationBar {
        TopLevelTab.entries.forEach { tab ->
            val selected = currentDestination?.matches(tab) == true
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = null,
                    )
                },
                label = { Text(text = stringResource(tab.labelResId)) },
                colors = NavigationBarItemDefaults.colors(),
                alwaysShowLabel = true,
            )
        }
    }
}

/**
 * Проверяет, является ли текущий destination одной из вкладок нижней навигации.
 * Для любого "внутреннего" экрана панель скрывается.
 */
private fun shouldShowBottomBar(destination: NavDestination?): Boolean {
    if (destination == null) return true
    return TopLevelTab.entries.any { destination.matches(it) }
}

private fun NavDestination.matches(tab: TopLevelTab): Boolean = when (tab) {
    TopLevelTab.Movies -> hasRoute<Destination.Movies>()
    TopLevelTab.Search -> hasRoute<Destination.Search>()
    TopLevelTab.Profile -> hasRoute<Destination.Profile>()
}

private fun NavHostController.navigateToTab(tab: TopLevelTab) {
    navigate(tab.destination) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
