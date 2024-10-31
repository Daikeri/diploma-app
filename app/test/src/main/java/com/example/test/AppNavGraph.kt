package com.example.test

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable


@Serializable object SelectionAuth
@Serializable object InputForm
@Serializable object ChooseGender
@Serializable object ChooseAge
@Serializable object BottomNavigate
@Serializable object RecFeed
@Serializable object PersonalList
@Serializable object Search
@Serializable object Profile

@Composable
fun ApplicationNavigationGraph(userLoggedIn: Boolean) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (userLoggedIn) BottomNavigate else SelectionAuth
    ) {
        composable<SelectionAuth> {

        }

        composable<InputForm> {

        }

        composable<ChooseGender> {

        }

        composable<ChooseAge> {

        }

        navigation<BottomNavigate>(startDestination = RecFeed) {
            composable<PersonalList> {  }
            composable<Search> {  }
            composable<Profile> {  }
        }
    }
}