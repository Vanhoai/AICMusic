package org.hinsun.music.presentation.onboard.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun VinylRecord(modifier: Modifier = Modifier) {
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
