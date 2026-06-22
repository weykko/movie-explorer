package ru.urfu.movie_explorer.core.domain.model

/**
 * Доменное представление ошибки.
 */
sealed class MovieError(message: String, cause: Throwable? = null) : Exception(message, cause) {

    class Network(cause: Throwable? = null) : MovieError(
        message = "No connection to the server. Please check your internet.",
        cause = cause,
    )

    class Server(val code: Int? = null, cause: Throwable? = null) : MovieError(
        message = if (code != null) "Server returned an error ($code)" else "Server returned a malformed response",
        cause = cause,
    )

    class NotFound(id: String) : MovieError(message = "Movie $id not found")

    class Unknown(cause: Throwable? = null) : MovieError(
        message = cause?.message ?: "Unexpected error",
        cause = cause,
    )
}
