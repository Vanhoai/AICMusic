package org.hinsun.music.presentation.swipe.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.hinsun.music.R
import org.hinsun.music.core.ui.design.widgets.shared.SharedGradientOutlineImage
import org.hinsun.music.presentation.graphs.NavRoute
import org.hinsun.music.presentation.swipe.widgets.GroupOption
import org.hinsun.music.presentation.swipe.widgets.OptionNavigate
import org.hinsun.music.presentation.swipe.widgets.SingleOption

val settingGroups = listOf(
    GroupOption(
        name = "Appearance",
        options = listOf(
            SingleOption(
                name = "Theme & Language",
                icon = R.drawable.ic_light,
                background = Color(0xFF5C95FF),
                route = NavRoute.APPEARANCE
            ),
            SingleOption(
                name = "Animation",
                icon = R.drawable.ic_animation,
                background = Color(0xFFFFA85C),
                route = NavRoute.ANIMATION
            ),
        ),
    ),
    GroupOption(
        name = "Music",
        options = listOf(
            SingleOption(
                name = "Audio",
                icon = R.drawable.ic_audio_outline,
                background = Color(0xFF14D01A),
                route = NavRoute.AUDIO
            ),
            SingleOption(
                name = "Downloading",
                icon = R.drawable.ic_downloading,
                background = Color(0xFF14D01A),
                route = NavRoute.DOWNLOADING
            ),
            SingleOption(
                name = "Storage",
                icon = R.drawable.ic_storage,
                background = Color(0xFF14D01A),
                route = NavRoute.STORAGE
            ),
            SingleOption(
                name = "Style player music",
                icon = R.drawable.ic_style,
                background = Color(0xFF14D01A),
                route = NavRoute.STYLE
            ),
        ),
    ),
    GroupOption(
        name = "Advanced",
        options = listOf(
            SingleOption(
                name = "Developer options",
                icon = R.drawable.ic_develop,
                background = Color(0xFFB7B7B7),
                route = NavRoute.DEVELOPER
            ),
        ),
    )
)

@Composable
fun SettingView(navHostController: NavHostController) {
    val scrollState = rememberScrollState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(scrollState, orientation = Orientation.Vertical)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null,
                )

                SharedGradientOutlineImage()
            }
        }

        item {
            OptionNavigate(
                groups = settingGroups,
                onPressOption = { path -> navHostController.navigate(path) }
            )
        }
    }
}