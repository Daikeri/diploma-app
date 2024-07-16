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
import com.example.movie_rec_sys.data.AuthResult
import com.example.movie_rec_sys.data.FirebaseAuthRepository
import com.example.movie_rec_sys.data.FirestoreRepository
import kotlinx.coroutines.launch


class AuthViewModel(
    private val firebaseRepos: FirebaseAuthRepository,
    private val firestoreRepos: FirestoreRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableLiveData<AuthResult>()
    val uiState = _uiState

    private var metaData: MutableMap<String, Any?> = mutableMapOf()

    suspend fun addNewUser(email: String, password: String) {
        _uiState.value = firebaseRepos.addNewUser(email, password)
    }

    suspend fun authExistUser(email: String, password: String) {
        _uiState.value = firebaseRepos.authExistUser(email, password)
    }

    fun addMetaData(key: String, value: Any?) {
        metaData[key] = value
    }

    fun uploadMetaData() {
        viewModelScope.launch {
            firestoreRepos.uploadMetaData(metaData)
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

                return AuthViewModel(
                    (application as MyApplication).appContainer.fireBaseRepos,
                    (application as MyApplication).appContainer.fireStoreRepos,
                    savedStateHandle
                ) as T
            }
        }
    }
}