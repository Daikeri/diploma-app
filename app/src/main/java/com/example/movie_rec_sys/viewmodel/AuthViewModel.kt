package com.example.movie_rec_sys.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.movie_rec_sys.MyApplication
import com.example.movie_rec_sys.data.AuthResult
import com.example.movie_rec_sys.data.FirebaseAuthRepository


class AuthViewModel(
    private val repos: FirebaseAuthRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableLiveData<AuthResult>()
    val uiState = _uiState

    suspend fun addNewUser(email: String, password: String) {
        _uiState.value = repos.addNewUser(email, password)
    }

    suspend fun authExistUser(email: String, password: String) {
        _uiState.value = repos.authExistUser(email, password)
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

                return AuthViewModel(
                    (application as MyApplication).appContainer.fireBaseRepos,
                    savedStateHandle
                ) as T
            }
        }
    }
}