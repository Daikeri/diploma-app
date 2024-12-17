package com.example.network

import android.content.Context
import android.util.Log
import javax.inject.Inject
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.request.SuccessResult
import dagger.hilt.android.qualifiers.ApplicationContext
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ErrorResult

class ImageLoader @Inject constructor(
    private val coilImageLoader: ImageLoader,
    @ApplicationContext private val applicationContext: Context
) {
    suspend fun downloadImage(url: String): ImageBitmap? {
        val request = ImageRequest.Builder(applicationContext)
            .memoryCachePolicy(CachePolicy.DISABLED)
            .diskCachePolicy(CachePolicy.DISABLED)
            .data(url)
            .build()

        val result: ImageResult = coilImageLoader
            .execute(request)

        return if (result is SuccessResult) {
            val drawable = result.drawable
            drawable
                .toBitmap()
                .asImageBitmap()
        } else {
            if (result is ErrorResult)
                Log.e("Coil exception", "${result.throwable}")
            null
        }
    }
}