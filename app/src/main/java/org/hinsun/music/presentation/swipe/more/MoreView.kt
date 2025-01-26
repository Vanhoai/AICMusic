package org.hinsun.music.presentation.swipe.more

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
import org.hinsun.music.design.widgets.shared.SharedGradientOutlineImage
import org.hinsun.music.presentation.graphs.NavRoute
import org.hinsun.music.presentation.swipe.setting.settingGroups
import org.hinsun.music.presentation.swipe.widgets.GroupOption
import org.hinsun.music.presentation.swipe.widgets.OptionNavigate
import org.hinsun.music.presentation.swipe.widgets.SingleOption


val moreGroups = listOf(
    GroupOption(
        name = "Account",
        options = listOf(
            SingleOption(
                name = "Profile",
                icon = R.drawable.ic_account,
                background = Color(0xFF5C95FF),
                route = NavRoute.APPEARANCE
            ),
            SingleOption(
                name = "Notification",
                icon = R.drawable.ic_notification,
                background = Color(0xFFFFA85C),
                route = NavRoute.ANIMATION
            ),
        ),
    ),
    GroupOption(
        name = "Security",
        options = listOf(
            SingleOption(
                name = "Biometric Authentication",
                icon = R.drawable.ic_fingerprint,
                background = Color(0xFFFF7A5C),
                route = NavRoute.AUDIO
            ),
            SingleOption(
                name = "Privacy & Policy",
                icon = R.drawable.ic_privacy,
                background = Color(0xFFFF7A5C),
                route = NavRoute.DOWNLOADING
            ),
        ),
    ),
    GroupOption(
        name = "About",
        options = listOf(
            SingleOption(
                name = "About Hinsun Music",
                icon = R.drawable.ic_audio,
                background = Color(0xFFB7B7B7),
                route = NavRoute.AUDIO
            ),
            SingleOption(
                name = "Contact Us",
                icon = R.drawable.ic_about,
                background = Color(0xFFB7B7B7),
                route = NavRoute.DOWNLOADING
            ),
        ),
    ),
)

@Composable
fun MoreView(navHostController: NavHostController) {
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
                groups = moreGroups,
                onPressOption = { path -> navHostController.navigate(path) }
            )
        }
    }
}