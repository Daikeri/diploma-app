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
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class RecScreenViewModel(
    private val firestoreRepos: FirestoreRepository,
    private val itemRepos: ItemRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _generalUiState = MutableStateFlow<RecScreenUiState>(RecScreenUiState.getEmptyInstance())
    val generalUiState: StateFlow<RecScreenUiState> = _generalUiState

    private val _cardUiState = MutableLiveData<CardDetailUiState>()
    val cardUiState = _cardUiState

    private lateinit var collectionRecommendation: Job

    fun fetchRecommendation() {
        collectionRecommendation = viewModelScope.launch {
        firestoreRepos.recommendation
            .collect { rawHashes ->
                Log.e("Flow Content", "$rawHashes")

                /*
                val (categories, sourceIDs) = withContext(Dispatchers.Default) {
                    val titles = extractTitleFeeds(rawHashes)
                    val items = extractItem(rawHashes, titles)
                    titles to items
                }

                val items = toMovieHashes(sourceIDs)

                _generalUiState.value = RecScreenUiState(
                    items.size,
                    categories,
                    items
                )

                 */

                val titles = extractTitleFeeds(rawHashes)
                val cardStates = extractCardStates(rawHashes, titles)
                val uiItems = fetchExternalContent(cardStates)
                val newIndex = indexation(uiItems)

                Log.e("New format", "$newIndex")

                _generalUiState.value = RecScreenUiState(
                    numFeeds = newIndex.size,
                    feedsTitle = titles,
                    cardsContent = newIndex
                )
            }
        }
    }

    fun uploadUpdates(
        collectionName: String="shared_pool",
        key: MutableMap<String, Any?>
    ) {
        viewModelScope.launch {
            firestoreRepos.uploadUpdates(collectionName, key)
        }
    }

    fun onUserChooseItem(category: Int, itemKey: String) {
        _cardUiState.value = _generalUiState.value.cardsContent[category][itemKey]?.let {
            CardDetailUiState(
                item = it["item"] as Movie,
                marked = it["marked"] as Boolean,
                viewed = it["viewed"] as Boolean,
                rated = it["rated"] as Int?
            )
        }
    }

    private suspend fun extractTitleFeeds(hashes: List<MutableMap<String, Any>>): List<String> {
       return withContext(Dispatchers.Default) {
           val categories = linkedSetOf<String>()
           hashes.forEach { hash ->
               hash["category"]?.let { categories.add(it.toString()) }
           }
           categories.toList()
       }
    }

    private suspend fun extractCardStates(
        hashes: List<MutableMap<String, Any>>,
        categories: List<String>
    ):List<List<MutableMap<String, Any?>>> {
        return withContext(Dispatchers.Default) {
            val cutHashes = mutableListOf<List<MutableMap<String, Any?>>>()

            categories.forEach { category ->
                val categoryElements = mutableListOf<MutableMap<String, Any?>>()

                hashes.forEach { hash ->
                    hash["category"]?.let {
                        if (it == category)
                            categoryElements.add(
                                mutableMapOf(
                                    "doc_id" to hash["doc_id"] as String,
                                    "item" to hash["source_item_id"] as String,
                                    "marked" to hash["marked"] as Boolean,
                                    "viewed" to hash["viewed"] as Boolean,
                                    "rated" to hash["rated"] as Int?
                                )
                            )
                    }
                }
                cutHashes.add(categoryElements)
            }

            cutHashes
        }
    }

    private suspend fun fetchExternalContent(
        collection: List<List<MutableMap<String, Any?>>>
    ):List<List<MutableMap<String, Any?>>> {
        return withContext(Dispatchers.IO) {
            collection.forEach { category ->
                category.map { movie ->
                    movie.replace("item", itemRepos.getUserItem(movie["item"] as String))
                }
            }
            collection
        }
    }

    private suspend fun indexation(
        collection: List<List<MutableMap<String, Any?>>>
    ): List<Map<String, Map<String, Any?>>> {
        return withContext(Dispatchers.Default) {
            val result = collection.map { category ->
                val acc = mutableMapOf<String, Map<String, Any?>>()
                category.map { item ->
                    val key = item["doc_id"] as String
                    item.remove("doc_id")
                    acc[key] = item.toMap()
                }
                acc.toMap()
            }
            result
        }
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
                        categoryItem.add(hash["source_item_id"] as String) // убрать дебильный ключ
                }
            }
            sourceIDs.add(categoryItem)
        }

        return sourceIDs
    }


    private suspend fun toMovieHashes(sourceIDs: List<List<String>>): List<Map<String,Movie>> {
        return withContext(Dispatchers.IO) {
            val movies = sourceIDs.map { idS -> itemRepos.getUserItems(idS) }
            movies.map { category ->
                val sources = category.map { movie ->
                    movie.externalId
                }
                sources.zip(category).toMap()
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
    val cardsContent:  List<Map<String, Map<String, Any?>>>,
) {
    companion object {
        fun getEmptyInstance(): RecScreenUiState {
            return RecScreenUiState(
                numFeeds = 0,
                feedsTitle = emptyList(),
                cardsContent = emptyList(),
            )
        }
    }
}


data class CardDetailUiState(
    var item: Movie = Movie.emptyInstance(),
    val rated: Int?=null,
    val viewed: Boolean = false,
    val marked: Boolean = false,
) {
    companion object {
        fun getEmptyInstance(): CardDetailUiState {
            return CardDetailUiState()
        }
    }
}

