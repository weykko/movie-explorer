package ru.urfu.movie_explorer.data.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Запись избранного фильма в локальной БД.
 *
 * Хранит подмножество полей [ru.urfu.movie_explorer.domain.model.Movie], достаточное
 * для отображения карточки в списке избранного и базовых деталей без обращения к сети.
 * Списочные поля (`genres`, `cast`) сериализуются в строки с разделителем — Room не
 * умеет хранить коллекции «из коробки», а тащить полноценный TypeConverter ради двух
 * полей избыточно.
 */
@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "original_title") val originalTitle: String?,
    @ColumnInfo(name = "year") val year: Int?,
    @ColumnInfo(name = "runtime_minutes") val runtimeMinutes: Int?,
    @ColumnInfo(name = "genres") val genresJoined: String,
    @ColumnInfo(name = "rating") val rating: Double?,
    @ColumnInfo(name = "votes") val votes: Int?,
    @ColumnInfo(name = "plot") val plot: String?,
    @ColumnInfo(name = "director") val director: String?,
    @ColumnInfo(name = "cast") val castJoined: String,
    @ColumnInfo(name = "poster_url") val posterUrl: String?,
    @ColumnInfo(name = "added_at") val addedAt: Long,
)
