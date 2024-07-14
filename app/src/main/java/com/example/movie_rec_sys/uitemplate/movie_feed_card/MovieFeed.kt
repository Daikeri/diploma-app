package com.example.movie_rec_sys.uitemplate.movie_feed_card

import android.util.Log
import androidx.compose.animation.animateColor
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
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    val alpha by infiniteTransition.animateColor(
        initialValue = Color.White,
        targetValue = Color(0xFFFEFCFF),
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                Color.White at 0 using LinearEasing
                Color.LightGray at 2000 using LinearEasing
                Color.White at 4000 using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Surface(
            elevation = 10.dp,
            color = Color(0xFFDDD9DA),//.colorScheme.surfaceContainerLowest,
            modifier = Modifier,
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFEEE7F6)) //MaterialTheme.colorScheme.surfaceDim
                   // .padding(vertical = 10.dp)

            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp, end = 6.dp, bottom = 10.dp, top = 4.dp)

                ) {
                    Text(
                        text = if (skeletonLoader) "" else categoryName,
                        color = Color(0xFF605D63),
                        modifier = Modifier.then(
                            if (skeletonLoader)
                                Modifier.background(alpha)
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
    cardContent.values.forEach {
        Log.e("item=${it.item}|delay=${it.delay}", "")
    }
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

