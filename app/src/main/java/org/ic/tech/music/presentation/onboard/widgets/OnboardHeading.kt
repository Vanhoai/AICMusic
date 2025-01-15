package org.ic.tech.music.presentation.onboard.widgets

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.ic.tech.music.R

@Composable
fun OnboardHeading(sizeBox: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AnimatedImage(height = sizeBox, width = sizeBox, id = R.drawable.onboard_1)
        AnimatedImage(height = sizeBox, width = sizeBox, id = R.drawable.onboard_1)
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