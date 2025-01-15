package org.ic.tech.music.presentation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.ic.tech.music.presentation.auth.AuthView
import org.ic.tech.music.presentation.onboard.OnBoardView
import org.ic.tech.music.presentation.swipe.SwipeView

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = NavRoute.AUTH.path
    ) {
        composable(NavRoute.ONBOARD.path) {
            OnBoardView(navHostController)
        }

        composable(NavRoute.AUTH.path) {
            AuthView(navHostController)
        }

        composable(NavRoute.SWIPE.path) {
            SwipeView()
        }
    }
}