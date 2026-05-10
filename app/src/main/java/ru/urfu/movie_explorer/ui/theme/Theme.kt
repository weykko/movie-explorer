package ru.urfu.movie_explorer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MovieExplorerDarkColors = darkColorScheme(
    primary = VioletPrimary,
    onPrimary = BackgroundDark,
    primaryContainer = VioletPrimaryContainer,
    onPrimaryContainer = OnSurface,
    secondary = VioletSecondary,
    onSecondary = BackgroundDark,
    tertiary = VioletTertiary,
    onTertiary = BackgroundDark,
    background = BackgroundDark,
    onBackground = OnSurface,
    surface = SurfaceDark,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariant,
    outline = OutlineDark,
)

/**
 * Тема приложения на Material 3.
 * Принудительно использует тёмную цветовую схему, чтобы фиолетовые акценты
 * выглядели одинаково вне зависимости от системных настроек.
 */
@Composable
fun MovieExplorerTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = MovieExplorerDarkColors,
        typography = MovieExplorerTypography,
        content = content,
    )
}
