package com.example.movie_rec_sys

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.movie_rec_sys.navigate.Navigate
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = MaterialTheme.colorScheme.surface,
            ) {
                Navigate(application as MyApplication)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().signOut()
    }
}

