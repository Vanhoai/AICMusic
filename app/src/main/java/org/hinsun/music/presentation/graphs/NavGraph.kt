package org.hinsun.music.presentation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.hinsun.music.presentation.auth.AuthView
import org.hinsun.music.presentation.onboard.OnBoardView
import org.hinsun.music.presentation.swipe.SwipeView

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = NavRoute.ONBOARD.path
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