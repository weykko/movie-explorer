package ru.urfu.movie_explorer.data.repository

import ru.urfu.movie_explorer.data.model.Movie

/**
 * Реализация репозитория на in-memory моках.
 * В следующих практиках будет заменена на реализацию поверх IMDb API.
 * Структура данных повторяет ответы imdbapi.dev.
 */
class MockMovieRepository : MovieRepository {

    override suspend fun getMovies(): List<Movie> = MOVIES

    override suspend fun getMovieById(id: String): Movie? = MOVIES.firstOrNull { it.id == id }

    private companion object {
        val MOVIES: List<Movie> = listOf(
            Movie(
                id = "tt0111161",
                title = "Побег из Шоушенка",
                originalTitle = "The Shawshank Redemption",
                year = 1994,
                runtimeMinutes = 142,
                genres = listOf("Драма"),
                rating = 9.3,
                votes = 2_900_000,
                plot = "Банкир Энди Дюфрейн осуждён на пожизненное заключение за убийство жены " +
                        "и её любовника. Попав в тюрьму Шоушенк, он находит дружбу и надежду " +
                        "там, где другие давно её потеряли.",
                director = "Фрэнк Дарабонт",
                cast = listOf("Тим Роббинс", "Морган Фриман", "Боб Гантон"),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BMDAyY2FhYjctNDc5OS00MDNlLThiMGUtY2UxYWVkNGY2ZjljXkEyXkFqcGc@._V1_.jpg",
            ),
            Movie(
                id = "tt0068646",
                title = "Крёстный отец",
                originalTitle = "The Godfather",
                year = 1972,
                runtimeMinutes = 175,
                genres = listOf("Криминал", "Драма"),
                rating = 9.2,
                votes = 2_000_000,
                plot = "Глава мафиозного клана Корлеоне передаёт власть своему сыну Майклу. " +
                        "Эпическая сага о семье, чести и цене власти.",
                director = "Фрэнсис Форд Коппола",
                cast = listOf("Марлон Брандо", "Аль Пачино", "Джеймс Каан"),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg",
            ),
            Movie(
                id = "tt0468569",
                title = "Тёмный рыцарь",
                originalTitle = "The Dark Knight",
                year = 2008,
                runtimeMinutes = 152,
                genres = listOf("Боевик", "Криминал", "Драма"),
                rating = 9.0,
                votes = 2_800_000,
                plot = "Бэтмен поднимает ставки в войне с преступностью Готэма. С помощью " +
                        "лейтенанта Гордона и прокурора Харви Дента он сталкивается с гениальным " +
                        "анархистом по прозвищу Джокер.",
                director = "Кристофер Нолан",
                cast = listOf("Кристиан Бейл", "Хит Леджер", "Аарон Экхарт"),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_.jpg",
            ),
            Movie(
                id = "tt0109830",
                title = "Форрест Гамп",
                originalTitle = "Forrest Gump",
                year = 1994,
                runtimeMinutes = 142,
                genres = listOf("Драма", "Мелодрама"),
                rating = 8.8,
                votes = 2_300_000,
                plot = "История жизни простого парня из Алабамы, ставшего невольным свидетелем " +
                        "и участником ключевых событий американской истории второй половины XX века.",
                director = "Роберт Земекис",
                cast = listOf("Том Хэнкс", "Робин Райт", "Гэри Синиз"),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BNDYwNzVjMTItZmU5YS00YjQ5LTljYjgtMjY2NDVmYWMyNWFmXkEyXkFqcGc@._V1_.jpg",
            ),
            Movie(
                id = "tt1375666",
                title = "Начало",
                originalTitle = "Inception",
                year = 2010,
                runtimeMinutes = 148,
                genres = listOf("Фантастика", "Боевик", "Триллер"),
                rating = 8.8,
                votes = 2_500_000,
                plot = "Дом Кобб — талантливый вор, специализирующийся на похищении секретов " +
                        "из подсознания во время сна. Его последнее задание — не украсть идею, " +
                        "а внедрить её.",
                director = "Кристофер Нолан",
                cast = listOf("Леонардо ДиКаприо", "Джозеф Гордон-Левитт", "Эллен Пейдж"),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_.jpg",
            ),
            Movie(
                id = "tt0816692",
                title = "Интерстеллар",
                originalTitle = "Interstellar",
                year = 2014,
                runtimeMinutes = 169,
                genres = listOf("Фантастика", "Драма", "Приключения"),
                rating = 8.7,
                votes = 2_100_000,
                plot = "В будущем, когда Земля становится непригодной для жизни, команда " +
                        "исследователей отправляется через червоточину в поисках нового дома " +
                        "для человечества.",
                director = "Кристофер Нолан",
                cast = listOf("Мэттью Макконахи", "Энн Хэтэуэй", "Джессика Честейн"),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BYzdjMDAxZGItMjI2My00ODA1LTlkNzItOWFjMDU5ZDJlYWY3XkEyXkFqcGc@._V1_.jpg",
            ),
            Movie(
                id = "tt0133093",
                title = "Матрица",
                originalTitle = "The Matrix",
                year = 1999,
                runtimeMinutes = 136,
                genres = listOf("Фантасттика", "Боевик"),
                rating = 8.7,
                votes = 2_000_000,
                plot = "Хакер Нео узнаёт от загадочного Морфеуса, что весь окружающий мир — " +
                        "это иллюзия, созданная машинами, и присоединяется к восстанию против них.",
                director = "Лана и Лилли Вачовски",
                cast = listOf("Киану Ривз", "Лоуренс Фишберн", "Кэрри-Энн Мосс"),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BN2NmN2VhMTQtMDNiOS00NDlhLTliMjgtODE2ZTY0ODBkZjFlXkEyXkFqcGc@._V1_.jpg",
            ),
            Movie(
                id = "tt0137523",
                title = "Бойцовский клуб",
                originalTitle = "Fight Club",
                year = 1999,
                runtimeMinutes = 139,
                genres = listOf("Драма"),
                rating = 8.8,
                votes = 2_300_000,
                plot = "Уставший от рутины офисный клерк знакомится с харизматичным продавцом " +
                        "мыла, и вместе они основывают подпольный бойцовский клуб.",
                director = "Дэвид Финчер",
                cast = listOf("Брэд Питт", "Эдвард Нортон", "Хелена Бонэм Картер"),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BOTgyOGQ1NDItNGU3Ny00MjU3LTg2YWEtNmEyYjBiMjI1Y2M5XkEyXkFqcGc@._V1_.jpg",
            ),
            Movie(
                id = "tt0110912",
                title = "Криминальное чтиво",
                originalTitle = "Pulp Fiction",
                year = 1994,
                runtimeMinutes = 154,
                genres = listOf("Криминал", "Драма"),
                rating = 8.9,
                votes = 2_200_000,
                plot = "Несколько переплетённых историй о мелких бандитах, боксёре и парочке " +
                        "грабителей закусочной в Лос-Анджелесе.",
                director = "Квентин Тарантино",
                cast = listOf("Джон Траволта", "Сэмюэл Л. Джексон", "Ума Турман"),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BYTViYTE3ZGQtNDBlMC00ZTAyLTkyODMtZGRiZDg0MjA2YThkXkEyXkFqcGc@._V1_.jpg",
            ),
            Movie(
                id = "tt0167260",
                title = "Властелин колец: Возвращение короля",
                originalTitle = "The Lord of the Rings: The Return of the King",
                year = 2003,
                runtimeMinutes = 201,
                genres = listOf("Фэнтези", "Приключения", "Драма"),
                rating = 9.0,
                votes = 1_900_000,
                plot = "Финальная глава эпопеи о Кольце Всевластия. Хоббит Фродо приближается " +
                        "к Роковой горе, а Арагорн ведёт войска людей в последнюю битву.",
                director = "Питер Джексон",
                cast = listOf("Элайджа Вуд", "Вигго Мортенсен", "Иэн Маккеллен"),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BMjE4MzAyMDc3MV5BMl5BanBnXkFtZTcwNTU0MTYzMw@@._V1_.jpg",
            ),
            Movie(
                id = "tt0114369",
                title = "Семь",
                originalTitle = "Se7en",
                year = 1995,
                runtimeMinutes = 127,
                genres = listOf("Криминал", "Триллер", "Драма"),
                rating = 8.6,
                votes = 1_700_000,
                plot = "Опытный детектив Сомерсет и его молодой напарник Миллс расследуют серию " +
                        "убийств, инсценированных по мотивам семи смертных грехов.",
                director = "Дэвид Финчер",
                cast = listOf("Брэд Пит��", "Морган Фриман", "Гвинет Пэлтроу"),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BOTUwODM5MTctZjczMi00OTk4LTg3NWUtNmVhMTAzNTNjYjcyXkEyXkFqcGc@._V1_.jpg",
            ),
            Movie(
                id = "tt0099685",
                title = "Славные парни",
                originalTitle = "Goodfellas",
                year = 1990,
                runtimeMinutes = 146,
                genres = listOf("Биография", "Криминал", "Драма"),
                rating = 8.7,
                votes = 1_200_000,
                plot = "История восхождения Генри Хилла в нью-йоркской мафии 1960–80-х годов — " +
                        "от мальчика на побегушках до полноправного «славного парня».",
                director = "Мартин Скорсезе",
                cast = listOf("Роберт Де Ниро", "Рэй Лиотта", "Джо Пеши"),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BY2NkZjEzMDgtN2RjYy00YzM1LWI4ZmQtMjIwYjFjNmI3ZGEwXkEyXkFqcGc@._V1_.jpg",
            ),
        )
    }
}
