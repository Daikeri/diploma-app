package com.example.network

import android.content.Context
import android.util.Log
import coil.ImageLoader
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    init {
        Log.e("NetworkModule created", "")
    }
    @Provides
    @Singleton
    fun provideCoilImageLoader(@ApplicationContext applicationContext: Context): ImageLoader {
        return ImageLoader
            .Builder(applicationContext)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson? {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideODMbAPI(@ApplicationContext applicationContext: Context, gson: Gson): ODMbApiService {
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
    ): Int
}

