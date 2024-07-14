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
import com.example.movie_rec_sys.data.FirestoreDoc
import com.example.movie_rec_sys.data.Movie
import com.example.movie_rec_sys.data.FirestoreRepository
import com.example.movie_rec_sys.data.ItemRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecScreenViewModel(
    private val firestoreRepos: FirestoreRepository,
    private val itemRepos: ItemRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _generalUiState = MutableStateFlow<MainScreenState>(MainScreenState.getEmptyInstance())
    val generalUiState: StateFlow<MainScreenState> = _generalUiState

    private val _cardUiState = MutableLiveData<CardDetailUiState>()
    val cardUiState = _cardUiState

    private var firstExtraction = false

    private lateinit var collectionRecommendation: Job

    private var updatesPool = mutableListOf<MutableMap<String, Any?>>()

    fun fetchRec() {
        viewModelScope.launch {
            firestoreRepos.recommendation.collect { docs ->
                if (!firstExtraction) {
                    initUI(docs)
                    firstExtraction = true
                } else {
                    /*
                    docs.forEach {
                        val (actionFlag, docID, docData) = it
                        when (actionFlag) {
                            "ADDED", "REMOVED" -> updatesPool.add(newDoc)
                            "MODIFIED" -> updateUiState(newDoc)
                        }
                    }

                     */
                }
            }
        }
    }

    private fun <K, V> deepCopy(map: Map<K, V>): Map<K, V> {
        return map.mapValues { entry ->
            when (entry.value) {
                is Map<*, *> -> deepCopy(entry.value as Map<Any?, Any?>) as V
                is List<*> -> (entry.value as List<*>).map {
                    when (it) {
                        is Map<*, *> -> deepCopy(it as Map<Any?, Any?>)
                        else -> it
                    }
                } as V
                else -> entry.value
            }
        }
    }

    private fun initUI(states: List<Triple<String, String, FirestoreDoc>>) {
        val recScreenContent = getEmptyContent(states)

        _generalUiState.value = MainScreenState(
            skeletonTitle = true,
            content = recScreenContent
        )

        viewModelScope.launch {
            val stack = mutableListOf(recScreenContent)
            states.forEach { (_, docId, docContent) ->
                val newHash = deepCopy(stack.removeLast())
                val movie = itemRepos.getUserItem(docId, docContent.itemId)
                val newComponent = UIComponent(
                    item = movie,
                    rated = docContent.rated,
                    viewed = docContent.viewed,
                    marked = docContent.marked
                )

                newHash[docContent.category]?.set(
                    docId,
                    newComponent
                )

                val newState = MainScreenState(
                    skeletonTitle = false,
                    content = newHash
                )

                stack.add(newHash as MutableMap<String, MutableMap<String, UIComponent>>)

                _generalUiState.value = newState
            }
        }
    }

    private fun getEmptyContent(
        states: List<Triple<String, String, FirestoreDoc>>
    ): MutableMap<String, MutableMap<String, UIComponent>> {
        val uiComposition: MutableMap<String, MutableMap<String, UIComponent>> = mutableMapOf()
        states.forEach { (_, docId, docContent) ->
            val category = uiComposition.getOrPut(docContent.category) { mutableMapOf() }
            category[docId] = UIComponent()
        }
        return uiComposition
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

data class UIComponent(
    var item: Movie?=null,
    val rated: Int?=null,
    val viewed: Boolean = false,
    val marked: Boolean = false,
)

data class MainScreenState(
    val skeletonTitle: Boolean,
    val content: Map<String, Map<String, UIComponent>>
) {
    companion object {
        fun getEmptyInstance(): MainScreenState {
            return MainScreenState(
                skeletonTitle = true,
                content = (1..3)
                    .toList()
                    .map { it.toString() }
                    .associateWith {
                        (1..10)
                            .toList()
                            .map { it.toString() }
                            .associateWith { UIComponent() }
                    }
            )
        }
    }
}