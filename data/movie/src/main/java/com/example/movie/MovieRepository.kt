package com.example.movie

import android.util.Log
import com.example.network.ImageLoader
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import androidx.compose.ui.graphics.ImageBitmap


class MovieRepository @Inject constructor(
    val movieRDS: ODMbApiService,
    val imageLoader: ImageLoader,
) {
    private val cashedMovie: MutableMap<String, Movie> = mutableMapOf()

    suspend fun getMovie(movieID: String): Movie? {
        return try {
            cashedMovie.getOrPut(movieID) { findOrDownloadMovie(movieID) }
        } catch (e: Exception) {
            Log.e("Exception from MovieRepos", "$e")
            null
        }
    }

    private suspend fun findOrDownloadMovie(movieID:String): Movie {
        val movie = movieRDS.getMovieDetails(
            apiKey = "f75f8380",
            movieId = movieID
        )

        val image = imageLoader.downloadImage(movie.poster)
        movie.downloadImage = image
        cashedMovie[movieID] = movie
        return movie
    }
}

data class Movie(
    @SerializedName("imdbID") var externalId: String,
    @SerializedName("Title") var title: String,
    @SerializedName("Year") var year: String,
    @SerializedName("Rated") var rated: String,
    @SerializedName("Released") var released: String,
    @SerializedName("Runtime") var runtime: String,
    @SerializedName("Genre") var genre: String,
    @SerializedName("Director") var director: String,
    @SerializedName("Actors") var actors: String,
    @SerializedName("Plot") var plot: String,
    @SerializedName("Poster") var poster: String,
    @SerializedName("Country") var country: String,
    @SerializedName("imdbRating") var imdbRating: String,
    var downloadImage: ImageBitmap? = null,
) {
    override fun toString(): String {
        return this.externalId
    }
}
