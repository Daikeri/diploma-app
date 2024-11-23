package com.example.movie

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object MovieModule {
    @Provides
    @Singleton
    fun provideGson(): Gson? {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideImageRDS(@ApplicationContext applicationContext: Context, gson: Gson): ODMbApiService {
        val hostSource = "http://www.omdbapi.com"
        val retrofitBuilder = Retrofit
            .Builder()
            .baseUrl(hostSource)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofitBuilder.create(ODMbApiService::class.java)
    }
}


interface ODMbApiService {
    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") movieId: String
    ): Movie
}

