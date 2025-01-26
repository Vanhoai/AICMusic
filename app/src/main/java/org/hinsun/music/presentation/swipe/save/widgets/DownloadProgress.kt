package org.hinsun.music.presentation.swipe.save.widgets

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.hinsun.music.R
import org.hinsun.music.design.theme.AppTheme
import org.hinsun.music.design.widgets.base.BaseButton
import org.hinsun.music.design.widgets.shared.SharedGradientButton
import kotlin.math.PI
import kotlin.math.sin

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun DownloadProgress(
    downloadProgress: Float = 0f,
    onStartDownload: () -> Unit = {},
    onCancel: () -> Unit = {},
    onOk: () -> Unit = {},
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val screenWidth = configuration.screenWidthDp
    val canvasSize = screenWidth * 0.6

    var animationState by remember { mutableFloatStateOf(0f) }
    var timer by remember { mutableIntStateOf(5) }

    LaunchedEffect(downloadProgress == 100f) {
        if (downloadProgress == 100f) {
            while (timer > 0) {
                timer--
                delay(1000)
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            animate(
                initialValue = 0f,
                targetValue = 2 * PI.toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            ) { value, _ ->
                animationState = value
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Download Progress",
            style = AppTheme.typography.normal,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(32.dp))

        Canvas(
            modifier = Modifier
                .size(canvasSize.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .clickable {
                    if (downloadProgress == 0f) onStartDownload()
                }
        ) {
            val size = Size(size.width, size.height)
            val radius = size.width / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            drawArc(
                color = Color.White,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(size.width / 2f - radius, 0f),
                style = Stroke(width = 4f, cap = StrokeCap.Round),
                size = Size(2 * radius, 2 * radius)
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = radius,
                center = center
            )

            // Function to create wave paths
            fun createWavePath(amplitude: Float, frequency: Float, phase: Float): Pair<Path, Path> {
                val wavePath = Path()
                val fillPath = Path()

                val startY = center.y + radius - (2 * radius * downloadProgress / 100f)
                fillPath.moveTo(-radius, size.height)

                for (x in (-radius.toInt())..(radius.toInt())) {
                    val xPos = center.x + x
                    val yPos = startY + amplitude *
                            sin((frequency * x / radius) + animationState + phase)

                    if (x == -radius.toInt()) {
                        wavePath.moveTo(xPos, yPos)
                        fillPath.moveTo(xPos, yPos)
                    } else {
                        wavePath.lineTo(xPos, yPos)
                        fillPath.lineTo(xPos, yPos)
                    }
                }

                fillPath.lineTo(center.x + radius, size.height)
                fillPath.lineTo(center.x - radius, size.height)
                fillPath.close()

                return Pair(wavePath, fillPath)
            }


            // Define parameters for three waves
            val waveParams = listOf(
                Triple(radius / 16f, 8f, 0f),
                Triple(radius / 12f, 6f, PI.toFloat() / 4),
                Triple(radius / 10f, 4f, PI.toFloat() / 2)
            )

            // Draw waves from largest to smallest
            waveParams.reversed().forEachIndexed { index, (amplitude, frequency, phase) ->
                val (wavePath, fillPath) = createWavePath(amplitude, frequency, phase)

                // Draw filled area with different alphas
                val colorFill = when (index) {
                    0 -> Color(0xFF93FF61)
                    1 -> Color(0xFF93FF61).copy(alpha = 0.5f)
                    2 -> Color(0xFF93FF61).copy(alpha = 0.3f)
                    else -> Color(0xFF93FF61)
                }

                drawPath(
                    path = fillPath,
                    color = colorFill,
                    style = Fill
                )
            }

            drawContext.canvas.nativeCanvas.apply {
                val text =
                    if (downloadProgress == 0f) "Start" else "%.2f".format(downloadProgress) + "%"
                val type = context.resources.getFont(R.font.ibm_flex_mono_medium)

                val paint = android.graphics.Paint().apply {
                    textAlign = android.graphics.Paint.Align.CENTER
                    wordSpacing = 2f
                    textSize = size.width * 0.15f
                    color = android.graphics.Color.WHITE
                    typeface = type
                    isFakeBoldText = true
                }

                drawText(
                    text,
                    center.x,
                    center.y + paint.textSize / 3,
                    paint
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 40.dp, bottom = 40.dp)
        ) {
            BaseButton(modifier = Modifier.weight(1f), onPress = { onCancel() }) {
                Text(
                    text = "Cancel",
                    style = AppTheme.typography.normal,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            SharedGradientButton(modifier = Modifier.weight(1f), onPress = { onOk() }) {
                Text(
                    text = if (downloadProgress == 0f) "Ok" else "Ok (${timer}s)",
                    style = AppTheme.typography.normal,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}