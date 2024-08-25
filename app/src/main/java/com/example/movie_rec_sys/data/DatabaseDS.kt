package com.example.movie_rec_sys.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity(tableName = "movies")
data class DBMovie(
    @PrimaryKey val id:Int,
    val title: String,
    val genres: String
)

@Entity(tableName = "links")
data class DBLinks(
    @PrimaryKey val id: Int,
    val externalId: String
)

data class MovieFromDB(
    val title: String,
    val genres: String,
    val externalId: String,
)

@Dao
interface MoviesDao {
    @Query("""
        SELECT movies.id, movies.title, movies.genres, links.externalId
        FROM movies
        INNER JOIN links ON movies.id = links.id
        WHERE movies.title LIKE :query
    """)
    fun findMovieByTitle(query: String): List<MovieFromDB>
}

@Database(entities = [DBMovie::class, DBLinks::class], version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}

