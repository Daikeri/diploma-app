package com.example.movie_rec_sys.uitemplate.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_rec_sys.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Preview
@Composable
fun GeneralScreen(
    toIn:() -> Unit = {},
    toUp: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .size(200.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor =  MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { toIn() },
                    modifier = Modifier.padding(4.dp),
                    colors = ButtonColors(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.onPrimary,
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 20.dp)
                ) {
                    Text(
                        text="Sign in",
                    )
                }

                Button(
                    onClick = { toUp() },
                    modifier = Modifier.padding(4.dp),
                    colors = ButtonColors(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.onPrimary,
                    )
                ) {
                    Text(
                        text="Sign up",
                    )
                }
            }
        }
    }
}