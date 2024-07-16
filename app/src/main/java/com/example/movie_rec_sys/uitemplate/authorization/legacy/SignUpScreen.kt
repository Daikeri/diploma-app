package com.example.movie_rec_sys.uitemplate.authorization.legacy

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.data.AuthResult
import com.example.movie_rec_sys.viewmodel.AuthViewModel
import kotlinx.coroutines.launch


@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    toRecScreen: () -> Unit
) {
    val authResult: AuthResult by viewModel.uiState.observeAsState(AuthResult(success = false, exception = ""))
    val localScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarHost(
        snackbarHostState,
        snackbar = {
            Snackbar(
                snackbarData = it,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier.padding(4.dp),
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") }
        )

        OutlinedTextField(
            modifier = Modifier.padding(4.dp),
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") }
        )

        OutlinedButton(
            onClick = {
                localScope.launch {
                    viewModel.addNewUser(email, password)
                    if (authResult.success){
                        toRecScreen()
                    }
                    else {
                        //Log.e("SIGN UP", "$authResult.success.toString() ${authResult.exception}")
                        snackbarHostState.showSnackbar(message = authResult.exception)
                    }
                }
            },
            modifier = Modifier.padding(4.dp)
        ) {
            Text("Sig Up")
        }
    }
}

