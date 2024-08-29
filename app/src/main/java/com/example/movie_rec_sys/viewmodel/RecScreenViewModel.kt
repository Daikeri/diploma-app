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
import com.example.movie_rec_sys.data.FirestoreRemoteSource
import com.example.movie_rec_sys.data.FSRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecScreenViewModel(
    private val firestoreRepos: FirestoreRemoteSource,
    private val itemRepos: FSRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _generalUiState = MutableStateFlow<RecScreenCommonUiState?>(null)
    val generalUiState: StateFlow<RecScreenCommonUiState?> = _generalUiState

    private val _cardUiState = MutableLiveData<CardDetailUiState>()
    val cardUiState = _cardUiState

    private var firstExtraction = false

    private var updatesPool = mutableListOf<MutableMap<String, Any?>>()

    init {
        viewModelScope.launch {
            itemRepos.recScreenData.collect { uiStruct ->
                val lastElement = (10 - 1) * 250
                val uiContent = uiStruct.mapValues { category ->
                    var indexesCount = 0
                    category.value.mapValues { item ->
                        val result = if (item.value.second == null)
                            RecScreenComponentUiState(
                                repeatDelay = lastElement,
                                downloadIndex = indexesCount
                            )
                        else
                            RecScreenComponentUiState(
                                item = item.value.second
                            )
                        indexesCount += 1
                        result
                    }
                }
                _generalUiState.value = RecScreenCommonUiState(false, content = uiContent)
            }
        }
    }

    fun onUserChooseItem(category: String, document: String) {
        val targetItem = itemRepos.getItem(category, document)
        _cardUiState.value = CardDetailUiState(
            item = targetItem!!.second!!,
            rated = targetItem.first.rated,
            viewed = targetItem.first.viewed,
            marked = targetItem.first.marked
        )
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

data class RecScreenComponentUiState(
    var item: Movie?=null,
    val repeatDelay: Int = 0,
    val downloadIndex: Int =0
)

data class RecScreenCommonUiState(
    val skeletonTitle: Boolean,
    val content: Map<String, Map<String, RecScreenComponentUiState>>
)