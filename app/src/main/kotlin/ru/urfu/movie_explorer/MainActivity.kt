package ru.urfu.movie_explorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.urfu.movie_explorer.core.common.theme.MovieExplorerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MovieExplorerTheme {
                MovieExplorerApp()
            }
        }
    }
}
