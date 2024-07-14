package com.example.movie_rec_sys.uitemplate.movie_feed_card

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.movie_rec_sys.viewmodel.UIComponent

@Composable
fun MovieFeed(
    categoryName: String,
    cardContent: Map<String, UIComponent>,
    skeletonLoader: Boolean,
    toDetail: (Int, String) -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                0.1f at 0 using LinearEasing
                0.7f at 2000 using LinearEasing
                0.1f at 4000 using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        modifier = Modifier
            .padding(vertical = 15.dp)
    ) {
        Surface(
            elevation = 10.dp,
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            modifier = Modifier,
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surfaceDim)
                    .padding(vertical = 10.dp)

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (skeletonLoader) "" else categoryName,
                        modifier = Modifier.then(
                            if (skeletonLoader)
                                Modifier.background(Color.Red.copy(alpha = alpha))
                            else
                                Modifier
                        )
                    )
                    Text(text = "All")
                }

                UpdatedContent(
                    cardContent = cardContent,
                    categoryName = categoryName,
                    toDetail = toDetail
                )
            }
        }
    }
}

@Composable
fun UpdatedContent(cardContent: Map<String, UIComponent>, categoryName: String, toDetail: (Int, String) -> Unit) {
    Log.e("CONTENT FOR CARD", "")
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        items(cardContent.keys.toList()) {docID ->
            MovieCard(
                categoryName,
                docID,
                cardContent[docID]!!,
                toDetail
            )
        }
    }
}

/*
 LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                ) {
                    items(cardContent.keys.toList()) {docID ->
                        MovieCard(
                            categoryName,
                            docID,
                            cardContent[docID]!!,
                            toDetail
                        )
                    }
                }
 */