package com.hraj9258.movietv.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.hraj9258.movietv.navigation.domain.AppNavigationDestinations
import com.hraj9258.movietv.ui.details.DetailsScreenRoot
import com.hraj9258.movietv.ui.home.HomeScreenRoot

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppNavigationDestinations.Home
    ) {
        composable<AppNavigationDestinations.Home>() {
            HomeScreenRoot(
                onTitleClick = { titleId ->
                    navController.navigate(AppNavigationDestinations.Details(titleId))
                }
            )
        }

        composable<AppNavigationDestinations.Details>() { backStackEntry ->
            val route = backStackEntry.toRoute<AppNavigationDestinations.Details>()
            val titleId = route.titleId
            DetailsScreenRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                titleId = titleId
            )
        }
    }
}