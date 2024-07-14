package com.example.movie_rec_sys.data
import com.google.gson.annotations.SerializedName
import androidx.compose.ui.graphics.ImageBitmap


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
    companion object {
        fun emptyInstance(): Movie {
            return Movie(
                externalId = "",
                title = "",
                year = "",
                rated = "",
                released = "",
                runtime = "",
                genre = "",
                director = "",
                actors = "",
                plot = "",
                poster = "",
                country = "",
                imdbRating = ""
            )
        }
    }

    override fun toString(): String {
        return this.externalId
    }
}
