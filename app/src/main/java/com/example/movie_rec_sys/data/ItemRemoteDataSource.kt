package com.example.movie_rec_sys.data

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.request.SuccessResult
import com.example.movie_rec_sys.MyApplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class ItemRemoteDataSource(
    private val hostSource: String,
    private val apiKey: String,
    private val localContext: MyApplication
) {
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(hostSource)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiImplement = retrofitBuilder.create(ODMbApiService::class.java)

    suspend fun fetchItem(id: String, otherApiKey: String=apiKey) : Movie {
        val item = apiImplement.getMovieDetails(otherApiKey, id)
        val downloadResult = downloadImage(item.poster)
        item.posterImage = downloadResult
        return item
    }

    private suspend fun downloadImage(url: String): ImageBitmap? {
        val imageLoader = ImageLoader.Builder(localContext)
            .crossfade(true)
            .build()

        // Создание ImageRequest
        val request = ImageRequest.Builder(localContext)
            .data(url)
            .build()

        // Выполнение запроса и загрузка изображения
        val result: ImageResult = imageLoader.execute(request)

        // Проверка успешного завершения загрузки
        if (result is SuccessResult) {
            val drawable = result.drawable
            // Преобразование Drawable в ImageBitmap
            return drawable.toBitmap().asImageBitmap()
        } else {
            // Обработка ошибки загрузки изображения
            return null
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