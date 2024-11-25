package com.example.personal_feed.viewmodel

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
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: FBAuthRepository,
    private val firestoreRepository: FirestoreRepository,
    private val movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    init {
        viewModelScope.launch {
            authRepository.userState.collect { user ->
                firestoreRepository.recommendation(user?.uid ?: "")
                    .transform { updates ->
                        emit(getEmptyUIState(updates))
                    }
                    .transform { recommendationsStruct ->
                        recommendationsStruct.keys.forEach { category ->
                            recommendationsStruct[category]!!.forEach { item ->
                                val targetItem = movieRepository.getMovie(item.value.first.itemId)
                                recommendationsStruct[category]?.set(item.key, Pair(item.value.first, targetItem))
                                delay(500)
                                emit(recommendationsStruct)
                            }
                        }
                    }
            }
        }
    }

    private fun getEmptyUIState(
        updates: List<Triple<String, String, RecommendationDoc>>
    ): MutableMap<String, MutableMap<String, Pair<RecommendationDoc, Movie?>>> {
        val viewState: MutableMap<String,
                MutableMap<String, Pair<RecommendationDoc, Movie?>>> = mutableMapOf()
        updates.forEach { (actionFlag, docId, docContent) ->
            when(actionFlag) {
                "ADDED" -> {
                    val category = viewState.getOrPut(docContent.category) { mutableMapOf() }
                    category[docId] = Pair(docContent, null)
                }
            }
        }
        return viewState
    }
}