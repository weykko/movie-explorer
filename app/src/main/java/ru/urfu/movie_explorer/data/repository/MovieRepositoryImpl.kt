package ru.urfu.movie_explorer.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.urfu.movie_explorer.data.network.api.ImdbApi
import ru.urfu.movie_explorer.data.network.mapper.toDomain
import ru.urfu.movie_explorer.domain.model.Movie
import ru.urfu.movie_explorer.domain.model.MovieError
import ru.urfu.movie_explorer.domain.model.MovieFilters
import ru.urfu.movie_explorer.domain.repository.MovieRepository
import java.io.IOException

/**
 * Реализация репозитория поверх IMDb API.
 */
class MovieRepositoryImpl(
    private val api: ImdbApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MovieRepository {

    private val popularMoviesCache = MutableStateFlow<List<Movie>>(emptyList())

    override fun observePopularMovies(): Flow<List<Movie>> = popularMoviesCache.asStateFlow()

    override suspend fun refreshPopularMovies(filters: MovieFilters, limit: Int) {
        val movies = runNetwork {
            api.getPopularTitles(
                limit = limit,
                genres = filters.genre?.let { listOf(it) },
                startYear = filters.minYear,
                endYear = filters.maxYear,
                minAggregateRating = filters.minRating,
                minVoteCount = filters.minVoteCount,
                sortBy = filters.sortBy?.apiValue,
                sortOrder = filters.sortBy?.apiSortOrder,
            ).titles.map { it.toDomain() }
        }
        popularMoviesCache.value = movies
    }

    override suspend fun getMovieById(id: String): Movie {
        popularMoviesCache.value.firstOrNull { it.id == id }?.let { return it }

        return try {
            runNetwork { api.getTitle(titleId = id).toDomain() }
        } catch (e: MovieError.Server) {
            if (e.code == HTTP_NOT_FOUND) throw MovieError.NotFound(id) else throw e
        }
    }

    override suspend fun searchMovies(query: String, limit: Int): List<Movie> = runNetwork {
        api.searchTitles(query = query, limit = limit).titles.map { it.toDomain() }
    }

    private suspend fun <T> runNetwork(block: suspend () -> T): T = withContext(ioDispatcher) {
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            throw MovieError.Server(code = e.code(), cause = e)
        } catch (e: IOException) {
            throw MovieError.Network(cause = e)
        } catch (e: MovieError) {
            throw e
        } catch (e: Exception) {
            throw MovieError.Unknown(cause = e)
        }
    }

    private companion object {
        const val HTTP_NOT_FOUND = 404
    }
}
