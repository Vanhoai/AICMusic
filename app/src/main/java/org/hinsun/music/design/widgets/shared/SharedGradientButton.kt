package org.hinsun.music.design.widgets.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.hinsun.music.design.widgets.base.BaseButton

@Composable
fun SharedGradientButton(
    modifier: Modifier = Modifier,
    onPress: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val horizontalGradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFFFCD6A),
            Color(0xFFFF8E52)
        )
    )


    BaseButton(
        modifier = modifier,
        brush = horizontalGradientBrush,
        behindColor = Color(0xFFFFCD6A).copy(alpha = 0.5f),
        contentAlignment = Alignment.Center,
        onPress = onPress
    ) {
        content()
    }
}