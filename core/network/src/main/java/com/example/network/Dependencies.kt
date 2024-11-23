package com.example.network

import android.content.Context
import android.util.Log
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
}
