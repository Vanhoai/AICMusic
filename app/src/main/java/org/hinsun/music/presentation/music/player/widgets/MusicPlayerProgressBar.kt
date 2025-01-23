package org.hinsun.music.presentation.music.player.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.hinsun.music.design.theme.AppTheme

@Composable
fun GradientProgressBar(
    progress: Float,
    currentTime: Int,
    totalTime: Int,
    modifier: Modifier = Modifier,
    height: Dp = 4.dp,
    backgroundColor: Color = Color.Gray.copy(alpha = 0.3f),
    gradientColors: List<Color> = listOf(
        Color(0xFFFFB5A7),
        Color(0xFFFF7B54)
    )
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .clip(RoundedCornerShape(height / 2))
                .background(backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(height / 2))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = gradientColors
                        )
                    )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(currentTime),
                style = AppTheme.typography.normal,
                color = Color.White
            )

            Text(
                text = formatTime(totalTime),
                style = AppTheme.typography.normal,
                color = Color.White,
                modifier = Modifier
            )
        }
    }
}

fun convertSecondsToProgress(
    currentSeconds: Int,
    totalSeconds: Int
): Float {
    return (currentSeconds.toFloat() / totalSeconds).coerceIn(0f, 1f)
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%d:%02d".format(minutes, remainingSeconds)
}

@Composable
fun MusicPlayerProgressBar(
    currentSeconds: Int,
    totalSeconds: Int,
    modifier: Modifier = Modifier
) {
    val progress = convertSecondsToProgress(currentSeconds, totalSeconds)

    GradientProgressBar(
        progress = progress,
        currentTime = currentSeconds,
        totalTime = totalSeconds,
        modifier = modifier.fillMaxWidth(),
        height = 6.dp
    )
}