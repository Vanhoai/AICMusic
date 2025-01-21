package org.hinsun.music.design.widgets.base

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hinsun.music.design.theme.AppTheme

data class TabSliderOption(
    val name: String,
    @DrawableRes val icon: Int? = null,
)

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun BaseTabSlider(
    options: List<TabSliderOption>,
    borderRadius: Int = 8,
    height: Int = 44,
    innerPadding: Int = 4,
    modifier: Modifier = Modifier
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val sliderWidth = (screenWidth - 40 - ((options.size + 1) * innerPadding)) / options.size

    var indexSelected by remember { mutableIntStateOf(0) }
    val offsetX by animateDpAsState(
        targetValue = ((sliderWidth + innerPadding * 2) * indexSelected).dp,
        animationSpec = SpringSpec(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "AnimatedOffsetX"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
                .clip(RoundedCornerShape(borderRadius.dp))
                .background(Color(0xFF4A4A4A), RoundedCornerShape(borderRadius.dp))
                .border(1.dp, Color(0xFF595959), RoundedCornerShape(borderRadius.dp))
        )

        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .width(sliderWidth.dp)
                .height(height.dp)
                .padding(innerPadding.dp)
                .clip(RoundedCornerShape((borderRadius - innerPadding).dp))
                .background(Color(0xFFD9D9D9))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in options.indices) {

                val textColor by animateColorAsState(
                    targetValue = if (indexSelected == i) Color.Black else Color.White,
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing),
                    label = "AnimatedTextColor"
                )

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { indexSelected = i },
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (options[i].icon != null) {
                        val colorActive by animateColorAsState(
                            targetValue = if (indexSelected == i) Color(0xFFFF972E) else Color.White,
                            animationSpec = tween(durationMillis = 300, easing = LinearEasing),
                            label = "AnimatedColor"
                        )

                        Image(
                            painter = painterResource(id = options[i].icon!!),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(
                                color = colorActive,
                                blendMode = BlendMode.SrcIn
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = options[i].name,
                        style = AppTheme.typography.normal,
                        fontSize = 16.sp,
                        color = textColor
                    )
                }
            }
        }
    }
}