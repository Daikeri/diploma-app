package com.example.movie_rec_sys.uitemplate.movie_feed_card

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.viewmodel.MainScreenState
import com.example.movie_rec_sys.viewmodel.RecScreenViewModel
import com.example.movie_rec_sys.viewmodel.UserCollectionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecScreen(
    viewModel: RecScreenViewModel = viewModel(factory = RecScreenViewModel.Factory),
    toDetail: (Int, String) -> Unit,
    testViewModel: UserCollectionViewModel = viewModel(factory = UserCollectionViewModel.Factory),
) {
    val generalUiState by viewModel.generalUiState.collectAsState()

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
                    //viewModel.applyUpdatesFromPool()
                    isRefreshing = false
                }
            }
        })

    /*
    LaunchedEffect(Unit) {
        if (!needRepeat) {
            viewModel.fetchRec()
            needRepeat = true
        }
    }
     */

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        generalUiState?.let { MyLazyColumns(it, listState, toDetail) }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun MyLazyColumns(generalUiState: MainScreenState, listState: LazyListState, toDetail: (Int, String) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        state = listState,
        modifier = Modifier,
    ) {
        items(generalUiState.content.keys.toList()) {
            MovieFeed(
                categoryName = it ,
                cardContent = generalUiState.content[it]!!,
                skeletonLoader = generalUiState.skeletonTitle,
                toDetail = toDetail
            )
        }
    }
}