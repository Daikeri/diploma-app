package com.example.movie_rec_sys.uitemplate.authorization

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.data.AuthResult
import com.example.movie_rec_sys.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun SignScreen(
    fillingAuthButton: String,
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



    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SnackbarHost(
            snackbarHostState,
            snackbar = {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            }
        )
        Card(
            modifier = Modifier
                .width(325.dp)
                .height(350.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor =  MaterialTheme.colorScheme.onTertiaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Email
                OutlinedTextField(
                    modifier = Modifier.padding(bottom = 4.dp),
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

                Column {
                    DropdownMenuWithLabel("Пол", listOf("Мужской", "Женский"))
                    DropdownMenuWithLabel("Возраст", listOf("18-25", "25-40", "40-70"))
                }

                Button(
                    onClick = {
                        localScope.launch {
                            if (fillingAuthButton == "Sign up") {
                                viewModel.addNewUser(email, password)
                            }
                            else {
                                viewModel.authExistUser(email, password)
                            }
                            if (authResult.success){
                                toRecScreen()
                            }
                            else {
                                //Log.e("FIREBASE-VIEWMODEL", "$authResult")
                                snackbarHostState.showSnackbar(message = authResult.exception)
                            }
                        }
                    },
                    modifier = Modifier.padding(4.dp),
                    enabled = buttonLock
                ) {
                    Text(fillingAuthButton)
                }
            }
        }
    }
}


@Composable
fun DropdownMenuWithLabel(label: String, options: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Option 1") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(80.dp)
    ) {
        Text(
            text = "$label:",
            fontSize = 18.sp,
            modifier = Modifier
                .width(85.dp) // Фиксированная ширина для лейбла
                .padding(end = 8.dp)
        )
        Box(
            modifier = Modifier
                .width(200.dp) // Фиксированная ширина для выпадающего списка
        ) {
            Text(
                text = selectedOption,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .padding(16.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(8.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOption = option
                            expanded = false
                        },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}
