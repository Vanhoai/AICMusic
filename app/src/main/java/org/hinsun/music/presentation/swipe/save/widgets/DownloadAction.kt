package org.hinsun.music.presentation.swipe.save.widgets

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hinsun.music.R
import org.hinsun.music.design.theme.AppTheme


data class ActionDownload(
    val name: String,
    @DrawableRes val icon: Int
)

val actions = listOf(
    ActionDownload(
        name = "Auto",
        icon = R.drawable.ic_star
    ),
    ActionDownload(
        name = "Audio",
        icon = R.drawable.ic_audio
    ),
)

@Composable
fun DownloadAction() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    var index by remember { mutableIntStateOf(0) }

    val height = 44

    val animationX by animateDpAsState(
        targetValue = if (index == 0) 0.dp else ((screenWidth - 40) / 2).dp,
        animationSpec = SpringSpec(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "AnimatedOffsetX"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF808080))
        )

        Box(
            modifier = Modifier
                .offset(x = animationX)
                .width(((screenWidth - 40) / 2).dp)
                .height(height.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFD9D9D9))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in actions.indices) {
                val colorActive by animateColorAsState(
                    targetValue = if (index == i) Color(0xFFFF972E) else Color.White,
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing),
                    label = "AnimatedColor"
                )

                val textColor by animateColorAsState(
                    targetValue = if (index == i) Color.Black else Color.White,
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing),
                    label = "AnimatedTextColor"
                )

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { index = i },
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = actions[i].icon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(
                            color = colorActive,
                            blendMode = BlendMode.SrcIn
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = actions[i].name,
                        style = AppTheme.typography.normal,
                        fontSize = 16.sp,
                        color = textColor
                    )
                }
            }
        }
    }
}