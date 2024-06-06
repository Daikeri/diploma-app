package com.example.movie_rec_sys.uitemplate.authorization.legacy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.data.AuthResult
import com.example.movie_rec_sys.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Preview
@Composable
fun SignInScreen(
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    toRecScreen: () -> Unit = {},
    emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$"),
    passwordRegex: Regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+\$).{8,}\$")
) {
    val authResult: AuthResult by viewModel.uiState.observeAsState(AuthResult(success = false, exception = ""))
    val localScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var firstInputEmail by remember { mutableStateOf(true) }
    var firstInputPassword by remember { mutableStateOf(true) }
    var correctInputEmail by remember { mutableStateOf(false) }
    var correctInputPassword by remember { mutableStateOf(false) }
    var correctInputGeneral by remember { mutableStateOf(correctInputEmail && correctInputPassword) }
    var buttonLock by remember { mutableStateOf(correctInputGeneral) }

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
        // Email
        OutlinedTextField(
            modifier = Modifier.padding(4.dp),
            value = email,
            onValueChange = {
                if (firstInputEmail)
                    firstInputEmail = false
                email = it
                correctInputEmail = emailRegex.matches(email)
                correctInputGeneral = correctInputEmail && correctInputPassword
                buttonLock = correctInputGeneral
            },
            isError = if (firstInputEmail) false else !correctInputEmail,
            label = { Text(text = "Email") },
        )

        //Password
        OutlinedTextField(
            modifier = Modifier.padding(4.dp),
            value = password,
            onValueChange = {
                if (firstInputPassword)
                    firstInputPassword = false
                password = it
                correctInputPassword = passwordRegex.matches(password)
                correctInputGeneral = correctInputEmail && correctInputPassword
                buttonLock = correctInputGeneral
            },
            isError = if (firstInputPassword) false else !correctInputPassword,
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(text = "Password") }
        )

        OutlinedButton(
            onClick = {
                localScope.launch {
                    viewModel.authExistUser(email, password)
                    if (authResult.success){
                        toRecScreen()
                    }
                    else {
                        snackbarHostState.showSnackbar(message = authResult.exception)
                    }
                }
            },
            modifier = Modifier.padding(4.dp),
            enabled = buttonLock
        ) {
            Text("Sig in")
        }
    }
}