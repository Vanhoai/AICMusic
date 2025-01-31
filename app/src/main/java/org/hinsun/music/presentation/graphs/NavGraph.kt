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
import org.hinsun.music.presentation.not_found.NotFoundView
import org.hinsun.music.presentation.onboard.OnBoardView
import org.hinsun.music.presentation.swipe.SwipeView
import org.hinsun.music.presentation.swipe.more.about.AboutView
import org.hinsun.music.presentation.swipe.more.authentication.AuthenticationView
import org.hinsun.music.presentation.swipe.more.contact.ContactView
import org.hinsun.music.presentation.swipe.more.notification.NotificationView
import org.hinsun.music.presentation.swipe.more.privacy_policy.PrivacyAndPolicyView
import org.hinsun.music.presentation.swipe.more.profile.ProfileView
import org.hinsun.music.presentation.swipe.setting.advanced.develop.DeveloperOptionsView
import org.hinsun.music.presentation.swipe.setting.music.animation.AnimationView
import org.hinsun.music.presentation.swipe.setting.music.appearance.AppearanceView
import org.hinsun.music.presentation.swipe.setting.music.audio.AudioView
import org.hinsun.music.presentation.swipe.setting.music.downloading.DownloadingView
import org.hinsun.music.presentation.swipe.setting.music.storage.StorageView
import org.hinsun.music.presentation.swipe.setting.music.style.StyleView
import timber.log.Timber

@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavGraph(navHostController: NavHostController) {
    SharedTransitionLayout {
        NavHost(navHostController, NavRoute.AUTH.path) {
            // Main Flow
            composable(NavRoute.ONBOARD.path) { OnBoardView(navHostController) }
            composable(NavRoute.AUTH.path) { AuthView(navHostController) }
            composable(NavRoute.SWIPE.path) {
                SwipeView(
                    navHostController = navHostController,
                    animatedVisibilityScope = this
                )
            }

            // Setting
            composable(NavRoute.APPEARANCE.path) { AppearanceView(navHostController) }
            composable(NavRoute.ANIMATION.path) { AnimationView(navHostController) }
            composable(NavRoute.AUDIO.path) { AudioView(navHostController) }
            composable(NavRoute.DOWNLOADING.path) { DownloadingView(navHostController) }
            composable(NavRoute.STORAGE.path) { StorageView(navHostController) }
            composable(NavRoute.STYLE.path) { StyleView(navHostController) }
            composable(NavRoute.DEVELOPER.path) { DeveloperOptionsView(navHostController) }

            // Player
            composable(NavRoute.PLAYER.path) { PlayerView(navHostController, this) }
            composable(NavRoute.PLAYLIST.path) { PlaylistView(navHostController, this) }

            // More
            composable(NavRoute.PROFILE.path) { ProfileView(navHostController) }
            composable(NavRoute.NOTIFICATION.path) { NotificationView(navHostController) }
            composable(NavRoute.AUTHENTICATION.path) { AuthenticationView(navHostController) }
            composable(NavRoute.PRIVACY_POLICY.path) { PrivacyAndPolicyView(navHostController) }
            composable(NavRoute.ABOUT.path) { AboutView(navHostController) }
            composable(NavRoute.CONTACT.path) { ContactView(navHostController) }
        }
    }
}