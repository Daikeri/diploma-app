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
}

/*
  @Expose(serialize = false, deserialize = false)
  val Language : String,
  @Expose(serialize = false, deserialize = false)
  val Country : String,
  @Expose(serialize = false, deserialize = false)
  val Awards : String,
  @Expose(serialize = false, deserialize = false)
  val Ratings : String,
  @Expose(serialize = false, deserialize = false)
  val Metascore : String,
  @Expose(serialize = false, deserialize = false)
  val imdbRating : String,
  @Expose(serialize = false, deserialize = false)
  val imdbVotes : String,
  @Expose(serialize = false, deserialize = false)
  val Type : String,
  @Expose(serialize = false, deserialize = false)
  val DVD : String,
  @Expose(serialize = false, deserialize = false)
  val BoxOffice : String,
  @Expose(serialize = false, deserialize = false)
  val Production : String,
  @Expose(serialize = false, deserialize = false)
  val Website : String,
  @Expose(serialize = false, deserialize = false)
  val Response : String
   */