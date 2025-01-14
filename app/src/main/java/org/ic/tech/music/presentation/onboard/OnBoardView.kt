package org.ic.tech.music.presentation.onboard

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import org.ic.tech.music.R
import org.ic.tech.music.design.theme.AppTheme
import org.ic.tech.music.design.widgets.base.BaseScaffold
import org.ic.tech.music.presentation.graphs.NavRoute


@Composable
fun OnBoardView(navHostController: NavHostController) {
    val viewModel = hiltViewModel<OnBoardViewModel>()
    val uiState = viewModel.uiState.collectAsState().value

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val sizeBox = (screenWidth - 40 - 8) / 2

    BaseScaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedImage(height = sizeBox, width = sizeBox, id = R.drawable.onboard_9)
                AnimatedImage(height = sizeBox, width = sizeBox, id = R.drawable.onboard_1)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DualOrbitingArcs()
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Music",
                style = AppTheme.typography.normal,
                fontWeight = FontWeight.Black,
                fontSize = 100.sp,
                color = AppTheme.colors.textPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            val horizontalGradientBrush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFFFCD6A),
                    Color(0xFFFF8E52)
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush = horizontalGradientBrush)
                    .height(60.dp)
                    .clickable {
                        navHostController.navigate(NavRoute.AUTH.path)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Get Started",
                    style = AppTheme.typography.normal,
                    fontSize = 20.sp,
                    color = AppTheme.colors.textPrimary
                )
            }
        }
    }
}

@Composable
fun DualOrbitingArcs(modifier: Modifier = Modifier) {
    val isPressed = remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")

    // Animate the angle for the orbiting dot
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    val brush = Brush.verticalGradient(listOf(Color(0xFFC0FFA3), Color(0xFFFECCFF)))

    Box(
        modifier = modifier
            .size(300.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed.value = true
                        tryAwaitRelease()
                        isPressed.value = false
                    }
                )
            }
            .scale(
                animateFloatAsState(
                    targetValue = if (!isPressed.value) 1f else 0.9f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
                    label = "SpringEffect"
                ).value
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
                .offset(x = (-20).dp, y = (-20).dp)
                .blur(160.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .clip(RoundedCornerShape(100.dp))
                .background(brush = brush),
        )


        VinylRecord()
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = size / 2f
            val orbitRadius = size.minDimension / 2.5f - 20f
            val arcWidth = 4f

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFFC0FFA3),
                        Color(0xFFFECCFF),
                        Color(0xFFC0FFA3)
                    )
                ),
                startAngle = angle,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(center.width - orbitRadius, center.height - orbitRadius),
                size = Size(orbitRadius * 2, orbitRadius * 2),
                style = Stroke(width = arcWidth, cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFFFECCFF),
                        Color(0xFFC0FFA3),
                        Color(0xFFFECCFF)
                    )
                ),
                startAngle = angle + 180f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(center.width - orbitRadius, center.height - orbitRadius),
                size = Size(orbitRadius * 2, orbitRadius * 2),
                style = Stroke(width = arcWidth, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun VinylRecord(modifier: Modifier = Modifier, isSpinning: Boolean = true) {
    Box(modifier = modifier.size(200.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.Black,
                radius = size.minDimension / 2
            )

            for (i in 1..12) {
                drawCircle(
                    color = Color.DarkGray,
                    radius = size.minDimension / 2 * (1 - i * 0.1f),
                    style = Stroke(width = 2f)
                )
            }

            drawCircle(
                color = Color.White,
                radius = size.minDimension / 10
            )
        }
    }
}


@Composable
fun AnimatedImage(
    width: Int,
    height: Int,
    @DrawableRes id: Int,
) {
    val isPressed = remember { mutableStateOf(false) }

    Image(
        painter = painterResource(id),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .height(height.dp)
            .width(width.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed.value = true
                        tryAwaitRelease()
                        isPressed.value = false
                    }
                )
            }
            .scale(
                animateFloatAsState(
                    targetValue = if (isPressed.value) 0.9f else 1f,
                    animationSpec = tween(300), label = "AnimatedScale"
                ).value
            ),
    )
}