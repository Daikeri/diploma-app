package com.example.movie_rec_sys.data

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.request.SuccessResult
import com.example.movie_rec_sys.MyApplication
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class ItemRemoteDataSource(
    private val hostSource: String,
    private val apiKey: String,
    private val localContext: MyApplication
) {
    private val gson = GsonBuilder().setLenient().create()

    private val sharedItems: MutableMap<String, Movie> = mutableMapOf()

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(hostSource)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val apiImplement = retrofitBuilder.create(ODMbApiService::class.java)

     suspend fun downloadItem(id: String, otherApiKey: String=apiKey): Movie {
        val item = apiImplement.getMovieDetails(otherApiKey, id)
        val downloadResult = downloadImage(item.poster)
        item.downloadImage = downloadResult
        sharedItems[id] = item
        return item
    }

    suspend fun getItem(id: String): Movie {
        return sharedItems.getOrPut(id) { downloadItem(id) }
    }

    private suspend fun downloadImage(url: String): ImageBitmap? {
        val imageLoader = ImageLoader.Builder(localContext)
            .crossfade(true)
            .build()

        val request = ImageRequest.Builder(localContext)
            .data(url)
            .build()

        val result: ImageResult = imageLoader.execute(request)

        return if (result is SuccessResult) {
            val drawable = result.drawable
            drawable.toBitmap().asImageBitmap()

        } else {
            null
        }
    }
}

interface ODMbApiService {
    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") movieId: String
    ): Movie
}