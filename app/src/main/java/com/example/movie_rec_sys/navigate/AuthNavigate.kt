package com.example.movie_rec_sys.navigate

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.movie_rec_sys.MyApplication
import com.example.movie_rec_sys.uitemplate.action.UpdatedSurveyDialog
import com.example.movie_rec_sys.uitemplate.authorization.AgeGroupScreen
import com.example.movie_rec_sys.uitemplate.authorization.GenderScreen
import com.example.movie_rec_sys.uitemplate.authorization.GeneralScreen
import com.example.movie_rec_sys.uitemplate.authorization.SignScreen
import com.example.movie_rec_sys.viewmodel.AuthViewModel

@Composable
fun Navigate(
    app: MyApplication,
    navController: NavHostController = rememberNavController(),
) {

    val startDestination = when(app.appContainer.fireBaseRepos.userExists()) {
        true -> "recommend-screen"
        false -> "general screen"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("general screen") {
            GeneralScreen(
                toIn = { navController.navigate("log_in") },
                toUp = { navController.navigate("sign_up") }
            )
        }
        composable("log_in") {
            SignScreen(fillingAuthButton = "Log in") {
                navController.navigate("recommend-screen")
            }
        }
        composable("sign_up") {
            SignScreen(fillingAuthButton = "Sign up") {
                navController.navigate("choose_gender")
            }
        }
        composable("choose_gender") {
            val parentBackStackEntry = remember(it) {
                navController.getBackStackEntry("sign_up")
            }

            val parentViewModel:AuthViewModel = viewModel(parentBackStackEntry)

            GenderScreen(parentViewModel) {
                navController.navigate("choose_age_group")
            }
        }
        composable("choose_age_group") {
            val parentBackStackEntry = remember(it) {
                navController.getBackStackEntry("sign_up")
            }

            val parentViewModel:AuthViewModel = viewModel(parentBackStackEntry)

            AgeGroupScreen(parentViewModel) {
                navController.navigate("recommend-dialog")
            }
        }
        dialog("recommend-dialog") {
            UpdatedSurveyDialog {
                navController.navigate("recommend-screen")
            }
        }
        composable("recommend-screen") {
            MainActionScreen()
        }
    }
}
