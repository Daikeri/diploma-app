package com.example.movie_rec_sys.data

import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
) {
    private var _user: FirebaseUser? = auth.currentUser
    val user = _user

    private lateinit var callback: suspend (FirebaseUser?) -> Unit

    fun userExists(): Boolean {
        return _user != null
    }

    suspend fun addNewUser(email: String, password: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                _user = result.user
                _user?.let { callback.invoke(it) }
                AuthResult(true,"")
            } catch (e: FirebaseAuthUserCollisionException) {
                AuthResult(
                    false,
                    exception = "Пользователь с указанным адресом электронной почты уже существует!"
                )
            } catch (e: FirebaseTooManyRequestsException) {
                AuthResult(
                    false,
                    exception = "Доступ был временно заблокирован из-за многочисленных неудачных попыток входа.")

            } catch (e: Exception) {

                AuthResult(false, exception = e.toString())
            }

        }
    }

    suspend fun authExistUser(email: String, password: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                _user = result.user
                _user?.let { callback.invoke(it) }
                AuthResult(true, "")
            } catch (e: FirebaseAuthInvalidUserException) {
                AuthResult(
                    false,
                    exception = "Пользователя с указанной учетной записью не существует!"
                )
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                AuthResult(
                    false,
                    exception = "Указан неверный адерс электронной почты или пароль!"
                )
            } catch (e: FirebaseTooManyRequestsException) {
                AuthResult(
                    false,
                    exception = "Доступ был временно заблокирован из-за многочисленных неудачных попыток входа.")

            } catch (e: Exception) {
                AuthResult(false, exception = e.toString())
            }
        }
    }

    fun addCallback(action: suspend (FirebaseUser?) -> Unit) {
        this.callback = action
    }
}

data class AuthResult(
    val success: Boolean,
    val exception: String,
)
