package ru.urfu.movie_explorer.feature.movies.list

/** Причина ошибки при загрузке фильмов. Маппится на локализованное сообщение в UI. */
enum class MoviesErrorReason {
    /** 5xx — сервер недоступен или сломан. */
    ServerUnavailable,

    /** 4xx — клиентская ошибка запроса (например, неверный фильтр). */
    RequestFailed,

    /** Сетевая ошибка / неизвестный сбой. */
    Network,
}
