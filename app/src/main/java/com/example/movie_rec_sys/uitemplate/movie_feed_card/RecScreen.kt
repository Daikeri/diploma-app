package com.example.movie_rec_sys.uitemplate.movie_feed_card

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.viewmodel.RecScreenUiState
import com.example.movie_rec_sys.viewmodel.RecScreenViewModel

@Composable
fun RecScreen(
    viewModel: RecScreenViewModel = viewModel(factory = RecScreenViewModel.Factory),
    toDetail: (String) -> Unit = {}
) {
    val generalUiState by viewModel.generalUiState.observeAsState(RecScreenUiState.getEmptyInstance())
    var needRepeat by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!needRepeat) {
            viewModel.fetchRecommendation()
            needRepeat = true
        }
    }

    LazyColumn {
        items(generalUiState.numFeeds) {
            MovieFeed(generalUiState.cardsContent[it], generalUiState.feedsTitle[it], toDetail)
        }
    }
}