package com.example.movie_rec_sys.navigate

import android.util.Log
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movie_rec_sys.uitemplate.recommendation.CardDetail
import com.example.movie_rec_sys.uitemplate.recommendation.RecScreen
import com.example.movie_rec_sys.uitemplate.action.MyNavBottomBar
import com.example.movie_rec_sys.uitemplate.search.SearchScreen
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainActionScreen() {
    val tabsNavController = rememberNavController()

    tabsNavController.addOnDestinationChangedListener { controller, destination, arguments ->
        Log.d("NavBackStack", "Current destination: ${destination.route}")
    }

    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {})

    Column(
        verticalArrangement = Arrangement.Bottom,
    ) {
        NavHost(
            navController = tabsNavController,
            startDestination = MainActionRoute.PERSONAL,
            modifier = Modifier.weight(1f)
                .pullRefresh(pullRefreshState)
        ) {
            composable(MainActionRoute.PERSONAL) {
                RecScreen(
                    toDetail = { category, item ->
                        tabsNavController.navigate("${MainActionRoute.DETAIL}/${category}/${item}") {
                            popUpTo(tabsNavController.graph.findStartDestination().id) {saveState = true}
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(MainActionRoute.LIST) {}
            composable(MainActionRoute.SEARCH) {
                SearchScreen()
            }
            composable(MainActionRoute.PROFILE) {}
            composable(
                route = "${MainActionRoute.DETAIL}/{categoryName}/{movieId}",
                arguments = listOf(
                    navArgument("categoryName") { type = NavType.StringType },
                    navArgument("movieId") { type = NavType.StringType }
                )
            ) {
                val parentEntry = remember(it) {
                    tabsNavController.getBackStackEntry(MainActionRoute.PERSONAL)
                }
                val parentViewModel: RecScreenViewModel = viewModel(parentEntry)
                val categoryName = it.arguments!!.getString("categoryName") ?: ""
                val movieId: String = it.arguments!!.getString("movieId") ?: ""
                CardDetail(categoryName, movieId, viewModel = parentViewModel)
            }

        }
        MyNavBottomBar(tabsNavController)
    }
}

