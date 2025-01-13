package org.ic.tech.music.design.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import timber.log.Timber

@Stable
class ICMusicColors(
    backgroundPrimary: Color = Color.Black,
    textPrimary: Color = Color.White
) {

    var backgroundPrimary by mutableStateOf(backgroundPrimary)
        private set

    var textPrimary by mutableStateOf(textPrimary)
        private set

    @Composable
    fun getAnimatedBackgroundColor(): Color {
        return animateColorAsState(
            label = "Animate Background Color",
            targetValue = backgroundPrimary,
            animationSpec = tween(durationMillis = 500)
        ).value
    }

    @Composable
    fun getAnimatedTextColor(): Color {
        return animateColorAsState(
            label = "Animate Text Color",
            targetValue = textPrimary,
            animationSpec = tween(durationMillis = 500)
        ).value
    }


    fun copy(): ICMusicColors {
        return ICMusicColors(backgroundPrimary, textPrimary)
    }

    fun updateLightTheme() {
        backgroundPrimary = Color.White
        textPrimary = Color.Black

        Timber.tag("AppTheme").d("Update Light Theme")
    }

    fun updateDarkTheme() {
        backgroundPrimary = Color.Black
        textPrimary = Color.White
    }
}

val LocalColors = staticCompositionLocalOf<ICMusicColors> {
    error("Colors not initialized")
}