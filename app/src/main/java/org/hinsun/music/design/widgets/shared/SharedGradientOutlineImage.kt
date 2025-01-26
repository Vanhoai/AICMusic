package org.hinsun.music.design.widgets.shared

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import org.hinsun.music.design.widgets.base.BaseImage

@Composable
fun SharedGradientOutlineImage() {
    val infiniteTransition = rememberInfiniteTransition()
    val angle = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .drawWithContent {
                drawContent()
                val radius = size.minDimension / 2
                val center = this.center

                val gradient = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFFFF8A8A),
                        Color(0xFFCD94FF),
                        Color(0xFFA8FF44),
                        Color(0xFFFFCA61),
                        Color(0xFFFF8A8A),
                    ),
                    center = center
                )

                rotate(angle.value, center) {
                    drawCircle(
                        brush = gradient,
                        radius = radius,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        BaseImage(shape = CircleShape)
    }
}