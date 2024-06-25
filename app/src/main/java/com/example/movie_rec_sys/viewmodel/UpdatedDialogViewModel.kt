package com.example.movie_rec_sys.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.movie_rec_sys.MyApplication
import com.example.movie_rec_sys.data.ItemRepository
import com.example.movie_rec_sys.data.Movie
import com.example.movie_rec_sys.data.PrimaryRecRepository
import kotlinx.coroutines.launch

class UpdatedDialogViewModel(
    private val primaryRepos: PrimaryRecRepository,
    private val itemRepos: ItemRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState: MutableLiveData<DialogUiState> = MutableLiveData()
    val uiState: LiveData<DialogUiState> = _uiState

    private lateinit var sourceResponse: MutableMap<String, MutableMap<String, Any?>>
    private lateinit var movies: List<Movie>
    private var currentItemIndex = 0

    fun getItems() {
        viewModelScope.launch {
            sourceResponse = primaryRepos.fetchIDs()
            movies = sourceResponse.map { (_, value) ->
                itemRepos.getUserItem(value["imdbId"].toString())
            }
            _uiState.value = DialogUiState(
                loading = false,
                item = movies[currentItemIndex]
            )
        }
    }

    fun itemFromCollect(action: Boolean) {
        if (action) {
            if (currentItemIndex < movies.size - 1) {
                currentItemIndex += 1
                _uiState.value = DialogUiState(
                    loading = false,
                    item = movies[currentItemIndex]
                )
            }
        } else {
            if (currentItemIndex > 0) {
                currentItemIndex -= 1
                _uiState.value = DialogUiState(
                    loading = false,
                    item = movies[currentItemIndex]
                )
            }
        }
    }

    fun markItem(action: Boolean) {
        sourceResponse.values.toList()[currentItemIndex]["mark"] = action
        Log.e("ITEM STATE AFTER MARK", "${sourceResponse.values}")
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

                return UpdatedDialogViewModel(
                    (application as MyApplication).appContainer.primaryRecRepos,
                    (application as MyApplication).appContainer.itemRepos,
                    savedStateHandle
                ) as T
            }
        }
    }
}

data class DialogUiState(
    val loading: Boolean,
    val item: Movie?=null
) {
    companion object {
        fun getEmptyInstance(): DialogUiState {
            return DialogUiState(
                loading = true
            )
        }
    }
}