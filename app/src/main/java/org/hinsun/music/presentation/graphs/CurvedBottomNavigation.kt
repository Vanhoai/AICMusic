package org.hinsun.music.presentation.graphs

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.hinsun.music.R
import org.hinsun.music.core.ui.design.theme.AppTheme
import org.hinsun.music.core.ui.design.widgets.base.BaseImage
import org.hinsun.music.presentation.music.mini.MiniPlayer
import kotlin.math.asin

data class BottomNavItem(
    val title: String,
    @DrawableRes val icon: Int,
    val route: SwipeRoute
)

const val idNameTransition = "IdNameTransition"
const val idImageTransition = "IdImageTransition"
const val idBackgroundTransition = "IdBackgroundTransition"

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CurvedBottomNavigation(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navHostController: NavHostController,
    currentRoute: String = SwipeRoute.HOME.path,
    items: List<BottomNavItem>,
    onPress: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {

    val c = LocalConfiguration.current.screenWidthDp.toFloat()
    val d = 12f
    val r = (d / 2f) + (c * c) / (8f * d)
    val sinX = (c / 2f / r)
    val n = asin(sinX)

    val alpha = (n * 180 / Math.PI).toFloat() * 0.75f

    fun calculateAngle(currentRoute: String = SwipeRoute.HOME.path): Float {
        var i = 0
        when (currentRoute) {
            SwipeRoute.HOME.path -> i = 0
            SwipeRoute.BOOKMARK.path -> i = 1
            SwipeRoute.SAVE.path -> i = 2
            SwipeRoute.SETTING.path -> i = 3
            SwipeRoute.MORE.path -> i = 4
        }

        return (-2f * alpha) + (i * alpha)
    }

    var angle by remember { mutableFloatStateOf(calculateAngle(currentRoute = currentRoute)) }
    val animatedSweepAngle = animateFloatAsState(
        targetValue = angle,
        animationSpec = SpringSpec(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "AnimatedSweepAngle"
    )

    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(172.dp)
            .background(Color(0xFF3E3E3E))
    ) {
        MiniPlayer(
            navHostController = navHostController,
            interactionSource = interactionSource,
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.Transparent),
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                drawArc(
                    color = Color(0xFF323232),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(size.width / 2f - r, 0f),
                    size = Size(2 * r, 2 * r),
                    style = Fill
                )

                drawArc(
                    color = Color(0xFF575757),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(size.width / 2f - r, 0f),
                    size = Size(2 * r, 2 * r),
                    style = Stroke(width = 1f, cap = StrokeCap.Round)
                )

                val spacingRadius = 4f

                drawArc(
                    color = Color(0xFFFF5E3E),
                    startAngle = 270f - (alpha / 2) + animatedSweepAngle.value,
                    sweepAngle = alpha,
                    useCenter = false,
                    topLeft = Offset(size.width / 2f - r + spacingRadius, 0f + spacingRadius),
                    size = Size(2 * r + spacingRadius, 2 * r + spacingRadius),
                    style = Stroke(width = spacingRadius * 2, cap = StrokeCap.Square)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0..4) {
                    val offsetY = if (i <= 2) {
                        (-12f * i)
                    } else {
                        -12f * (4 - i)
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .offset(y = (offsetY + 10f).dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                coroutineScope.launch {
                                    val newAngle = withContext(Dispatchers.Default) {
                                        (-2f * alpha) + (i * alpha)
                                    }

                                    withContext(Dispatchers.Main) {
                                        angle = newAngle

                                        delay(600)
                                        onPress(items[i])
                                    }
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = items[i].icon),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = items[i].title,
                            style = AppTheme.typography.normal,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

