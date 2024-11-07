package com.example.test

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.auth.ui.InputFormScreen
import com.example.auth.ui.SelectionScreen
import kotlinx.serialization.Serializable


@Serializable object SelectionAuth
@Serializable
data class InputForm(val hasRegisteredBefore: Boolean)
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
          SelectionScreen { authState ->
              navController.navigate(InputForm(authState))
          }
        }

        composable<InputForm> {
            val passArg = it.toRoute<InputForm>()
            InputFormScreen(
                hasRegisteredBefore = passArg.hasRegisteredBefore
            ) {
                navController.navigate(RecFeed)
            }
        }

        composable<ChooseGender> {

        }

        composable<ChooseAge> {

        }

        navigation<BottomNavigate>(startDestination = RecFeed) {
            composable<RecFeed> {  }
            composable<PersonalList> {  }
            composable<Search> {  }
            composable<Profile> {  }
        }
    }
}
