package org.hinsun.music.design.widgets.shared

import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import org.hinsun.music.MainActivity
import org.hinsun.music.R
import timber.log.Timber
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun SharedWaveAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "InfiniteTransition")
    val context = LocalContext.current

    val resource = context.resources

    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Phase1"
    )

    val phase2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Phase2"
    )

    val phase3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Phase3"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Constants
        val amplitude = size.height / 6 // Amplitude of each wave
        val frequency = 2f // Number of cycles for each wave

        // Draw multiple sine waves
        drawSineWave(Color.White, amplitude, frequency, phase1)
        drawSineWave(Color.White, amplitude * 1.15f, frequency + 1, phase2)
        drawSineWave(Color.White, amplitude / 1.3f, frequency - 0.5f, phase3)
    }
}

private fun DrawScope.drawSineWave(
    color: Color,
    amplitude: Float,
    frequency: Float,
    phase: Float
) {
    val points = mutableListOf<Offset>()
    for (x in 0..size.width.toInt()) {
        val normalizedX = x / size.width
        val y =
            (amplitude * sin(2 * PI * frequency * normalizedX + phase)).toFloat() + size.height / 2
        points.add(Offset(x.toFloat(), y))
    }

    for (i in 0 until points.size - 1) {
        drawLine(
            color = color,
            start = points[i],
            end = points[i + 1],
            strokeWidth = 1f
        )
    }
}