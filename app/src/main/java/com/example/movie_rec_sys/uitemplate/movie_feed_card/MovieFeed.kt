package com.example.movie_rec_sys.uitemplate.movie_feed_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.data.Movie

import com.example.movie_rec_sys.viewmodel.RecScreenViewModel

//@Preview
@Composable
fun MovieFeed(
    cardStates: Map<String, Map<String, Any?>>, // cardStates: Map<String, Movie>
    categoryIndex: Int,
    categoryName: String="Some Category",
    toDetail: (Int, String) -> Unit
) {
    Column {
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
            items(cardStates.keys.toList()) {docID ->
                MovieCard(categoryIndex, docID, cardStates[docID]!!, toDetail)
            }
        }
    }
}