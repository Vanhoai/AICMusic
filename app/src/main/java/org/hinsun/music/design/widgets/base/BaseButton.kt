package org.hinsun.music.design.widgets.base

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.BottomCenter,
    brush: Brush = Brush.horizontalGradient(
        colors = listOf(
            Color.White,
            Color.White
        )
    ),
    behindColor: Color = Color.White.copy(alpha = 0.4f),
    content: @Composable () -> Unit
) {
    val isPressed = remember { mutableStateOf(false) }
    val offsetY by animateDpAsState(
        targetValue = if (isPressed.value) 0.dp else (-12).dp,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "AnimatedOffsetY"
    )

    Box(
        modifier = modifier
            .height(72.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed.value = true
                        tryAwaitRelease()
                        isPressed.value = false
                    }
                )
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .background(behindColor, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
        )

        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .offset(y = offsetY)
                .background(brush, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = contentAlignment
        ) {
            content()
        }
    }
}