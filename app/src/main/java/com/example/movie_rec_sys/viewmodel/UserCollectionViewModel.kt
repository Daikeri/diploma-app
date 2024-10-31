package com.example.movie_rec_sys.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.movie_rec_sys.MyApplication
import com.example.movie_rec_sys.data.FirestoreRemoteSource
import com.example.movie_rec_sys.data.FSRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserCollectionViewModel(
    private val itemRepos: FSRepository,
    private val firestoreRepos: FirestoreRemoteSource,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = MutableStateFlow<UserCollectionState?>(null)
    val uiState: StateFlow<UserCollectionState?> = _uiState

    init {
        viewModelScope.launch {
            firestoreRepos.usersCollection.collect { updates ->

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

                return UserCollectionViewModel(
                    (application as MyApplication).appContainer.itemRepos,
                    (application as MyApplication).appContainer.fireStoreRepos,
                    savedStateHandle
                ) as T
            }
        }
    }
}

data class UserCollectionState(
    val content: Map<String, Map<String, UserCollectionItem>>
)

data class UserCollectionItem(
    private val some: String
)