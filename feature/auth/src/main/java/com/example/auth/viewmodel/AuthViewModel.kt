package com.example.auth.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.auth.AuthState
import com.example.auth.FBAuthRepository
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseRepos: FBAuthRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableLiveData<AuthUiState?>(null)
    val uiState = _uiState

    private var metaData: MutableMap<String, Any?> = mutableMapOf()

    private var numAttempt = 0

    suspend fun createUser(email: String, password: String) {
        val networkResult = firebaseRepos.createUser(email, password)
        compareWithMessage(networkResult)
    }

    suspend fun authExistUser(email: String, password: String) {
        val networkResult = firebaseRepos.authExistUser(email, password)
        compareWithMessage(networkResult)
    }

    private fun compareWithMessage(state: AuthState) {
        Log.e("From VM", "${state}")
        _uiState.value = when(state) {
            AuthState.UserWithEmailExist -> AuthUiState(
                errorMessage = "Пользователь с указанным адресом электронной почты уже существует!",
                isSuccessful = false,
                attempt = numAttempt++
            )
            AuthState.TooManyRequests -> AuthUiState(
                errorMessage = "Доступ был временно заблокирован из-за многочисленных неудачных попыток входа",
                isSuccessful = false,
                numAttempt++
            )
            AuthState.UserDoesNotExist -> AuthUiState(
                errorMessage = "Пользователя с указанной учетной записью не существует!",
                isSuccessful = false,
                numAttempt++
            )
            AuthState.IncorrectEmailOrPassword -> AuthUiState(
                errorMessage = "Указан неверный адерс электронной почты или пароль!",
                isSuccessful = false,
                numAttempt++
            )
            AuthState.Success -> AuthUiState(
                errorMessage = null,
                isSuccessful = true,
                numAttempt++
            )
        }
        Log.e("uiState", "${_uiState.value}")
    }

    fun addMetaData(key: String, value: Any?) {
        metaData[key] = value
    }

    /*
    fun uploadMetaData() {
        viewModelScope.launch {
            firestoreRepos.uploadMetaData(metaData)
        }
    }
     */
}

data class AuthUiState(
    val errorMessage: String?,
    val isSuccessful: Boolean,
    val attempt: Int
)