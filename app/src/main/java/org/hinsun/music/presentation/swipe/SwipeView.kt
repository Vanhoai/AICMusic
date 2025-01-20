package org.hinsun.music.presentation.swipe

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.hinsun.music.R
import org.hinsun.music.design.widgets.base.BaseScaffold
import org.hinsun.music.presentation.graphs.BottomNavItem
import org.hinsun.music.presentation.graphs.CurvedBottomNavigation
import org.hinsun.music.presentation.graphs.SwipeRoute
import org.hinsun.music.presentation.swipe.about.AboutView
import org.hinsun.music.presentation.swipe.bookmark.BookmarkView
import org.hinsun.music.presentation.swipe.home.HomeView
import org.hinsun.music.presentation.swipe.save.SaveView
import org.hinsun.music.presentation.swipe.setting.SettingView

val items = listOf(
    BottomNavItem(
        title = "Home",
        icon = R.drawable.ic_home,
        route = SwipeRoute.HOME
    ),
    BottomNavItem(
        title = "Bookmark",
        icon = R.drawable.ic_heart,
        route = SwipeRoute.BOOKMARK
    ),
    BottomNavItem(
        title = "Save",
        icon = R.drawable.ic_save,
        route = SwipeRoute.SAVE
    ),
    BottomNavItem(
        title = "Setting",
        icon = R.drawable.ic_settings,
        route = SwipeRoute.SETTING
    ),
    BottomNavItem(
        title = "About",
        icon = R.drawable.ic_about,
        route = SwipeRoute.ABOUT
    )
)

@Composable
fun SwipeView(navHostController: NavHostController) {
    val navController = rememberNavController()

    val activity = LocalContext.current as Activity
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination

    BackHandler {
        if (currentRoute?.route == SwipeRoute.HOME.path) activity.finish()
    }

    BaseScaffold(
        bottomBar = {
            CurvedBottomNavigation(
                items = items,
                onPress = { item ->
                    navController.navigate(item.route.path) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = false
                            saveState = false
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            navController = navController,
            startDestination = SwipeRoute.SAVE.path
        ) {
            composable(SwipeRoute.HOME.path) {
                HomeView()
            }

            composable(SwipeRoute.BOOKMARK.path) {
                BookmarkView()
            }

            composable(SwipeRoute.SAVE.path) {
                SaveView()
            }

            composable(SwipeRoute.SETTING.path) {
                SettingView()
            }

            composable(SwipeRoute.ABOUT.path) {
                AboutView()
            }
        }
    }
}