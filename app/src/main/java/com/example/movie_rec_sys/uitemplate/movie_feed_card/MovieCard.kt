package com.example.movie_rec_sys.uitemplate.movie_feed_card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.data.Movie
import com.example.movie_rec_sys.viewmodel.RecScreenViewModel

@Composable // 40 symbol max
fun MovieCard(
    item: Movie,
    toDetail: (String) -> Unit = {},
    viewModel: RecScreenViewModel = viewModel(factory = RecScreenViewModel.Factory)
) {
    OutlinedCard(
        onClick = {
            toDetail(item.externalId)
        },
        modifier = Modifier.size(width = 120.dp, height = 240.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            Modifier.fillMaxSize(),
        ) {
            item.posterImage?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    Modifier.weight(3f)
                        .fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.title,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}