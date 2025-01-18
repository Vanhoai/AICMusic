package org.hinsun.music.presentation.swipe

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.hinsun.music.R
import org.hinsun.music.design.theme.AppTheme
import org.hinsun.music.design.widgets.base.BaseScaffold
import org.hinsun.music.presentation.graphs.BottomNavItem
import org.hinsun.music.presentation.graphs.CurvedBottomNavigation
import timber.log.Timber
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.roundToInt

@Composable
fun SwipeView(navHostController: NavHostController) {
    var currentRoute by remember { mutableStateOf("home") }

    val items = listOf(
        BottomNavItem(
            title = "Home",
            icon = Icons.Outlined.Home,
            route = "home"
        ),
        BottomNavItem(
            title = "Bookmark",
            icon = Icons.Outlined.FavoriteBorder,
            route = "bookmark"
        ),
        BottomNavItem(
            title = "Save",
            icon = Icons.Outlined.Star,
            route = "save"
        ),
        BottomNavItem(
            title = "Setting",
            icon = Icons.Outlined.Settings,
            route = "setting"
        ),
        BottomNavItem(
            title = "About",
            icon = Icons.Outlined.Info,
            route = "about"
        )
    )

//    BaseScaffold(
//        bottomBar = {
//            CurvedBottomNavigation(
//                items = items,
//                currentRoute = currentRoute,
//                onPress = { item ->
//                    currentRoute = item.route
//                }
//            )
//        }
//    ) { paddingValues ->
//        // Content của màn hình
//    }

    val c = LocalConfiguration.current.screenWidthDp.toFloat()
    val d = 12f
    val r = (d / 2f) + (c * c) / (8f * d)
    val sinX = (c / 2f / r)
    val n = asin(sinX)

    var angle by remember { mutableFloatStateOf((n * 180 / Math.PI).toFloat()) }
    val animatedSweepAngle = animateFloatAsState(
        targetValue = angle,
        animationSpec = SpringSpec(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "AnimatedSweepAngle"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Button(onClick = {
            angle += ((n * 180 / Math.PI) * 2).toFloat()
        }) {
            Text(text = "Click Me")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.White.copy(alpha = 0.3f)),
            ) {

                Canvas(modifier = Modifier.fillMaxSize()) {

                    drawArc(
                        color = Color.White,
                        startAngle = 270f - (animatedSweepAngle.value / 2),
                        sweepAngle = (n * 180 / Math.PI).toFloat(),
                        useCenter = false,
                        topLeft = Offset(size.width / 2f - r, 0f),
                        size = Size(2 * r, 2 * r),
                        style = Stroke(width = 8f, cap = StrokeCap.Round)
                    )

                    drawArc(
                        color = Color.White.copy(alpha = 0.3f),
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(size.width / 2f - r, 0f),
                        size = Size(2 * r, 2 * r),
                        style = Fill
                    )
                }
            }

//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(80.dp)
//            ) {
//                for (i in 0..4) {
//                    val offsetY = if (i <= 2) {
//                        -12f * i
//                    } else {
//                        -12f * (4 - i)
//                    }
//
//                    Column(
//                        modifier = Modifier
//                            .weight(1f)
//                            .offset(y = offsetY.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Image(
//                            painter = painterResource(R.drawable.ic_home),
//                            contentDescription = null
//                        )
//
//                        Spacer(modifier = Modifier.height(4.dp))
//
//                        Text(
//                            text = items[i].title,
//                            style = AppTheme.typography.normal,
//                            fontSize = 16.sp,
//                            color = Color.White
//                        )
//                    }
//                }
//            }
        }
    }
}