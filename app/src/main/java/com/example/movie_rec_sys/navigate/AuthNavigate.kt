package com.example.movie_rec_sys.navigate

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.movie_rec_sys.MyApplication
import com.example.movie_rec_sys.uitemplate.action.MyDialog
import com.example.movie_rec_sys.uitemplate.authorization.AgeGroupScreen
import com.example.movie_rec_sys.uitemplate.authorization.GenderScreen
import com.example.movie_rec_sys.uitemplate.authorization.GeneralScreen
import com.example.movie_rec_sys.uitemplate.authorization.SignScreen

@Composable
fun Navigate(app: MyApplication) {
    val startDestination = //"choose_age_group"
       if (app.appContainer.fireBaseRepos.userExists()) "recommend-screen" else "general screen"
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("general screen") {
            GeneralScreen(
                toIn = { navController.navigate("log in") },
                toUp = { navController.navigate("sign up") }
            )
        }
        composable("log in") {
            SignScreen(
                fillingAuthButton = "Log in",
                toRecScreen = { navController.navigate("recommend-screen") }
            )
        }
        composable("sign up") {
            SignScreen(
                fillingAuthButton = "Sign up",
                toRecScreen = { navController.navigate("recommend-dialog") }
            )
        }
        composable("choose_gender") {
            GenderScreen()
        }
        composable("choose_age_group") {
            AgeGroupScreen()
        }
        dialog("recommend-dialog") {
            MyDialog({navController.navigate("recommend-screen")})
        }

        composable("recommend-screen") {
            MainActionScreen()
        }
    }
}
