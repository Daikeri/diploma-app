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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private var firstExtraction = false

    private lateinit var collectionRecommendation: Job

    private var updatesPool = mutableListOf<MutableMap<String, Any?>>()

    fun fetchRecommendation() {
        collectionRecommendation = viewModelScope.launch {
        firestoreRepos.recommendation
            .collect { rawHashes ->
                Log.e("Flow Content", "$rawHashes")

                if (!firstExtraction) {
                    initializeUiState(rawHashes)
                    firstExtraction = true
                } else {
                    rawHashes.forEach { newDoc ->
                        when(newDoc["action_flag"].toString()) {
                            "ADDED", "REMOVED" -> updatesPool.add(newDoc)
                            "MODIFIED" -> updateUiState(newDoc)
                        }
                    }
                }
            }
        }
    }
    private suspend fun updateUiState(rawHash: MutableMap<String, Any?>) {
        Log.e("Update UI State", "I'm HERE")
        val newState = withContext(Dispatchers.Default) {
            val categorySet = _generalUiState.value.feedsTitle
            val targetItemIndex = categorySet.indexOfFirst { it == rawHash["category"].toString() }
            val updatedItem = _generalUiState.value.cardsContent[targetItemIndex][rawHash["doc_id"].toString()]
            listOf("marked", "viewed", "rated").forEach {
                updatedItem?.set(it, rawHash[it])
            }
            RecScreenUiState(
                numFeeds = _generalUiState.value.numFeeds,
                feedsTitle = _generalUiState.value.feedsTitle,
                cardsContent = _generalUiState.value.cardsContent
            )
        }
        _generalUiState.value = newState
    }

    private suspend fun removeFromUiState(rawHash: MutableMap<String, Any?>) {
        val newState = withContext(Dispatchers.Default) {
            val categorySet = _generalUiState.value.feedsTitle
            val targetItemIndex = categorySet.indexOfFirst { it == rawHash["category"].toString() }
            _generalUiState.value.cardsContent[targetItemIndex].remove(rawHash["doc_id"].toString())
            val indexNonEmpty = _generalUiState.value.cardsContent
                .mapIndexedNotNull  { index, linkedHashMap ->
                    if (linkedHashMap.isNotEmpty()) index else null
            }

            val newState = indexNonEmpty.map {
                (_generalUiState.value.feedsTitle[it] to _generalUiState.value.cardsContent[it])
            }

            RecScreenUiState(
                numFeeds = newState.size,
                feedsTitle = newState.map { it.first },
                cardsContent =  newState.map { it.second }
            )
        }
        _generalUiState.value = newState
    }

    suspend fun appendTheUiState(rawHash: MutableMap<String, Any?>) {
        val categorySet = _generalUiState.value.feedsTitle
        val targetCategoryName = categorySet.find { it == rawHash["category"].toString() }
        val (newFeeds, newCardsContent) = if (targetCategoryName == null) {
            val newCategory = rawHash["category"].toString()
            val updatedTitle = _generalUiState.value.feedsTitle + listOf(newCategory)
            val docID = rawHash["doc_id"].toString()
            rawHash.remove("doc_id")
            val updatedCardsContent = _generalUiState.value.cardsContent + listOf(linkedMapOf(docID to rawHash))
            (updatedTitle to updatedCardsContent)
        } else {
            val targetCategoryIndex = categorySet.indexOfFirst { it == rawHash["category"].toString() }
            val docID = rawHash["doc_id"].toString()
            rawHash.remove("doc_id")
            _generalUiState.value.cardsContent[targetCategoryIndex][docID] = rawHash
            (_generalUiState.value.feedsTitle to _generalUiState.value.cardsContent)
        }

        RecScreenUiState(
            numFeeds = newFeeds.size,
            feedsTitle = newFeeds,
            cardsContent = newCardsContent
        )
    }


    private suspend fun initializeUiState(rawHashes: List<MutableMap<String, Any?>>) {
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

    private suspend fun extractTitleFeeds(hashes: List<MutableMap<String, Any?>>): List<String> {
       return withContext(Dispatchers.Default) {
           val categories = linkedSetOf<String>()
           hashes.forEach { hash ->
               hash["category"]?.let { categories.add(it.toString()) }
           }
           categories.toList()
       }
    }

    private suspend fun extractCardStates(
        hashes: List<MutableMap<String, Any?>>,
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
    ): List<LinkedHashMap<String, MutableMap<String, Any?>>> {
        return withContext(Dispatchers.Default) {
            val result = collection.map { category ->
                val acc = LinkedHashMap<String, MutableMap<String, Any?>>()
                category.map { item ->
                    val key = item["doc_id"] as String
                    item.remove("doc_id")
                    acc[key] = item.toMutableMap()
                }
                acc
            }
            result
        }
    }

    fun uploadUpdates(
        collectionName: String="shared_pool",
        update: MutableMap<String, Any?>
    ) {
        viewModelScope.launch {
            firestoreRepos.uploadUpdates(collectionName, update)
        }
    }

    fun addTheUpdatesPool(
        collectionName: String="shared_pool",
        key: MutableMap<String, Any?>
    ) {

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
    val cardsContent:  List<LinkedHashMap<String, MutableMap<String, Any?>>>,
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

