package com.example.movie_rec_sys.uitemplate.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_rec_sys.R
import com.example.movie_rec_sys.viewmodel.SearchViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.viewmodel.SearchUiStateComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)) {
    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        val uiState by viewModel.uiState.collectAsState()
        val localScope = rememberCoroutineScope()

        MySearchBar(
            state = uiState,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            localScope.launch {
                viewModel.findMovieByTitle(it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    state: Map<String, SearchUiStateComponent>,
    modifier: Modifier = Modifier,
    findMovieByTitle: (String) -> Unit,
) {
    var inputQuery by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }

    SearchBar(
        query = inputQuery,
        onQueryChange = {
            inputQuery = it
            findMovieByTitle(inputQuery)
        },
        onSearch = { isActive = !isActive },
        active = isActive,
        onActiveChange = { isActive = !isActive },
        leadingIcon = { LeadingIcon(isActive) { isActive = !isActive }  },
        placeholder = { Text(text = "Давайте чета поищем...") },
        colors = SearchBarDefaults.colors(containerColor = Color(0xFFEDEDF4)),
        modifier = Modifier
            .then(modifier)
    ) {
        SearchContent(state)
    }
}
@Composable
fun LeadingIcon(isActive: Boolean, change: () -> Unit)  {
    AnimatedContent(
        targetState = isActive,
        label = "",
        transitionSpec = { fadeIn().togetherWith(fadeOut()) }
    ) {
        if (it) {
            IconButton(onClick = change) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        }
    }
}


@Composable
fun SearchContent(
    state: Map<String, SearchUiStateComponent>,
    modifier:Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        if (state.isNotEmpty()) {
            items(state.keys.toList()) {id ->
                FoundItem(
                    externalId = id,
                    headlineText = state[id]!!.title,
                    supportingText = state[id]!!.genres,
                    trailingText = state[id]!!.score,
                    image = state[id]!!.poster
                )
            }
        }
    }
}

@Composable
fun FoundItem(
    externalId: String,
    modifier: Modifier=Modifier,
    headlineText: String="Some Film Title",
    supportingText: String?="Some genres",
    trailingText: String?="Some Score",
    image: ImageBitmap?=null,
) {
    ListItem(
        headlineContent = { Text(text = headlineText)},
        supportingContent = {
            if (supportingText != null) {
                Text(text = supportingText)
            } else {
                Text(text = "", modifier = Modifier.background(Color.LightGray))
            }
        },
        trailingContent = {
            if (trailingText != null) {
                Text(text = trailingText)
            } else {
                Text(text = "", modifier = Modifier.background(Color.LightGray))
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color(0xFFE2E2E9)), // more - 0xFFE2E2E9, 0xFFE7E8EE
        leadingContent = {
            if (image != null)
                Image(
                    bitmap = image,
                    contentDescription = null,
                    modifier = Modifier
                        .height(64.dp)
                        .width(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            else
                Image(
                    painter = painterResource(id = R.drawable.test_image),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null,
                    modifier = Modifier
                        .height(64.dp)
                        .width(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
        },
        modifier = Modifier
            .then(modifier)
            .clip(RoundedCornerShape(12.dp))
    )
}
