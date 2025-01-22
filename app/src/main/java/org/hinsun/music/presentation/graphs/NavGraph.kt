package org.hinsun.music.presentation.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.hinsun.music.presentation.auth.AuthView
import org.hinsun.music.presentation.onboard.OnBoardView
import org.hinsun.music.presentation.player.PlayerView
import org.hinsun.music.presentation.swipe.SwipeView
import org.hinsun.music.presentation.swipe.setting.advanced.develop.DeveloperOptionsView
import org.hinsun.music.presentation.swipe.setting.music.animation.AnimationView
import org.hinsun.music.presentation.swipe.setting.music.appearance.AppearanceView
import org.hinsun.music.presentation.swipe.setting.music.audio.AudioView
import org.hinsun.music.presentation.swipe.setting.music.downloading.DownloadingView
import org.hinsun.music.presentation.swipe.setting.music.storage.StorageView
import org.hinsun.music.presentation.swipe.setting.music.style.StyleView

@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavGraph(navHostController: NavHostController) {
    SharedTransitionLayout {
        NavHost(
            navController = navHostController,
            startDestination = NavRoute.SWIPE.path
        ) {
            composable(NavRoute.ONBOARD.path) { OnBoardView(navHostController) }
            composable(NavRoute.AUTH.path) { AuthView(navHostController) }
            composable(NavRoute.SWIPE.path) {
                SwipeView(
                    navHostController = navHostController,
                    animatedVisibilityScope = this
                )
            }

            composable(NavRoute.APPEARANCE.path) { AppearanceView(navHostController) }
            composable(NavRoute.ANIMATION.path) { AnimationView(navHostController) }
            composable(NavRoute.AUDIO.path) { AudioView(navHostController) }
            composable(NavRoute.DOWNLOADING.path) { DownloadingView(navHostController) }
            composable(NavRoute.STORAGE.path) { StorageView(navHostController) }
            composable(NavRoute.STYLE.path) { StyleView(navHostController) }
            composable(NavRoute.DEVELOPER.path) { DeveloperOptionsView(navHostController) }

            composable(
                route = "${NavRoute.PLAYER.path}/{idImage}/{idName}",
                arguments = listOf(
                    navArgument("idImage") { type = NavType.IntType },
                    navArgument("idName") { type = NavType.IntType }
                )
            ) {
                val idName = it.arguments?.getInt("idName") ?: 1000
                val idImage = it.arguments?.getInt("idImage") ?: 2000

                PlayerView(
                    navHostController = navHostController,
                    animatedVisibilityScope = this,
                    idName = idName,
                    idImage = idImage
                )
            }
        }
    }
}