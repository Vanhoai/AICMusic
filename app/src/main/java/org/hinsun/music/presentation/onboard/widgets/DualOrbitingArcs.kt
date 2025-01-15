package org.hinsun.music.presentation.onboard.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hinsun.music.design.widgets.shared.SharedWaveAnimation
import kotlin.math.roundToInt

@Composable
fun DualOrbitingArcs(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
    val coroutineScope = rememberCoroutineScope()

    val centerOffset = remember { mutableStateOf(Offset.Zero) }
    val vinylOffset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val maxRadius = 200f

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
            .height(300.dp)
            .fillMaxWidth(),
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

        Box(
            modifier = Modifier
                .size(200.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {},
                        onDragEnd = {
                            coroutineScope.launch {
                                vinylOffset.animateTo(Offset.Zero, animationSpec = spring())
                            }
                        },
                        onDragCancel = {
                            coroutineScope.launch {
                                vinylOffset.animateTo(Offset.Zero, animationSpec = spring())
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()

                            val newOffset = vinylOffset.value + dragAmount

                            // calculate distance from center
                            val distanceFromCenter = newOffset.getDistance()

                            // Limit the offset to the maximum radius
                            val limitedOffset = if (distanceFromCenter <= maxRadius) {
                                newOffset
                            } else {
                                newOffset.copy(x = maxRadius * newOffset.x / distanceFromCenter)
                            }

                            coroutineScope.launch(Dispatchers.Main) {
                                vinylOffset.animateTo(limitedOffset, animationSpec = spring())
                            }
                        }
                    )
                }
                .onGloballyPositioned { layoutCoordinates ->
                    centerOffset.value = layoutCoordinates.size.center.toOffset()
                }
                .offset {
                    IntOffset(
                        vinylOffset.value.x.roundToInt(),
                        vinylOffset.value.y.roundToInt()
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            VinylRecord()
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = size / 2f
                val orbitRadius = size.minDimension / 2f + 12f
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

        SharedWaveAnimation()
    }
}