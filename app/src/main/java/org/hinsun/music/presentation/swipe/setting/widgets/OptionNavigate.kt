package org.hinsun.music.presentation.swipe.setting.widgets

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hinsun.music.R
import org.hinsun.music.design.theme.AppTheme
import org.hinsun.music.presentation.graphs.NavRoute

data class SingleOption(
    val name: String,
    @DrawableRes val icon: Int,
    val background: Color,
    val route: NavRoute
)

data class GroupOption(
    val name: String,
    val options: List<SingleOption>
)

val groups = listOf(
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
                route = NavRoute.DEVELOP
            ),
        ),
    )
)

@Composable
fun OptionNavigate(onPressOption: (path: String) -> Unit) {

    val interactionSource = remember { MutableInteractionSource() }

    groups.forEach { group ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = group.name,
                style = AppTheme.typography.normal,
                fontSize = 18.sp,
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF5A5A5A), RoundedCornerShape(8.dp))
                    .background(Color(0xFF373737))
            ) {
                group.options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(horizontal = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                onPressOption(option.route.path)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(option.background),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(option.icon),
                                contentDescription = null
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = option.name,
                            style = AppTheme.typography.normal,
                            fontSize = 16.sp,
                            color = AppTheme.colors.textPrimary,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color(0xFF5A5A5A))
                    )
                }
            }
        }

    }
}