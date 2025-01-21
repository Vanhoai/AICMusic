package org.hinsun.music.design.widgets.base

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BaseSwitch(
    isActive: Boolean = false,
    onChange: (Boolean) -> Unit = {}
) {
    val thumbSize = 28
    val trackWidth = 51
    val paddingSize = 2

    val transition = updateTransition(isActive, label = "switch_transition")

    // Animate horizontal movement
    val offsetX by transition.animateDp(
        label = "offsetX",
        transitionSpec = {
            tween(durationMillis = 300, easing = LinearOutSlowInEasing)
        }
    ) { active ->
        if (active) (trackWidth - thumbSize - paddingSize * 2).dp else 0.dp
    }

    // Animate scale based on position
    val scale by transition.animateFloat(
        label = "scale",
        transitionSpec = {
            tween(durationMillis = 300, easing = LinearEasing)
        }
    ) { active ->
        // Calculate progress (0f to 1f)
        val progress = if (active) {
            offsetX.value / (trackWidth - thumbSize - paddingSize * 2)
        } else {
            1f - offsetX.value / (trackWidth - thumbSize - paddingSize * 2)
        }

        // Scale calculation:
        // - From start to middle (0 to 0.5): scale up from 1f to 1.2f
        // - From middle to end (0.5 to 1): scale down from 1.2f to 1f
        when {
            progress < 0.5f -> 1f + (progress * 2 * 0.4f) // Scale up from 1.0 to 1.4f
            else -> 1.4f - ((progress - 0.5f) * 2 * 0.4f) // Scale down from 1.4f to 1.0f
        }
    }

    val trackColor by animateColorAsState(
        targetValue = if (isActive) Color(0xFFFF8E52) else Color(0xFF818181),
        animationSpec = tween(durationMillis = 300, easing = LinearEasing)
    )

    // Track
    Box(
        modifier = Modifier
            .width(trackWidth.dp)
            .height((thumbSize + paddingSize * 2).dp)
            .clip(RoundedCornerShape((trackWidth / 2).dp))
            .background(trackColor, RoundedCornerShape((trackWidth / 2).dp))
            .padding(paddingSize.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onChange(!isActive)
            }
    ) {
        // Thumb
        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .size(thumbSize.dp)
                .scale(scaleX = scale, scaleY = 1f) // Apply scale transform
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}