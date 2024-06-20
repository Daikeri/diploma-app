package com.example.movie_rec_sys.uitemplate.movie_feed_card

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.data.Movie

import com.example.movie_rec_sys.viewmodel.RecScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@Preview
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieFeed(
    cardStates: Map<String, Map<String, Any?>>, // cardStates: Map<String, Movie>
    categoryIndex: Int,
    categoryName: String="Some Category",
    toDetail: (Int, String) -> Unit,
    viewModel: RecScreenViewModel = viewModel()
) {

    Box(
        modifier = Modifier
            .padding(vertical = 15.dp)
    ) {
        Surface(
            elevation = 10.dp,
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier,
            //.padding(bottom = 15.dp)

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
                    Text(text = categoryName)
                    Text(text = "All")
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                ) {
                    items(cardStates.keys.toList()) {docID ->
                        MovieCard(categoryIndex, docID, cardStates[docID]!!, toDetail)
                    }
                }
            }
        }
    }

}