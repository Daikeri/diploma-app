package com.example.movie_rec_sys.uitemplate.movie_feed_card

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.data.Movie
import com.example.movie_rec_sys.viewmodel.RecScreenViewModel
import com.example.movie_rec_sys.viewmodel.UIComponent

@Composable // 40 symbol max
fun MovieCard(
    categoryName: String,
    docID: String,
    state: UIComponent,
    toDetail: (Int, String) -> Unit,
    viewModel: RecScreenViewModel = viewModel()
) {
    val isDownload = state.item!=null
    MovieCard(isDownload, state)
}

@Composable
fun ImageStub(modifier: Modifier) {
   Box(modifier = Modifier
       .fillMaxSize()
       .clip(RoundedCornerShape(12.dp))
       .then(modifier)
   )
}

@Composable
fun DownloadImage(modifier: Modifier, state:UIComponent) {
    Image(
        bitmap = state.item!!.downloadImage!!,
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .then(modifier)
    )
}

@Composable
fun MovieCard(isDownload: Boolean, state: UIComponent) {

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                0.3f at 0 using LinearEasing
                0.7f at 2000 using LinearEasing
                0.3f at 4000 using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    OutlinedCard(
        onClick = {
            //viewModel.onUserChooseItem(categoryName, docID)
            //toDetail(categoryName, docID)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .size(width = 120.dp, height = 240.dp)
    ) {
        Column(
            Modifier.fillMaxSize(),
        ) {
            if (isDownload) {
                Log.e("IN DONE", "")
                DownloadImage(
                    modifier = Modifier.weight(3f),
                    state = state
                )
            } else {
                ImageStub(
                    modifier = Modifier
                        .weight(3f)
                        .background(Color.LightGray.copy(alpha = alpha))
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isDownload) state.item!!.title else "",
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (isDownload)
                                Modifier
                            else
                                Modifier.background(Color.LightGray.copy(alpha = alpha))
                        )
                )
            }
        }
    }
}