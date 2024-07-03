package com.example.movie_rec_sys.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class PrimaryRecDataSource(
    private val hostSource: String,
) {
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(hostSource)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiImplement = retrofitBuilder.create(ItemBasedFiltration::class.java)

    suspend fun fetchIDs(): MutableMap<String, MutableMap<String, Any?>> {
        return apiImplement.getRec()
    }

    suspend fun sendFeedback(feedback: GenresData): Boolean {
        val networkResult = apiImplement.sendFeedback(feedback)
        return networkResult.isSuccessful
    }
}

interface ItemBasedFiltration {
    @GET("primary_rec")
    suspend fun getRec(): MutableMap<String, MutableMap<String, Any?>>

    @POST("feedback")
    suspend fun sendFeedback(@Body map: GenresData): Response<Any>
}




data class GenresData(
    val user_id: String,
    val genres: MutableMap<String, MutableMap<String, Any?>>
)
