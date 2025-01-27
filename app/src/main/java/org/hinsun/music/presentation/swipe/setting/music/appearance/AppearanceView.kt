package org.hinsun.music.presentation.swipe.setting.music.appearance

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.hinsun.music.design.widgets.base.BaseScaffold
import org.hinsun.music.design.widgets.shared.SharedTopBar
import org.hinsun.music.presentation.swipe.setting.music.widgets.buildAccessibility
import org.hinsun.music.presentation.swipe.setting.music.widgets.buildAppearanceBrightAreaBackground
import org.hinsun.music.presentation.swipe.setting.music.widgets.buildAppearanceTheme

@Composable
fun AppearanceView(navHostController: NavHostController) {

    val scrollState = rememberScrollState()

    BaseScaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .scrollable(scrollState, orientation = Orientation.Vertical)
        ) {
            item {
                SharedTopBar(
                    name = "Appearance",
                    onBackPress = { navHostController.popBackStack() })
            }

            buildAppearanceTheme()
            buildAppearanceBrightAreaBackground()
            buildAccessibility()
        }
    }
}