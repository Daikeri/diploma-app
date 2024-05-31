package com.example.movie_rec_sys.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.movie_rec_sys.MyApplication
import com.example.movie_rec_sys.data.Movie
import com.example.movie_rec_sys.data.ItemRepository
import com.example.movie_rec_sys.data.PrimaryRecRepository
import kotlinx.coroutines.launch

class DialogViewModel(
    private val primaryRepos: PrimaryRecRepository,
    private val itemRepository: ItemRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableLiveData<List<Movie>>()
    val uiState = _uiState

    private val _sendSuccessful = MutableLiveData<Boolean>()
    val sendSuccessful = _sendSuccessful

    private lateinit var userFeedback: MutableMap<String, MutableMap<String, Any?>>
    private var markers = mutableMapOf<String, String>()

    fun getItem() {
        viewModelScope.launch {
            userFeedback = primaryRepos.fetchIDs()

            _uiState.value = itemRepository.getUserItems(
                userFeedback.values.map { it["imdbId"].toString() }
            )

            markers = (1..5).toList().map {
                it.toString()
            }.zip(userFeedback.keys).toMap().toMutableMap()
        }
    }

    fun markItem(key: String, value: Boolean) {
        val movie = markers[key]
        userFeedback[movie]?.set("mark", value)
    }

    fun sendFeedback() {
        viewModelScope.launch {
            sendSuccessful.value = primaryRepos.sendFeedback(userFeedback)
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

                return DialogViewModel(
                    (application as MyApplication).appContainer.primaryRecRepos,
                    (application as MyApplication).appContainer.itemRepos,
                    savedStateHandle
                ) as T
            }
        }
    }
}
