package com.example.movie

import com.example.network.ImageLoader
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import androidx.compose.ui.graphics.ImageBitmap


class MovieRepository @Inject constructor(
    private val movieRDS: ODMbApiService,
    private val imageLoader: ImageLoader,
    val apiKey: String = ""
) {
    private val cashedMovie: MutableMap<String, Movie> = mutableMapOf()

    suspend fun getMovie(movieID: String): Movie {
        return cashedMovie.getOrPut(movieID) { findOrDownloadMovie(movieID) }
    }

    private suspend fun findOrDownloadMovie(movieID:String): Movie {
        val movie = movieRDS.getMovieDetails(
            apiKey = apiKey,
            movieId = movieID
        )

        val image = imageLoader.downloadImage(movieID)
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
