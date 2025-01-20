package org.hinsun.music.presentation.swipe.save.widgets

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hinsun.music.R
import org.hinsun.music.design.theme.AppTheme
import org.hinsun.music.design.theme.fontFamily
import org.hinsun.music.design.widgets.base.BaseButton
import org.hinsun.music.design.widgets.shared.SharedGradientButton

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun DownloadProgress() {

    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val screenWidth = configuration.screenWidthDp
    val canvasSize = screenWidth * 0.6

    val progress by remember { mutableFloatStateOf(0f) }
    var animatedProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(progress) {
        animate(
            initialValue = animatedProgress,
            targetValue = progress,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        ) { value, _ ->
            animatedProgress = value
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Download",
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
                    Toast.makeText(context, "Start download audio", Toast.LENGTH_SHORT).show()
                }
        ) {

            val path = Path()
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

//            path.addArc(
//                Rect(
//                    center = center,
//                    radius = radius
//                ),
//                180f,
//                360f
//            )
//            path.arcTo(
//                rect = Rect(
//                    center = center,
//                    radius = radius
//                ),
//                180f,
//                -360f * (animatedProgress / 100f),
//                forceMoveTo = true
//            )
//            path.close()
//
//            drawPath(
//                path = path,
//                color = Color(0xFF89FF52)
//            )

            drawContext.canvas.nativeCanvas.apply {
                // val text = "${animatedProgress.toInt()}%"
                val text = "Start"

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
            BaseButton(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Cancel",
                    style = AppTheme.typography.normal,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            SharedGradientButton(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Ok (5s)",
                    style = AppTheme.typography.normal,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}