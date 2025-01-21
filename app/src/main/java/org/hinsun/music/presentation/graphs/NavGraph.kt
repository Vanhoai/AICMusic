package org.hinsun.music.presentation.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.hinsun.music.presentation.auth.AuthView
import org.hinsun.music.presentation.onboard.OnBoardView
import org.hinsun.music.presentation.swipe.SwipeView
import org.hinsun.music.presentation.swipe.setting.animation.AnimationView
import org.hinsun.music.presentation.swipe.setting.appearance.AppearanceView
import org.hinsun.music.presentation.swipe.setting.audio.AudioView
import org.hinsun.music.presentation.swipe.setting.downloading.DownloadingView
import org.hinsun.music.presentation.swipe.setting.storage.StorageView
import org.hinsun.music.presentation.swipe.setting.style.StyleView

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = NavRoute.SWIPE.path
    ) {
        composable(NavRoute.ONBOARD.path) { OnBoardView(navHostController) }
        composable(NavRoute.AUTH.path) { AuthView(navHostController) }
        composable(NavRoute.SWIPE.path) { SwipeView(navHostController) }

        composable(NavRoute.APPEARANCE.path) { AppearanceView(navHostController) }
        composable(NavRoute.ANIMATION.path) { AnimationView(navHostController) }
        composable(NavRoute.AUDIO.path) { AudioView(navHostController) }
        composable(NavRoute.DOWNLOADING.path) { DownloadingView(navHostController) }
        composable(NavRoute.STORAGE.path) { StorageView(navHostController) }
        composable(NavRoute.STYLE.path) { StyleView(navHostController) }
    }
}