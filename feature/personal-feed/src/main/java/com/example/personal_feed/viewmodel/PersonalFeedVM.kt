package com.example.personal_feed.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.FBAuthRepository
import com.example.firestore.FirestoreRepository
import com.example.firestore.RecommendationDoc
import com.example.movie.Movie
import com.example.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalFeedVM @Inject constructor(
    val authRepository: FBAuthRepository,
    val firestoreRepository: FirestoreRepository,
    val movieRepository: MovieRepository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = MutableStateFlow<PersonalFeedUiState?>(null)
    val uiState: StateFlow<PersonalFeedUiState?> = _uiState

    init {
        viewModelScope.launch {
            authRepository.userState.collect { user ->
                firestoreRepository.recommendation(user?.uid ?: "")
                    .collect { updates ->
                        val categoryContent: MutableMap<String, MutableList<MovieCard>> = mutableMapOf()

                        updates.forEach { doc ->
                            when(doc.actionFlag) {
                                "ADDED" -> {
                                    categoryContent
                                        .getOrPut(doc.category) { mutableListOf() }
                                        .add(
                                            MovieCard(
                                                item = null,
                                                downloadResID = doc.downloadResID,
                                                docID = doc.docID,
                                                relevanceIndex = doc.relevanceIndex
                                            )
                                        )
                                }
                            }
                        }

                        val sortedByRelevance = categoryContent.values
                            .toList()
                            .map { feed ->
                                feed.sortedBy { it.relevanceIndex }
                            }

                        _uiState.value = PersonalFeedUiState(
                            numCategory = categoryContent.keys.size,
                            categoryHeading = categoryContent.keys.toList(),
                            contentFeed = sortedByRelevance
                        )

                        _uiState.value = _uiState.value!!.copy(
                            contentFeed = sortedByRelevance.map { category ->
                                category.map {
                                    val networkResult = movieRepository.getMovie(it.downloadResID)
                                    it.copy(item = networkResult)
                                }
                            }
                        )

                        Log.e("${_uiState.value!!.contentFeed[0][0]}", "")
                    }
            }
        }
    }
}



data class PersonalFeedUiState(
    val numCategory: Int,
    val categoryHeading: List<String>,
    val contentFeed: List<List<MovieCard>>
)

data class MovieCard(
    val item: Movie?,
    val downloadResID: String,
    val docID: String,
    val relevanceIndex: Int
)