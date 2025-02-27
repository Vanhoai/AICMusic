package org.hinsun.music.core.ui.design.widgets.base

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke


@Composable
fun BaseLoading(
    modifier: Modifier = Modifier,
    circleColors: List<Color> = listOf(
        Color(0xFFFFB7B7),
        Color(0xFFD4A6FF),
        Color(0xFFC6FF81),
        Color(0xFFFFD98D),
        Color(0xFFFFB7B7),
    ),
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")

    // Animate the angle for the orbiting dot
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = size / 2f
        val orbitRadius = size.minDimension / 2f + 12f
        val arcWidth = 4f

        drawArc(
            brush = Brush.sweepGradient(colors = circleColors),
            startAngle = angle,
            sweepAngle = 120f,
            useCenter = false,
            topLeft = Offset(center.width - orbitRadius, center.height - orbitRadius),
            size = Size(orbitRadius * 2, orbitRadius * 2),
            style = Stroke(width = arcWidth, cap = StrokeCap.Round)
        )

        drawArc(
            brush = Brush.sweepGradient(colors = circleColors),
            startAngle = angle + 180f,
            sweepAngle = 120f,
            useCenter = false,
            topLeft = Offset(center.width - orbitRadius, center.height - orbitRadius),
            size = Size(orbitRadius * 2, orbitRadius * 2),
            style = Stroke(width = arcWidth, cap = StrokeCap.Round)
        )
    }
}