package com.example.movie_rec_sys.uitemplate.authorization.legacy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AuthorizationScreen() {
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
            onClick = {},
            modifier = Modifier.padding(4.dp)
        ) {
            Text("Sig up")
        }
    }
}

