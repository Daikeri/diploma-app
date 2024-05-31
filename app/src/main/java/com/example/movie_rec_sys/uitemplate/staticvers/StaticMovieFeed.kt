package com.example.movie_rec_sys.uitemplate.staticvers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StaticMovieFeed(itemsCount: Int, categoryName:String="Some Category") {
    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.secondaryContainer)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = categoryName)
            Text(text = "All")
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(itemsCount) {
                StaticMovieCard()
            }
        }
    }
}