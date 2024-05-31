package com.example.movie_rec_sys.uitemplate.staticvers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun MainScreen(feedsCount: Int = 3, itemsInFeedCount: Int = 10 ){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.background(color = MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        items(feedsCount) {
            StaticMovieFeed(itemsInFeedCount)
        }
    }
}