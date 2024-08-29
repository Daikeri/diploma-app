package com.example.movie_rec_sys.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity(tableName = "movies")
data class DBMovie(
    @PrimaryKey val id: Int,
    val title: String,
    val genres: String
)

@Entity(tableName = "links")
data class DBLinks(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val id: Int,
    @ColumnInfo(name = "external_id") val externalId: String
)

data class MovieFromDB(
    val id: Int,
    val title: String,
    val genres: String,
    @ColumnInfo(name = "external_id") val externalId: String
)

@Dao
interface MoviesDao {
    @Query("""
    SELECT movies.id, movies.title, movies.genres, links.external_id
    FROM movies
    INNER JOIN links ON movies.id = links.movie_id
    WHERE :query IS NOT NULL AND :query <> '' 
      AND LOWER(movies.title) LIKE LOWER(:query) || '%'
""")

    fun findMovieByTitle(query: String): List<MovieFromDB>
}

@Database(entities = [DBMovie::class, DBLinks::class], version = 3)
abstract class AppDB : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}
