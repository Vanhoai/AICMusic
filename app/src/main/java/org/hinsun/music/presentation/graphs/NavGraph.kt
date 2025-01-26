package org.hinsun.music.presentation.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.hinsun.music.presentation.auth.AuthView
import org.hinsun.music.presentation.music.player.PlayerView
import org.hinsun.music.presentation.music.playlist.PlaylistView
import org.hinsun.music.presentation.onboard.OnBoardView
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

            composable(NavRoute.PLAYER.path) {
                PlayerView(
                    navHostController = navHostController,
                    animatedVisibilityScope = this
                )
            }

            composable(route = NavRoute.PLAYLIST.path) {
                PlaylistView(
                    navHostController = navHostController,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}