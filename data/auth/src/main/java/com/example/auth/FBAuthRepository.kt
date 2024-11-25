package com.example.auth

import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")
class FBAuthRepository @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuth
) {
    val userState = MutableStateFlow<FirebaseUser?>(firebaseAuthDataSource.currentUser)

    init {
        val innerFlow = callbackFlow {
            val authStateListener = FirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser)
            }

            firebaseAuthDataSource.addAuthStateListener(authStateListener)

            awaitClose {  firebaseAuthDataSource.removeAuthStateListener(authStateListener) }
        }.flowOn(Dispatchers.IO)

        CoroutineScope(Dispatchers.IO).launch {
            innerFlow.collect { state ->
                userState.emit(state)
            }
        }
    }

    suspend fun createUser(email: String, password: String): AuthState {
        return withContext(Dispatchers.IO) {
            try {
                val networkRequest = firebaseAuthDataSource
                    .createUserWithEmailAndPassword(email, password)
                    .await()
                AuthState.Success
            } catch (e: FirebaseAuthUserCollisionException) {
                AuthState.UserWithEmailExist
            } catch (e: FirebaseTooManyRequestsException) {
                AuthState.TooManyRequests
            }
        }
    }

    suspend fun authExistUser(email: String, password: String): AuthState {
        return withContext(Dispatchers.IO) {
            try {
                val networkRequest = firebaseAuthDataSource
                    .signInWithEmailAndPassword(email, password)
                    .await()
                AuthState.Success
            } catch (e: FirebaseAuthInvalidUserException) {
                AuthState.UserDoesNotExist
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                AuthState.IncorrectEmailOrPassword
            } catch (e: FirebaseTooManyRequestsException) {
                AuthState.TooManyRequests
            }
        }
    }

    suspend fun logOut() {

    }
}

enum class AuthState {
    UserWithEmailExist,
    TooManyRequests,
    UserDoesNotExist,
    IncorrectEmailOrPassword,
    Success,
}

