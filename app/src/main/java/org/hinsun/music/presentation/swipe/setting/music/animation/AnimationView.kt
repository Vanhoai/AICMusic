package org.hinsun.music.presentation.swipe.setting.music.animation

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.hinsun.music.core.ui.design.widgets.base.BaseScaffold
import org.hinsun.music.core.ui.design.widgets.shared.SharedTopBar
import org.hinsun.music.presentation.swipe.setting.music.widgets.buildCanvas
import org.hinsun.music.presentation.swipe.setting.music.widgets.buildMotion

@Composable
fun AnimationView(navHostController: NavHostController) {
    val scrollState = rememberScrollState()

    BaseScaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .scrollable(scrollState, orientation = Orientation.Vertical)
        ) {
            item {
                SharedTopBar(name = "Animation", onBackPress = { navHostController.popBackStack() })
            }

            buildMotion()
            buildCanvas()
        }
    }
}