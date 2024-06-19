package com.example.movie_rec_sys.uitemplate.movie_feed_card

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.viewmodel.RecScreenUiState
import com.example.movie_rec_sys.viewmodel.RecScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecScreen(
    viewModel: RecScreenViewModel = viewModel(factory = RecScreenViewModel.Factory),
    toDetail: (Int, String) -> Unit
) {
    val generalUiState by viewModel.generalUiState.collectAsState()
    Log.e("RECOMPOSE REC SCREEN", "YEAP")
    var needRepeat by rememberSaveable { mutableStateOf(false) }

    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            if (listState.firstVisibleItemScrollOffset == 0) {
                scope.launch {
                    isRefreshing = true
                    viewModel.applyUpdatesFromPool()
                    isRefreshing = false
                }
            }
        })

    LaunchedEffect(Unit) {
        if (!needRepeat) {
            viewModel.fetchRecommendation()
            needRepeat = true
        }
    }

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
    ) {
        MyLazyColumns(generalUiState, listState, toDetail)

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun MyLazyColumns(generalUiState:RecScreenUiState, listState: LazyListState, toDetail: (Int, String) -> Unit) {
    LazyColumn(state = listState) {
        Log.e("RECOMPOSE IN FEED", "ZALYPA")
        items(generalUiState.numFeeds) {
            Log.e("RECOMPOSE IN FEED", "ZALYPA")
            MovieFeed(generalUiState.cardsContent[it], it, generalUiState.feedsTitle[it], toDetail)
        }
    }
}