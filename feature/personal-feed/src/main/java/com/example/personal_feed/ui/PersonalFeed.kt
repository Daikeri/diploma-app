package com.example.personal_feed.ui

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movie.Movie
import com.example.personal_feed.R
import com.example.personal_feed.viewmodel.MovieCard
import com.example.personal_feed.viewmodel.PersonalFeedUiState
import com.example.personal_feed.viewmodel.PersonalFeedVM
import kotlinx.coroutines.delay


@Composable
fun PersonalFeed(
    viewModel: PersonalFeedVM = hiltViewModel(),
    onMovieCardClick: (String, String) -> Unit = { _, _ ->}
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .background(Color(0xFF141414))
    ) {
        if (uiState == null)
            CircularStub()
        else
            CategoryContainer(
                uiState = uiState!!,
                onMovieCardClick = onMovieCardClick
            )
    }
}

@Composable
fun CircularStub() {

}

@Composable
fun CategoryContainer(
    uiState: PersonalFeedUiState,
    onMovieCardClick: (String, String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(uiState.numCategory) {
            Category(
                header = uiState.categoryHeading[it],
                content = uiState.contentFeed[it],
                onMovieCardClick = onMovieCardClick
            )
        }
    }
}

@Composable
fun Category(
    header: String,
    content: List<MovieCard>,
    onMovieCardClick: (String, String) -> Unit
) {
    val numItems = content.size

    Surface(
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF292929),//.colorScheme.surfaceContainerLowest,
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(color = Color(0xFF292929)) //MaterialTheme.colorScheme.surfaceDim
            // .padding(vertical = 10.dp)

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, end = 6.dp, bottom = 10.dp, top = 4.dp)

            ) {
                Text(
                    text = header,
                    color = Color(0xFFC4C4C4),
                    modifier = Modifier,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.inter_18pt_bold))
                )
                //Text(text = "All", color = Color(0xFF615B71))
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                items(numItems) {
                    MovieCard(
                        state = content[it],
                        downloadIndex = it,
                        numItems = numItems,
                        onMovieCardClick = onMovieCardClick
                    )
                }
            }
        }
    }
}


@Composable
fun MovieCard(
    state: MovieCard,
    downloadIndex: Int,
    numItems: Int,
    viewModel: PersonalFeedVM = hiltViewModel(),
    onMovieCardClick: (String, String) -> Unit = { _, _ ->}
) {
    Card(
        onClick = { onMovieCardClick(state.downloadResID, state.docID) },
        colors = CardDefaults.cardColors(
            containerColor =  MaterialTheme.colorScheme.primary.copy(alpha=0f)
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
            if (state.item != null) {
                DownloadImage(
                    modifier = Modifier.weight(3f),
                    state = state.item
                )
            } else {
                ImageStub(
                    downloadIndex,
                    numItems,
                    modifier = Modifier
                        .weight(3f)
                )
            }
            Title(
                state = state,
                downloadIndex = downloadIndex,
                numItems = numItems,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DownloadImage(
    state: Movie,
    modifier: Modifier
) {
    Image(
        bitmap = state.downloadImage!!,
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .then(modifier)
    )
}

@Composable
fun ImageStub(
    downloadIndex: Int,
    numItems: Int,
    modifier: Modifier
) {
    val dynamicColor = remember { Animatable(Color(0xFFCAC5CB)) }

    LaunchedEffect(key1 = downloadIndex) {
        delay((downloadIndex * 250).toLong())
        while (true) {
            dynamicColor.animateTo(
                targetValue = Color(0xFF413F44),
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            )
            dynamicColor.animateTo(
                targetValue = Color(0xFFCAC5CB),
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            )
            delay((numItems + 250).toLong())
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(12.dp))
        .background(dynamicColor.value)
        .then(modifier)
    )
}

@Composable
fun Title(
    state: MovieCard,
    downloadIndex: Int,
    numItems: Int,
    modifier: Modifier
) {
    val dynamicColor = remember { Animatable(Color(0xFFCAC5CB)) }

    LaunchedEffect(key1 = downloadIndex) {
        delay((downloadIndex * 250).toLong())
        while (true) {
            dynamicColor.animateTo(
                targetValue = Color(0xFF413F44),
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            )
            dynamicColor.animateTo(
                targetValue = Color(0xFFCAC5CB),
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            )
            delay((numItems + 250).toLong())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Text(
            text = if (state.item != null) state.item.title else "",
            fontFamily = FontFamily(Font(R.font.inter_18pt_regular)),
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            maxLines = 2,
            color = Color(0xFFC4C4C4),//MaterialTheme.colorScheme.onPrimary,
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