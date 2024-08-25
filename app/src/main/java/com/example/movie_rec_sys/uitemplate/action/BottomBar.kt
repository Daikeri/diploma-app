package com.example.movie_rec_sys.uitemplate.action

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.movie_rec_sys.navigate.MainActionRoute
import com.example.movie_rec_sys.navigate.MainActionTab

object Tabs {
    val listOfTabs = listOf(
        MainActionTab(
            route = MainActionRoute.PERSONAL,
            icon = Icons.Rounded.Home,
            label = "Главная"
        ),
        /*
        MainActionTab(
            route = MainActionRoute.LIST,
            icon = Icons.Rounded.Favorite,
            label = "Мое"
        ),
         */
        MainActionTab(
            route = MainActionRoute.SEARCH,
            icon = Icons.Rounded.Search,
            label = "Поиск"
        ),
        MainActionTab(
            route = MainActionRoute.PROFILE,
            icon = Icons.Rounded.Person,
            label = "Профиль"
        ),
    )
}

@Composable
fun MyNavBottomBar(navController: NavController) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        Tabs.listOfTabs.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {saveState = true}
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(imageVector = navItem.icon,
                        contentDescription = navItem.label)
                },
                label = {
                    Text(text = navItem.label)
                }
            )
        }
    }
}



