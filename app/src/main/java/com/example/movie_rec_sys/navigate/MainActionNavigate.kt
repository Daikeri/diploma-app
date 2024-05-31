package com.example.movie_rec_sys.navigate

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movie_rec_sys.uitemplate.movie_feed_card.CardDetail
import com.example.movie_rec_sys.uitemplate.movie_feed_card.RecScreen
import com.example.movie_rec_sys.uitemplate.action.MyNavBottomBar
import com.example.movie_rec_sys.viewmodel.RecScreenViewModel


object MainActionRoute {
    const val PERSONAL = "Personal"
    const val LIST = "MyList"
    const val SEARCH = "Search"
    const val PROFILE = "Profile"
    const val DETAIL = "Detail"
}

data class MainActionTab(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun MainActionScreen() {
    val tabsNavController = rememberNavController()

    tabsNavController.addOnDestinationChangedListener { controller, destination, arguments ->
        Log.d("NavBackStack", "Current destination: ${destination.route}")
    }

    Column(verticalArrangement = Arrangement.Bottom) {
        NavHost(
            navController = tabsNavController,
            startDestination = MainActionRoute.PERSONAL,
            modifier = Modifier.weight(1f)
        ) {
            composable(MainActionRoute.PERSONAL) {
                RecScreen(
                    toDetail = { value ->
                        tabsNavController.navigate("${MainActionRoute.DETAIL}/${value}") {
                            popUpTo(tabsNavController.graph.findStartDestination().id) {saveState = true}
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(MainActionRoute.LIST) {}
            composable(MainActionRoute.SEARCH) {}
            composable(MainActionRoute.PROFILE) {}
            composable(
                route = "${MainActionRoute.DETAIL}/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.StringType })
            ) {
                val parentEntry = remember(it) {
                    tabsNavController.getBackStackEntry(MainActionRoute.PERSONAL)
                }
                val parentViewModel: RecScreenViewModel = viewModel(parentEntry)
                val movieId = it.arguments?.getString("movieId") ?: ""
                CardDetail(movieId, viewModel = parentViewModel)
            }
            composable("some") { CardDetail() }
        }
        MyNavBottomBar(tabsNavController)
    }
}