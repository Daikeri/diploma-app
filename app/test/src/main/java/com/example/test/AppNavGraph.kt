package com.example.test

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.auth.ui.InputFormScreen
import com.example.auth.ui.SelectionScreen
import kotlinx.serialization.Serializable
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.pulltorefresh.*


@Serializable object SelectionAuth
@Serializable
data class InputForm(val hasRegisteredBefore: Boolean)
@Serializable object ChooseGender
@Serializable object ChooseAge
@Serializable object BottomNavigate
@Serializable object RecFeed
@Serializable object MainScreenWithBottomBar
@Serializable object PersonalList
@Serializable object Search
@Serializable object Profile


@Composable
fun ApplicationNavigationGraph(userLoggedIn: Boolean) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (userLoggedIn) MainScreenWithBottomBar else SelectionAuth
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
                navController.navigate(ChooseGender)
            }
        }

        composable<ChooseGender> {

        }

        composable<ChooseAge> {

        }

        composable<MainScreenWithBottomBar> {
            MainScreenWithBottomBar()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithBottomBar() {
    Column {
        val navController = rememberNavController()
        var isRefreshing by remember { mutableStateOf(false) }
        val pullToRefreshState = rememberPullToRefreshState()
       
        NavHost(
            navController = navController,
            startDestination = RecFeed,
            modifier = Modifier
                .pullToRefresh(
                    isRefreshing = isRefreshing,
                    state = pullToRefreshState
                ) {}
        ) {
            composable<RecFeed> {  }
            composable<PersonalList> {  }
            composable<Search> {  }
            composable<Profile> {  }
        }

        MyBottomBar()
    }
}

@Composable
fun MyBottomBar() {

}