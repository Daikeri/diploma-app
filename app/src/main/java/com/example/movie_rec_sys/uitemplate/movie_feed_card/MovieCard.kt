package com.example.movie_rec_sys.uitemplate.movie_feed_card

import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import java.nio.file.WatchEvent

@Composable // 40 symbol max
fun MovieCard(
    categoryName: String,
    docID: String,
    state: UIComponent,
    toDetail: (Int, String) -> Unit,
    viewModel: RecScreenViewModel = viewModel()
) {
    val isDownload = state.item!=null

    Card(
        onClick = {
            //viewModel.onUserChooseItem(categoryName, docID)
            //toDetail(categoryName, docID)
        },
        colors = CardDefaults.cardColors(
            containerColor =  MaterialTheme.colorScheme.primary.copy(alpha=0f)
        ),
        elevation = CardDefaults.cardElevation(
            //defaultElevation = 4.dp
        ),
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .size(width = 120.dp, height = 240.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (isDownload) {
                DownloadImage(
                    modifier = Modifier.weight(3f),
                    state = state
                )
            } else {
                ImageStub(
                    state,
                    modifier = Modifier
                        .weight(3f)
                )
            }
            Title(state = state, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ImageStub(state: UIComponent, modifier: Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    key(state) {
        val dynamicColor = infiniteTransition.animateColor(
            initialValue = Color(0xFFCAC5CB),
            targetValue = Color(0xFF413F44),
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = state.delay + 2500
                    Color(0xFFCAC5CB) at state.delay using FastOutSlowInEasing
                    Color(0xFF413F44) at state.delay + 1000 using FastOutSlowInEasing
                    Color(0xFFCAC5CB) at state.delay + 2500 using FastOutSlowInEasing
                },
                repeatMode = RepeatMode.Restart
            ), label = "")

        Box(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(dynamicColor.value)
            .then(modifier)
        )
    }
}

@Composable
fun DownloadImage(state:UIComponent, modifier: Modifier) {
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
fun Title(state: UIComponent, modifier: Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    key(state) {
        Log.e("State in Title","")
        val dynamicColor = infiniteTransition.animateColor(
            initialValue = Color(0xFFCAC5CB),
            targetValue = Color(0xFF413F44),
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = state.delay + 2500
                    Color(0xFFCAC5CB) at state.delay using FastOutSlowInEasing
                    Color(0xFF413F44) at state.delay + 1000 using FastOutSlowInEasing
                    Color(0xFFCAC5CB) at state.delay + 2500 using FastOutSlowInEasing
                },
                repeatMode = RepeatMode.Restart
            ), label = "")

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier)
        ) {
            Text(
                text = if (state.item != null) state.item!!.title else "",
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                maxLines = 2,
                color = Color(0xFF615C69),//MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .then(
                        if (state.item != null)
                            Modifier
                        else
                            Modifier.background(dynamicColor.value)
                    )
            )
        }
    }
}
