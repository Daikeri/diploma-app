package com.example.movie_rec_sys.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.movie_rec_sys.MyApplication
import com.example.movie_rec_sys.data.DBRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlin.coroutines.cancellation.CancellationException

class SearchViewModel(
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var prevRequest: Job? = null
    private val _uiState = MutableStateFlow<Map<String, SearchUiStateComponent>>(mapOf())
    val uiState = _uiState

    suspend fun findMovieByTitle(title: String) {
        prevRequest?.cancel()

        prevRequest = viewModelScope.launch {
            yield()
            dbRepository.findMovieByTitle(title).collect { searchStruct ->
                yield()
                _uiState.value = searchStruct.mapValues { entry ->
                    yield()
                    val state =
                        if (entry.value.second == null) {
                            SearchUiStateComponent(
                                title = entry.value.first.title,
                                genres = entry.value.first.genres,
                            )
                        } else {
                            SearchUiStateComponent(
                                title = entry.value.first.title,
                                genres = entry.value.first.genres,
                                score = entry.value.second!!.imdbRating,
                                poster = entry.value.second!!.downloadImage
                            )
                        }
                    state
                }
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                val savedStateHandle = extras.createSavedStateHandle()

                return SearchViewModel(
                    (application as MyApplication).appContainer.dbRepos,
                    savedStateHandle
                ) as T
            }
        }
    }
}

data class SearchUiStateComponent(
    val title: String,
    val genres: String,
    val score: String?=null,
    val poster: ImageBitmap?=null
)