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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecScreenViewModel(
    private val firestoreRepos: FirestoreRepository,
    private val itemRepos: ItemRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _generalUiState = MutableStateFlow<RecScreenUiState>(RecScreenUiState.getEmptyInstance())
    val generalUiState: StateFlow<RecScreenUiState> = _generalUiState

    private val _cardUiState = MutableLiveData<CardDetailUiState>()
    val cardUiState = _cardUiState

    fun fetchRecommendation() {
        viewModelScope.launch {
        firestoreRepos.fetchRecommendation()
            .collect {
                val (categories, sourceIDs) = withContext(Dispatchers.Default) {
                    Log.e("Content", "$it")
                    val titles = extractTitle(it)
                    val items = extractItem(it, titles)
                    titles to items
                }

                val items = withContext(Dispatchers.IO) {
                    val movies = sourceIDs.map { idS -> itemRepos.getUserItems(idS) }
                    movies.map { category ->
                        val sources = category.map { movie ->
                            movie.externalId
                        }
                        val result = sources.zip(category).toMap()
                        Log.e("Yeap", "${result.javaClass}")
                        result
                    }
                }

                _generalUiState.value = RecScreenUiState(
                    items.size,
                    categories,
                    items
                )
            }
        }
    }

    fun uploadUpdates(
        collectionName: String="recommendation",
        key: Map<String, Int>
    ) {
        viewModelScope.launch {
            firestoreRepos.uploadUpdates(collectionName, key)
        }
    }

    fun onUserChooseItem(category: Int, itemKey: String) {
        _cardUiState.value = _generalUiState.value.cardsContent[category][itemKey]?.let {
            CardDetailUiState(
                item = it
            )
        }
    }

    private suspend fun extractTitle(hashes: List<MutableMap<String, Any>>): List<String> {
        val categories = mutableSetOf<String>()
        hashes.forEach { hash ->
            hash["category"]?.let { categories.add(it.toString()) }
        }
        return categories.toList()
    }

    private suspend fun extractItem(
        hashes: List<MutableMap<String, Any>>,
        categories: List<String>
    ): List<List<String>> {
        val sourceIDs = mutableListOf<List<String>>()
        categories.forEach { category ->
            val categoryItem = mutableListOf<String>()
            hashes.forEach { hash ->
                hash["category"]?.let {
                    if (it == category)
                        categoryItem.add(hash["source_item_id"] as String)
                }
            }
            sourceIDs.add(categoryItem)
        }

        return sourceIDs
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
    val cardsContent: List<Map<String,Movie>>,
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
    val marked: Pair<Boolean, Int?> = Pair(false, null),
    val viewed: Boolean = false,
    val rated: Boolean = false,
) {
    companion object {
        fun getEmptyInstance(): CardDetailUiState {
            return CardDetailUiState()
        }
    }
}

