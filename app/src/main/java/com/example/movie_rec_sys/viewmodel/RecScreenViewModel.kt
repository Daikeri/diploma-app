package com.example.movie_rec_sys.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.movie_rec_sys.MyApplication
import com.example.movie_rec_sys.data.Movie
import com.example.movie_rec_sys.data.FirestoreRepository
import com.example.movie_rec_sys.data.ItemRepository
import kotlinx.coroutines.launch

class RecScreenViewModel(
    private val firestoreRepos: FirestoreRepository,
    private val itemRepos: ItemRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _generalUiState = MutableLiveData<RecScreenUiState>()
    val generalUiState = _generalUiState

    private val _cardUiState = MutableLiveData<CardDetailUiState>()
    val cardUiState = _cardUiState

    fun fetchRecommendation() {
        viewModelScope.launch {
            val networkResult = firestoreRepos.fetchRecommendation()
            _generalUiState.value = RecScreenUiState(
                networkResult.size,
                extractTitle(networkResult),
                extractItem(networkResult)
            )
        }
    }

    fun onUserChooseItem(id: String) {
        _generalUiState.value?.cardsContent?.forEach { list ->
            val result = list.find { card -> card.externalId == id }
            result?.let {
                _cardUiState.value = CardDetailUiState(
                    item = it
                )
            }
        }
    }

    private fun extractTitle(hash: List<MutableMap<String, Any>>): List<String> {
        return  hash.map { it["category"].toString() }
    }

    private suspend fun extractItem(hash: List<MutableMap<String, Any>>): List<List<Movie>> {
        val idsForCategory = hash.mapNotNull { it["items"] as? List<String> }
        return idsForCategory.map { itemRepos.getUserItems(it) }
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

                return RecScreenViewModel(
                    (application as MyApplication).appContainer.fireStoreRepos,
                    (application as MyApplication).appContainer.itemRepos,
                    savedStateHandle
                ) as T
            }
        }
    }
}

data class RecScreenUiState(
    val numFeeds: Int,
    val feedsTitle: List<String>,
    val cardsContent: List<List<Movie>>,
    var currentDetailCard: String?=null
) {
    companion object {
        fun getEmptyInstance(): RecScreenUiState {
            return RecScreenUiState(
                numFeeds = 0,
                feedsTitle = emptyList(),
                cardsContent = emptyList(),
                currentDetailCard = null
            )
        }
    }
}


data class CardDetailUiState(
    var item: Movie = Movie.emptyInstance(),
    val rated: Int? = null,
    val marked: Boolean = false,
    val viewed: Boolean = false,
) {
    companion object {
        fun getEmptyInstance(): CardDetailUiState {
            return CardDetailUiState()
        }
    }
}
